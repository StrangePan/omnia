package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.concatWith
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.publish
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import omnia.algorithm.SetAlgorithms
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.observable.ObservableSet
import omnia.data.structure.observable.ObservableSet.SetOperation

internal class WritableObservableSetImpl<E : Any> : WritableObservableSet<E> {

  val subject = BehaviorSubject(MutationEvent(ImmutableSet.empty(), ImmutableSet.empty()))

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
    mutationsGenerator: (ImmutableSet<E>, ImmutableSet<E>) -> ImmutableSet<SetOperation<E>>
  ): Boolean {
    val previousState = subject.value.state
    if (!shouldMutate(previousState)) {
      return false
    }
    val newState = mutator(previousState)
    val event = MutationEvent(newState, mutationsGenerator(previousState, newState))
    subject.onNext(event)
    return true
  }

  private val state get() = subject.value.state

  override fun iterator(): Iterator<E> {
    return state.iterator()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return state.containsUnknownTyped(item)
  }

  override val count: Int
    get() {
      return state.count
    }

  override val isPopulated: Boolean
    get() = state.isPopulated

  override fun toReadOnly(): ObservableSet<E> {
    return object : ObservableSet<E> {
      override val observables: ObservableSet.Observables<E>
        get() {
          return this@WritableObservableSetImpl.observables
        }

      override fun iterator(): Iterator<E> {
        return this@WritableObservableSetImpl.iterator()
      }

      override fun containsUnknownTyped(item: Any?): Boolean {
        return this@WritableObservableSetImpl.containsUnknownTyped(item)
      }

      override val count: Int
        get() {
          return this@WritableObservableSetImpl.count
        }
    }
  }

  private class AddToSet<E : Any>(override val item: E) : ObservableSet.AddToSet<E>

  private class RemoveFromSet<E : Any>(override val item: E) : ObservableSet.RemoveFromSet<E>

  override val observables = object : ObservableSet.Observables<E> {

    override val states = subject.map { it.state }

    override val mutations =
      subject.publish { it.take(1).map { createEventForNewSubscription(it.state) }.concatWith(it) }
  }

  inner class MutationEvent(state: ImmutableSet<E>, operations: ImmutableSet<SetOperation<E>>) :
    GenericMutationEvent<ImmutableSet<E>, ImmutableSet<SetOperation<E>>>(state, operations),
    ObservableSet.MutationEvent<E>

  private fun createEventForNewSubscription(set: ImmutableSet<E>): MutationEvent {
    return MutationEvent(set, set.map { AddToSet(it) }.toImmutableSet())
  }
}