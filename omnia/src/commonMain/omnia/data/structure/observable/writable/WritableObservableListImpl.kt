package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.concatWith
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.subject.publish.PublishSubject
import kotlin.concurrent.Volatile
import omnia.data.structure.IntRange
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.observable.ObservableList
import omnia.data.structure.observable.ObservableList.ListOperation

internal class WritableObservableListImpl<E : Any> : WritableObservableList<E> {

  @Volatile
  private var currentState = ImmutableList.empty<E>()
  private val mutationEvents = PublishSubject<MutationEvent>()

  override fun insertAt(index: Int, item: E) {
    val range: IntRange = IntRange.just(index)
    mutateState(
      { true },
      { state -> state.toBuilder().insertAt(index, item).build() }
    ) { _, currentState -> generateInsertAtMutations(currentState, range) }
  }

  override fun removeAt(index: Int): E {
    val range: IntRange = IntRange.just(index)
    val value: E = state.itemAt(index)
    mutateState(
      { true },
      { state -> state.toBuilder().removeAt(index).build() }
    ) { previousState: ImmutableList<E>, _: ImmutableList<E> ->
      generateRemoveAtMutations(
        previousState,
        range
      )
    }
    return value
  }

  override fun replaceAt(index: Int, item: E): E {
    val range: IntRange = IntRange.just(index)
    val value: E = state.itemAt(index)
    mutateState(
      { true },
      { state -> state.toBuilder().replaceAt(index, item).build() }
    ) { previousState, currentState ->
      generateReplaceAtMutations(
        previousState,
        currentState,
        range
      )
    }
    return value
  }

  override fun add(item: E) {
    mutateState(
      { true },
      { state -> state.toBuilder().add(item).build() }
    ) { _, currentState ->
      generateInsertAtMutations(
        currentState,
        IntRange.just(currentState.count - 1)
      )
    }
  }

  override fun addAll(items: Iterable<E>) {
    mutateState(
      { true },
      { state -> state.toBuilder().addAll(items).build() }
    ) { previousState, currentState ->
      generateInsertAtMutations(
        currentState,
        IntRange.startingAt(previousState.count).endingAt(currentState.count)
      )
    }
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    val index = state.indexOf(item)
    return mutateState(
      { index != null },
      { state -> state.toBuilder().removeAt(index!!).build() }
    ) { previousState, _ ->
      generateRemoveAtMutations(
        previousState, IntRange.just(index!!)
      )
    }
  }

  override fun clear() {
    mutateState({ obj: ImmutableList<E> -> obj.isPopulated },
      { ImmutableList.empty() }
    ) { previousState, _ ->
      generateRemoveAtMutations(
        previousState, IntRange.startingAt(0).endingAt(previousState.count)
      )
    }
  }

  private fun mutateState(
    shouldMutate: (ImmutableList<E>) -> Boolean,
    mutator: (ImmutableList<E>) -> ImmutableList<E>,
    mutationsGenerator: (ImmutableList<E>, ImmutableList<E>) -> List<ListOperation<E>>
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

  override val count: Int
    get() {
      return state.count
    }

  override val isPopulated: Boolean
    get() = state.isPopulated

  override fun itemAt(index: Int): E {
    return state.itemAt(index)
  }

  override fun indexOf(item: Any?): Int? {
    return state.indexOf(item)
  }

  override fun toReadOnly(): ObservableList<E> {
    return object : ObservableList<E> {
      override val observables: ObservableList.Observables<E>
        get() {
          return this@WritableObservableListImpl.observables
        }

      override fun iterator(): Iterator<E> {
        return this@WritableObservableListImpl.iterator()
      }

      override fun containsUnknownTyped(item: Any?): Boolean {
        return this@WritableObservableListImpl.containsUnknownTyped(item)
      }

      override val count: Int
        get() {
          return this@WritableObservableListImpl.count
        }

      override fun itemAt(index: Int): E {
        return this@WritableObservableListImpl.itemAt(index)
      }

      override fun indexOf(item: Any?): Int? {
        return this@WritableObservableListImpl.indexOf(item)
      }
    }
  }

  override val observables = Observables()

  private val state: ImmutableList<E> get() = currentState

  private class AddToList<E : Any>(override val items: List<E>, override val indices: IntRange) :
    ObservableList.AddToList<E>

  private class MoveInList<E : Any>(
    override val items: List<E>,
    override val previousIndices: IntRange,
    override val currentIndices: IntRange
  ) : ObservableList.MoveInList<E>

  private class RemoveFromList<E : Any>(
    override val items: List<E>,
    override val indices: IntRange
  ) : ObservableList.RemoveFromList<E>

  private class ReplaceInList<E : Any>(
    override val replacedItems: List<E>,
    override val newItems: List<E>,
    override val indices: IntRange
  ) : ObservableList.ReplaceInList<E>

  inner class Observables : GenericObservables<List<E>, MutationEvent>(
    observable<List<E>> {
      it.onNext(state)
      it.onComplete()
    }
      .concatWith(mutationEvents.map { it.state }),
    observable<MutationEvent> {
      it.onNext(generateMutationEventForNewSubscription())
      it.onComplete()
    }
      .concatWith(mutationEvents)),
    ObservableList.Observables<E>

  inner class MutationEvent(state: List<E>, operations: List<ListOperation<E>>) :
    GenericMutationEvent<List<E>, List<ListOperation<E>>>(state, operations),
    ObservableList.MutationEvent<E>

  private fun generateMutationEventForNewSubscription(): MutationEvent {
    val state: List<E> = state
    return MutationEvent(
      state, ImmutableList.of(AddToList(state, IntRange.startingAt(0).endingAt(state.count)))
    )
  }

  companion object {

    private fun <E : Any> generateInsertAtMutations(
      state: ImmutableList<E>, range: IntRange
    ): ImmutableList<ListOperation<E>> {
      val builder: ImmutableList.Builder<ListOperation<E>> = ImmutableList.builder()

      // Move must be done before insert to make way for new items
      if (range.end < state.count) {
        val numItemsMoved = state.count - range.end
        val moveFromRange: IntRange = IntRange.startingAt(range.start).withLength(numItemsMoved)
        val moveToRange: IntRange = IntRange.startingAt(range.end).withLength(numItemsMoved)
        builder.add(MoveInList(state.getSublist(moveToRange), moveFromRange, moveToRange))
      }
      builder.add(AddToList(state.getSublist(range), range))
      return builder.build()
    }

    private fun <E : Any> generateRemoveAtMutations(
      previousState: ImmutableList<E>, range: IntRange
    ): ImmutableList<ListOperation<E>> {
      val builder: ImmutableList.Builder<ListOperation<E>> = ImmutableList.builder()

      /// Removal must be done BEFORE move to make space for moved items
      builder.add(RemoveFromList(previousState.getSublist(range), range))
      if (range.end < previousState.count) {
        val numItemsMoved = previousState.count - range.end
        val moveFromRange: IntRange = IntRange.startingAt(range.end).withLength(numItemsMoved)
        val moveToRange: IntRange = IntRange.startingAt(range.start).withLength(numItemsMoved)
        builder.add(
          MoveInList(previousState.getSublist(moveFromRange), moveFromRange, moveToRange)
        )
      }
      return builder.build()
    }

    private fun <E : Any> generateReplaceAtMutations(
      previousState: ImmutableList<E>, currentState: ImmutableList<E>, range: IntRange
    ): ImmutableList<ListOperation<E>> {
      return ImmutableList.of(
        ReplaceInList(
          previousState.getSublist(range), currentState.getSublist(range), range
        )
      )
    }
  }
}
