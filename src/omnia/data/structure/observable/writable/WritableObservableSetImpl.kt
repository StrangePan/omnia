package omnia.data.structure.observable.writable

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream
import omnia.algorithm.SetAlgorithms
import omnia.data.stream.Collectors
import omnia.data.structure.Collection
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.observable.ObservableSet
import omnia.data.structure.observable.ObservableSet.SetOperation

internal class WritableObservableSetImpl<E : Any> : WritableObservableSet<E> {

  @Volatile
  private var currentState: ImmutableSet<E> = ImmutableSet.empty()
  private val mutationEvents: Subject<MutationEvent> =
    PublishSubject.create<MutationEvent>().toSerialized()
  private val observableChannels: ObservableChannels = ObservableChannels()

  override fun add(item: E) {
    mutateState(
      { state -> !state.contains(item) },
      { state -> ImmutableSet.copyOf(SetAlgorithms.unionOf(state, ImmutableSet.of(item))) }
    ) { _, _ -> ImmutableSet.of(AddToSet(item)) }
  }

  override fun addAll(items: Collection<out E>) {
    mutateState(
      { state -> items.stream().anyMatch { e: E -> !state.contains(e) } },
      { state -> state.toBuilder().addAll(items).build() }
    ) { previousState, currentState ->
      SetAlgorithms.differenceBetween(previousState, currentState)
        .stream()
        .map { item: E -> AddToSet(item) }
        .collect(Collectors.toImmutableSet())
    }
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return mutateState(
      { state -> state.containsUnknownTyped(item) },
      { state ->
        ImmutableSet.copyOf(
          SetAlgorithms.differenceBetween(
            state,
            ImmutableSet.of(item!!)
          )
        )
      }
    ) { _, _ ->
      @Suppress("UNCHECKED_CAST")
      ImmutableSet.of(RemoveFromSet(item as E))
    }
  }

  override fun clear() {
    mutateState({ obj: ImmutableSet<E> -> obj.isPopulated },
      { ImmutableSet.empty() }
    ) { previousState, _ ->
      previousState.stream()
        .map { item: E -> RemoveFromSet(item) }
        .collect(Collectors.toSet())
    }
  }

  private fun mutateState(
    shouldMutate: Predicate<ImmutableSet<E>>,
    mutator: Function<ImmutableSet<E>, ImmutableSet<E>>,
    mutationsGenerator: BiFunction<ImmutableSet<E>, ImmutableSet<E>, Set<SetOperation<E>>>
  ): Boolean {
    synchronized(this) {
      val previousState = currentState
      if (!shouldMutate.test(previousState)) {
        return false
      }
      val newState = mutator.apply(previousState)
      currentState = newState
      mutationEvents.onNext(
        MutationEvent(newState, mutationsGenerator.apply(previousState, newState))
      )
      return true
    }
  }

  override fun iterator(): Iterator<E> {
    return state.iterator()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return state.containsUnknownTyped(item)
  }

  override fun count(): Int {
    return state.count()
  }

  override val isPopulated: Boolean
    get() = state.isPopulated

  override fun toReadOnly(): ObservableSet<E> {
    return object : ObservableSet<E> {
      override fun observe(): ObservableSet.ObservableChannels<E> {
        return this@WritableObservableSetImpl.observe()
      }

      override fun iterator(): Iterator<E> {
        return this@WritableObservableSetImpl.iterator()
      }

      override fun containsUnknownTyped(item: Any?): Boolean {
        return this@WritableObservableSetImpl.containsUnknownTyped(item)
      }

      override fun count(): Int {
        return this@WritableObservableSetImpl.count()
      }

      override fun stream(): Stream<E> {
        return this@WritableObservableSetImpl.stream()
      }
    }
  }

  override fun stream(): Stream<E> {
    return state.stream()
  }

  override fun observe(): ObservableChannels {
    return observableChannels
  }

  private val state: ImmutableSet<E>
    get() {
      synchronized(this) { return currentState }
    }

  private class AddToSet<E : Any>(private val item: E) : ObservableSet.AddToSet<E> {

    override fun item(): E {
      return item
    }
  }

  private class RemoveFromSet<E : Any>(private val item: E) : ObservableSet.RemoveFromSet<E> {

    override fun item(): E {
      return item
    }
  }

  inner class ObservableChannels : GenericObservableChannels<Set<E>, MutationEvent>(
    Observable.create { flowableEmitter: ObservableEmitter<Set<E>> ->
      flowableEmitter.onNext(state)
      flowableEmitter.onComplete()
    }
      .concatWith(mutationEvents.map { obj -> obj.state() }),
    Observable.create { flowableEmitter: ObservableEmitter<MutationEvent> ->
      flowableEmitter.onNext(generateMutationEventForNewSubscription())
      flowableEmitter.onComplete()
    }
      .concatWith(mutationEvents)), ObservableSet.ObservableChannels<E>

  inner class MutationEvent(state: Set<E>, operations: Set<SetOperation<E>>) :
    GenericMutationEvent<Set<E>, Set<SetOperation<E>>>(state, operations),
    ObservableSet.MutationEvent<E>

  private fun generateMutationEventForNewSubscription(): MutationEvent {
    val state: Set<E> = state
    return MutationEvent(
      state,
      state.stream().map { item: E -> AddToSet(item) }.collect(Collectors.toSet())
    )
  }
}