package omnia.data.structure.observable.writable

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.subjects.PublishSubject
import omnia.data.stream.Collectors
import omnia.data.stream.Streams
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableDirectedGraph
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.observable.ObservableDirectedGraph
import omnia.data.structure.observable.ObservableGraph
import omnia.data.structure.observable.ObservableGraph.GraphOperation
import omnia.data.structure.tuple.Couplet
import java.util.*
import java.util.function.*
import java.util.function.Function

internal class WritableObservableDirectedGraphImpl<E> : WritableObservableDirectedGraph<E> {
    @Volatile
    private var state: ImmutableDirectedGraph<E>
    private val mutationEvents = PublishSubject.create<MutationEvent<E>>().toSerialized()

    constructor() {
        state = ImmutableDirectedGraph.empty()
    }

    constructor(original: DirectedGraph<E>) {
        state = ImmutableDirectedGraph.copyOf(original)
    }

    override fun addNode(item: E) {
        mutateState(
                { currentState -> !currentState.contents().contains(item) },
                { currentState -> currentState.toBuilder().addNode(item).build() }
        ) { _, _ -> ImmutableSet.of(AddNodeToGraph.create(item)) }
    }

    override fun replaceNode(original: E, replacement: E) {
        mutateState(
                { currentState: ImmutableDirectedGraph<E> ->
                    require(currentState.contents().contains(original)) { "cannot replace a non-existent node. original=$original" }
                    require(!currentState.contents().contains(replacement)) { "cannot replace a node with an already existing node. replacement=$replacement" }
                    true
                },
                { currentState -> currentState.toBuilder().replaceNode(original, replacement).build() }
        ) { previousState, newState ->
            Streams.concat(
                previousState.nodeOf(original)
                    .stream()
                    .map { obj -> obj.edges() }
                    .flatMap { obj -> obj.stream() }
                    .map { obj -> obj.endpoints() }
                    .map { couplet -> couplet.map( Function{ obj -> obj.item() }) }
                    .map { endpoints -> RemoveEdgeFromGraph.create(endpoints) },
                previousState.nodeOf(original)
                    .stream()
                    .map { obj -> obj.item() }
                    .map { item -> RemoveNodeFromGraph.create(item) },
                newState.nodeOf(replacement)
                    .stream()
                    .map { obj -> obj.item() }
                    .map { item -> AddNodeToGraph.create(item) },
                newState.nodeOf(replacement)
                    .stream()
                    .map { obj -> obj.edges() }
                    .flatMap { obj -> obj.stream() }
                    .map { obj -> obj.endpoints() }
                    .map { couplet -> couplet.map( Function{ obj -> obj.item() } )}
                    .map { endpoints -> AddEdgeToGraph.create(endpoints) })
                .collect(Collectors.toSet())
        }
    }

    override fun removeUnknownTypedNode(item: Any?): Boolean {
        return mutateState(
                { currentState -> currentState.contents().containsUnknownTyped(item) },
                { currentState -> currentState.toBuilder().removeUnknownTypedNode(item).build() }
        ) { previousState, _ ->
            Streams.concat(
                    previousState.nodeOfUnknownType(item).stream()
                            .map { obj -> obj.edges() }
                            .flatMap { obj -> obj.stream() }
                            .map { obj -> obj.endpoints() }
                            .map { couplet -> couplet.map( Function{ obj -> obj.item() } )}
                            .map { endpoints -> RemoveEdgeFromGraph.create(endpoints) },
                    previousState.nodeOfUnknownType(item).stream()
                            .map { obj -> obj.item() }
                            .map { item: E -> RemoveNodeFromGraph.create(item) })
                .collect(Collectors.toSet())
        }
    }

    override fun addEdge(from: E, to: E) {
        mutateState(
                { currentState -> currentState.edgeOf(from, to).isEmpty },
                { currentState -> currentState.toBuilder().addEdge(from, to).build() }
        ) { _, newState ->
            newState.edgeOf(from, to).stream()
                    .map { obj -> obj.endpoints() }
                    .map { couplet -> couplet.map( Function{ obj -> obj.item() } )}
                    .map { endpoints -> AddEdgeToGraph.create(endpoints) }
                    .collect(Collectors.toSet())
        }
    }

    override fun removeEdge(from: E, to: E): Boolean {
        return removeEdgeUnknownTyped(from, to)
    }

    override fun removeEdgeUnknownTyped(from: Any?, to: Any?): Boolean {
        return mutateState(
                { currentState -> currentState.edgeOfUnknownType(from, to).isPresent },
                { currentState -> currentState.toBuilder().removeEdgeUnknownEdge(from, to).build() }
        ) { previousState, _ ->
            previousState.edgeOfUnknownType(from, to).stream()
                .map { obj -> obj.endpoints() }
                .map { couplet -> couplet.map( Function{ obj -> obj.item() } )}
                .map { endpoints -> RemoveEdgeFromGraph.create(endpoints) }
                .collect(Collectors.toSet())
        }
    }

    private fun mutateState(
            shouldChange: Predicate<in ImmutableDirectedGraph<E>>,
            mutateState: Function<in ImmutableDirectedGraph<E>, out ImmutableDirectedGraph<E>>,
            mutationOperations: BiFunction<in ImmutableDirectedGraph<E>, in ImmutableDirectedGraph<E>, out Set<GraphOperation<E>>>): Boolean {
        var previousState: ImmutableDirectedGraph<E>
        var nextState: ImmutableDirectedGraph<E>
        synchronized(this) {
            previousState = state
            if (!shouldChange.test(previousState)) {
                return false
            }
            nextState = Objects.requireNonNull(mutateState.apply(previousState))
            state = nextState
            val operations = mutationOperations.apply(previousState, nextState)
            mutationEvents.onNext(
                    object : MutationEvent<E> {
                        override fun state(): DirectedGraph<E> {
                            return nextState
                        }

                        override fun operations(): Set<GraphOperation<E>> {
                            return operations
                        }
                    })
        }
        return true
    }

    override fun nodeOf(item: E): Optional<out DirectedGraph.DirectedNode<E>> {
        return nodeOfUnknownType(item)
    }

    override fun nodeOfUnknownType(item: Any?): Optional<out DirectedGraph.DirectedNode<E>> {
        return getState().nodeOfUnknownType(item)
    }

    override fun edgeOf(from: E, to: E): Optional<out DirectedGraph.DirectedEdge<E>> {
        return edgeOfUnknownType(from, to)
    }

    override fun edgeOfUnknownType(from: Any?, to: Any?): Optional<out DirectedGraph.DirectedEdge<E>> {
        return getState().edgeOfUnknownType(from, to)
    }

    override fun contents(): Set<E> {
        return getState().contents()
    }

    override fun nodes(): Set<out DirectedGraph.DirectedNode<E>> {
        return getState().nodes()
    }

    override fun edges(): Set<out DirectedGraph.DirectedEdge<E>> {
        return getState().edges()
    }

    override fun toReadOnly(): ObservableDirectedGraph<E> {
        return object : ObservableDirectedGraph<E> {
            override fun observe(): ObservableDirectedGraph.ObservableChannels<E> {
                return this@WritableObservableDirectedGraphImpl.observe()
            }

            override fun nodeOf(item: E): Optional<out DirectedGraph.DirectedNode<E>> {
                return nodeOfUnknownType(item)
            }

            override fun nodeOfUnknownType(item: Any?): Optional<out DirectedGraph.DirectedNode<E>> {
                return this@WritableObservableDirectedGraphImpl.nodeOfUnknownType(item)
            }

            override fun edgeOf(from: E, to: E): Optional<out DirectedGraph.DirectedEdge<E>> {
                return edgeOfUnknownType(from, to)
            }

            override fun edgeOfUnknownType(from: Any?, to: Any?): Optional<out DirectedGraph.DirectedEdge<E>> {
                return this@WritableObservableDirectedGraphImpl.edgeOfUnknownType(from, to)
            }

            override fun nodes(): Set<out DirectedGraph.DirectedNode<E>> {
                return this@WritableObservableDirectedGraphImpl.nodes()
            }

            override fun edges(): Set<out DirectedGraph.DirectedEdge<E>> {
                return this@WritableObservableDirectedGraphImpl.edges()
            }

            override fun contents(): Set<E> {
                return this@WritableObservableDirectedGraphImpl.contents()
            }
        }
    }

    override fun observe(): ObservableDirectedGraph.ObservableChannels<E> {
        return object : ObservableDirectedGraph.ObservableChannels<E> {
            override fun states(): Observable<DirectedGraph<E>> {
                return Observable.create { emitter: ObservableEmitter<DirectedGraph<E>> ->
                    synchronized(this) {
                        emitter.onNext(getState())
                        emitter.setDisposable(
                                mutationEvents.map { obj: MutationEvent<E> -> obj.state() }
                                        .subscribe({ value: DirectedGraph<E> -> emitter.onNext(value) }, { error: Throwable -> emitter.onError(error) }) { emitter.onComplete() })
                    }
                }
            }

            override fun mutations(): Observable<MutationEvent<E>> {
                return Observable.create { emitter: ObservableEmitter<MutationEvent<E>> ->
                    synchronized(this) {
                        val state = getState()
                        val operations: Set<GraphOperation<E>> =
                            Streams.concat(
                                state.nodes().stream()
                                    .map { obj -> obj.item() }
                                    .map { item -> AddNodeToGraph.create(item) },
                                state.edges().stream()
                                    .map { obj -> obj.endpoints() }
                                    .map { couplet -> couplet.map( Function{ obj -> obj.item() }) }
                                    .map { endpoints -> AddEdgeToGraph.create(endpoints) })
                                .collect(Collectors.toImmutableSet())
                        emitter.onNext(object : MutationEvent<E> {
                            override fun state(): DirectedGraph<E> {
                                return state
                            }

                            override fun operations(): Set<GraphOperation<E>> {
                                return operations
                            }
                        })
                        emitter.setDisposable(
                                mutationEvents
                                        .subscribe({ value: MutationEvent<E> -> emitter.onNext(value) }, { error: Throwable -> emitter.onError(error) }) { emitter.onComplete() })
                    }
                }
            }
        }
    }

    private fun getState(): ImmutableDirectedGraph<E> {
        synchronized(this) { return state }
    }

    internal interface MutationEvent<E> : ObservableDirectedGraph.MutationEvent<E> {
        override fun state(): DirectedGraph<E>
        override fun operations(): Set<GraphOperation<E>>
    }

    private abstract class NodeOperation<E>(private val item: E) : ObservableGraph.NodeOperation<E> {
        override fun item(): E {
            return item
        }

    }

    private class AddNodeToGraph<E> private constructor(item: E) : NodeOperation<E>(item), ObservableGraph.AddNodeToGraph<E> {
        companion object {
            fun <E> create(item: E): AddNodeToGraph<E> {
                return AddNodeToGraph(item)
            }
        }
    }

    private class RemoveNodeFromGraph<E> private constructor(item: E) : NodeOperation<E>(item), ObservableGraph.RemoveNodeFromGraph<E> {
        companion object {
            fun <E> create(item: E): RemoveNodeFromGraph<E> {
                return RemoveNodeFromGraph(item)
            }
        }
    }

    private abstract class EdgeOperation<E>(private val endpoints: Couplet<E>) : ObservableGraph.EdgeOperation<E> {
        override fun endpoints(): Couplet<E> {
            return endpoints
        }

    }

    private class AddEdgeToGraph<E> private constructor(endpoints: Couplet<E>) : EdgeOperation<E>(endpoints), ObservableGraph.AddEdgeToGraph<E> {
        companion object {
            fun <E> create(endpoints: Couplet<E>): AddEdgeToGraph<E> {
                return AddEdgeToGraph(endpoints)
            }
        }
    }

    private class RemoveEdgeFromGraph<E> private constructor(endpoints: Couplet<E>) : EdgeOperation<E>(endpoints), ObservableGraph.RemoveEdgeFromGraph<E> {
        companion object {
            fun <E> create(endpoints: Couplet<E>): RemoveEdgeFromGraph<E> {
                return RemoveEdgeFromGraph(endpoints)
            }
        }
    }
}