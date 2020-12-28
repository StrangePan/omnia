package omnia.data.structure.observable.writable

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import omnia.data.structure.Collection
import omnia.data.structure.IntRange
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.observable.ObservableList
import omnia.data.structure.observable.ObservableList.ListOperation
import java.util.OptionalInt
import java.util.function.BiFunction
import java.util.function.Predicate
import java.util.stream.Stream

internal class WritableObservableListImpl<E> : WritableObservableList<E> {
    @Volatile
    private var currentState: ImmutableList<E> = ImmutableList.empty()
    private val mutationEvents: Subject<MutationEvent> = PublishSubject.create<MutationEvent>().toSerialized()
    private val observableChannels: ObservableChannels = ObservableChannels()
    
    override fun insertAt(index: Int, item: E) {
        val range: IntRange = IntRange.just(index)
        mutateState(
                { true },
                { state -> state.toBuilder().insertAt(index, item).build() }
        ) { _, currentState -> generateInsertAtMutations(currentState, range) }
    }

    override fun removeAt(index: Int): E {
        val range: IntRange = IntRange.just(index)
        var value: E
        synchronized(this) {
            value = state.itemAt(index)
            mutateState(
                    { true },
                    { state -> state.toBuilder().removeAt(index).build() }
            ) { previousState: ImmutableList<E>, _: ImmutableList<E> -> generateRemoveAtMutations(previousState, range) }
        }
        return value
    }

    override fun replaceAt(index: Int, item: E): E {
        val range: IntRange = IntRange.just(index)
        var value: E
        synchronized(this) {
            value = state.itemAt(index)
            mutateState(
                    { true },
                    { state -> state.toBuilder().replaceAt(index, item).build() }
            ) { previousState, currentState -> generateReplaceAtMutations(previousState, currentState, range) }
        }
        return value
    }

    override fun add(item: E) {
        mutateState(
                { true },
                { state -> state.toBuilder().add(item).build() }
        ) { _, currentState -> generateInsertAtMutations(currentState, IntRange.just(currentState.count() - 1)) }
    }

    override fun addAll(items: Collection<out E>) {
        mutateState(
                { true },
                { state -> state.toBuilder().addAll(items).build() }
        ) { previousState, currentState ->
            generateInsertAtMutations(
                    currentState,
                    IntRange.startingAt(previousState.count()).endingAt(currentState.count()))
        }
    }

    override fun removeUnknownTyped(item: Any?): Boolean {
        synchronized(this) {
            val index = state.indexOf(item)
            return mutateState(
                    { index.isPresent },
                    { state -> state.toBuilder().removeAt(index.orElseThrow()).build() }
            ) { previousState, _ ->
                generateRemoveAtMutations(
                        previousState, IntRange.just(index.orElseThrow()))
            }
        }
    }

    override fun clear() {
        synchronized(this) {
            mutateState({ obj: ImmutableList<E> -> obj.isPopulated },
                    { ImmutableList.empty() }
            ) { previousState, _ ->
                generateRemoveAtMutations(
                        previousState, IntRange.startingAt(0).endingAt(previousState.count()))
            }
        }
    }

    private fun mutateState(
        shouldMutate: Predicate<ImmutableList<E>>,
        mutator: java.util.function.Function<ImmutableList<E>, ImmutableList<E>>,
        mutationsGenerator: BiFunction<ImmutableList<E>, ImmutableList<E>, List<ListOperation<E>>>
    ): Boolean {
        synchronized(this) {
            val previousState = currentState
            if (!shouldMutate.test(previousState)) {
                return false
            }
            val newState = mutator.apply(previousState)
            currentState = newState
            mutationEvents.onNext(
                    MutationEvent(newState, mutationsGenerator.apply(previousState, newState)))
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

    override fun itemAt(index: Int): E {
        return state.itemAt(index)
    }

    override fun indexOf(item: Any?): OptionalInt {
        return state.indexOf(item)
    }

    override fun stream(): Stream<E> {
        return state.stream()
    }

    override fun toReadOnly(): ObservableList<E> {
        return object : ObservableList<E> {
            override fun observe(): ObservableList.ObservableChannels<E> {
                return this@WritableObservableListImpl.observe()
            }

            override fun iterator(): Iterator<E> {
                return this@WritableObservableListImpl.iterator()
            }

            override fun containsUnknownTyped(item: Any?): Boolean {
                return this@WritableObservableListImpl.containsUnknownTyped(item)
            }

            override fun count(): Int {
                return this@WritableObservableListImpl.count()
            }

            override fun itemAt(index: Int): E {
                return this@WritableObservableListImpl.itemAt(index)
            }

            override fun indexOf(item: Any?): OptionalInt {
                return this@WritableObservableListImpl.indexOf(item)
            }

            override fun stream(): Stream<E> {
                return this@WritableObservableListImpl.stream()
            }
        }
    }

    override fun observe(): ObservableChannels {
        return observableChannels
    }

    private val state: ImmutableList<E>
        get() {
            synchronized(this) { return currentState }
        }

    private class AddToList<E>(private val items: List<E>, private val indices: IntRange) : ObservableList.AddToList<E> {
        override fun items(): List<E> {
            return items
        }

        override fun indices(): IntRange {
            return indices
        }
    }

    private class MoveInList<E>(private val items: List<E>, private val previousIndices: IntRange, private val currentIndices: IntRange) : ObservableList.MoveInList<E> {
        override fun items(): List<E> {
            return items
        }

        override fun previousIndices(): IntRange {
            return previousIndices
        }

        override fun currentIndices(): IntRange {
            return currentIndices
        }
    }

    private class RemoveFromList<E>(private val items: List<E>, private val indices: IntRange) : ObservableList.RemoveFromList<E> {
        override fun items(): List<E> {
            return items
        }

        override fun indices(): IntRange {
            return indices
        }
    }

    private class ReplaceInList<E>(private val replacedItems: List<E>, private val newItems: List<E>, private val indices: IntRange) : ObservableList.ReplaceInList<E> {
        override fun replacedItems(): List<E> {
            return replacedItems
        }

        override fun newItems(): List<E> {
            return newItems
        }

        override fun indices(): IntRange {
            return indices
        }
    }

    inner class ObservableChannels : GenericObservableChannels<List<E>, MutationEvent>(
            Observable.create { flowableEmitter: ObservableEmitter<List<E>> ->
                flowableEmitter.onNext(state)
                flowableEmitter.onComplete()
            }
                    .concatWith(mutationEvents.map { obj: MutationEvent -> obj.state() }),
            Observable.create {
                flowableEmitter: ObservableEmitter<MutationEvent> ->
                flowableEmitter.onNext(generateMutationEventForNewSubscription())
                flowableEmitter.onComplete()
            }
                .concatWith(mutationEvents)), ObservableList.ObservableChannels<E>

    inner class MutationEvent(state: List<E>, operations: List<ListOperation<E>>) : GenericMutationEvent<List<E>, List<ListOperation<E>>>(state, operations), ObservableList.MutationEvent<E>

    private fun generateMutationEventForNewSubscription(): MutationEvent {
        val state: List<E> = state
        return MutationEvent(
                state, ImmutableList.of(AddToList(state, IntRange.startingAt(0).endingAt(state.count()))))
    }

    companion object {
        private fun <E> generateInsertAtMutations(
                state: ImmutableList<E>, range: IntRange): ImmutableList<ListOperation<E>> {
            val builder: ImmutableList.Builder<ListOperation<E>> = ImmutableList.builder()

            // Move must be done before insert to make way for new items
            if (range.end() < state.count()) {
                val numItemsMoved = state.count() - range.end()
                val moveFromRange: IntRange = IntRange.startingAt(range.start()).withLength(numItemsMoved)
                val moveToRange: IntRange = IntRange.startingAt(range.end()).withLength(numItemsMoved)
                builder.add(MoveInList(state.getSublist(moveToRange), moveFromRange, moveToRange))
            }
            builder.add(AddToList(state.getSublist(range), range))
            return builder.build()
        }

        private fun <E> generateRemoveAtMutations(
                previousState: ImmutableList<E>, range: IntRange): ImmutableList<ListOperation<E>> {
            val builder: ImmutableList.Builder<ListOperation<E>> = ImmutableList.builder()

            /// Removal must be done BEFORE move to make space for moved items
            builder.add(RemoveFromList(previousState.getSublist(range), range))
            if (range.end() < previousState.count()) {
                val numItemsMoved = previousState.count() - range.end()
                val moveFromRange: IntRange = IntRange.startingAt(range.end()).withLength(numItemsMoved)
                val moveToRange: IntRange = IntRange.startingAt(range.start()).withLength(numItemsMoved)
                builder.add(
                        MoveInList(previousState.getSublist(moveFromRange), moveFromRange, moveToRange))
            }
            return builder.build()
        }

        private fun <E> generateReplaceAtMutations(
                previousState: ImmutableList<E>, currentState: ImmutableList<E>, range: IntRange): ImmutableList<ListOperation<E>> {
            return ImmutableList.of(
                    ReplaceInList(
                            previousState.getSublist(range), currentState.getSublist(range), range))
        }
    }
}