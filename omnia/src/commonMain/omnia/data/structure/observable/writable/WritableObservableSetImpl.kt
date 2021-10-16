package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.concatWith
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.subject.publish.PublishSubject
import kotlin.jvm.Volatile
import omnia.algorithm.SetAlgorithms
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.observable.ObservableSet
import omnia.data.structure.observable.ObservableSet.SetOperation

internal class WritableObservableSetImpl<E : Any> : WritableObservableSet<E> {

  @Volatile
  private var currentState = ImmutableSet.empty<E>()
  private val mutationEvents = PublishSubject<MutationEvent>()
  private val observableChannels = ObservableChannels()

  override fun add(item: E) {
    mutateState(
      { state -> !state.contains(item) },
      { state -> ImmutableSet.copyOf(SetAlgorithms.unionOf(state, ImmutableSet.of(item))) }
    ) { _, _ -> ImmutableSet.of(AddToSet(item)) }
  }

  override fun addAll(items: Iterable<E>) {
    mutateState(
      { state -> items.any { !state.contains(it) } },
      { state -> state.toBuilder().addAll(items).build() }
    ) { previousState, currentState ->
      SetAlgorithms.differenceBetween(previousState, currentState)
        .map { AddToSet(it) }
        .toImmutableSet()
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
      previousState.map { RemoveFromSet(it) }.toImmutableSet()
    }
  }

  private fun mutateState(
    shouldMutate: (ImmutableSet<E>) -> Boolean,
    mutator: (ImmutableSet<E>) -> ImmutableSet<E>,
    mutationsGenerator: (ImmutableSet<E>, ImmutableSet<E>) -> Set<SetOperation<E>>
  ): Boolean {
    val previousState = currentState
    if (!shouldMutate(previousState)) {
      return false
    }
    val newState = mutator(previousState)
    currentState = newState
    mutationEvents.onNext(
      MutationEvent(newState, mutationsGenerator(previousState, newState))
    )
    return true
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
    }
  }

  override fun observe(): ObservableChannels {
    return observableChannels
  }

  private val state: ImmutableSet<E>
    get() = currentState

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
    observable<Set<E>> {
      it.onNext(state)
      it.onComplete()
    }
      .concatWith(mutationEvents.map { it.state() }),
    observable<MutationEvent> {
      it.onNext(generateMutationEventForNewSubscription())
      it.onComplete()
    }
      .concatWith(mutationEvents)),
    ObservableSet.ObservableChannels<E>

  inner class MutationEvent(state: Set<E>, operations: Set<SetOperation<E>>) :
    GenericMutationEvent<Set<E>, Set<SetOperation<E>>>(state, operations),
    ObservableSet.MutationEvent<E>

  private fun generateMutationEventForNewSubscription(): MutationEvent {
    val state: Set<E> = state
    return MutationEvent(state, state.map { AddToSet(it) }.toImmutableSet())
  }
}