package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableEmitter
import com.badoo.reaktive.observable.doOnBeforeComplete
import com.badoo.reaktive.observable.doOnBeforeError
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.publish.PublishSubject
import kotlin.jvm.Volatile
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableDirectedGraph
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.observable.ObservableDirectedGraph
import omnia.data.structure.observable.ObservableGraph
import omnia.data.structure.observable.ObservableGraph.GraphOperation
import omnia.data.structure.tuple.Couplet

internal class WritableObservableDirectedGraphImpl<E : Any> : WritableObservableDirectedGraph<E> {

  @Volatile
  private var state: ImmutableDirectedGraph<E>

  private val mutationEvents = PublishSubject<MutationEvent<E>>()

  constructor() {
    state = ImmutableDirectedGraph.empty()
  }

  constructor(original: DirectedGraph<E>) {
    state = ImmutableDirectedGraph.copyOf(original)
  }

  override fun addNode(item: E) {
    mutateState(
      { currentState -> !currentState.contents.contains(item) },
      { currentState -> currentState.toBuilder().addNode(item).build() }
    ) { _, _ -> ImmutableSet.of(AddNodeToGraph.create(item)) }
  }

  override fun replaceNode(original: E, replacement: E) {
    mutateState(
      { currentState: ImmutableDirectedGraph<E> ->
        require(
          currentState.contents.contains(original)
        ) { "cannot replace a non-existent node. original=$original" }
        require(
          !currentState.contents.contains(replacement)
        ) { "cannot replace a node with an already existing node. replacement=$replacement" }
        true
      },
      { currentState -> currentState.toBuilder().replaceNode(original, replacement).build() }
    ) { previousState, newState ->
      listOfNotNull(
          previousState.nodeOf(original)
            ?.edges
            ?.map { it.endpoints }
            ?.map { couplet -> couplet.map { it.item } }
            ?.map { RemoveEdgeFromGraph.create(it) },
          previousState.nodeOf(original)
            ?.item
            ?.let { RemoveNodeFromGraph.create(it) }
            ?.let { listOf(it) },
          newState.nodeOf(replacement)
            ?.item
            ?.let { AddNodeToGraph.create(it) }
            ?.let { listOf(it) },
          newState.nodeOf(replacement)
            ?.edges
            ?.map { it.endpoints }
            ?.map { couplet -> couplet.map { it.item } }
            ?.map { AddEdgeToGraph.create(it) })
        .flatten()
        .toImmutableSet()
    }
  }

  override fun removeUnknownTypedNode(item: Any?): Boolean {
    return mutateState(
      { currentState -> currentState.contents.containsUnknownTyped(item) },
      { currentState -> currentState.toBuilder().removeUnknownTypedNode(item).build() }
    ) { previousState, _ ->
      listOfNotNull(
          previousState.nodeOfUnknownType(item)
            ?.edges
            ?.map { it.endpoints }
            ?.map { couplet -> couplet.map { it.item } }
            ?.map { RemoveEdgeFromGraph.create(it) },
          previousState.nodeOfUnknownType(item)
            ?.item
            ?.let { RemoveNodeFromGraph.create(it) }
            ?.let { listOf(it) })
        .flatten()
        .toImmutableSet()
    }
  }

  override fun addEdge(from: E, to: E) {
    mutateState(
      { currentState -> currentState.edgeOf(from, to) == null },
      { currentState -> currentState.toBuilder().addEdge(from, to).build() }
    ) { _, newState ->
      newState.edgeOf(from, to)
        ?.endpoints
        ?.map { it.item }
        ?.let { AddEdgeToGraph.create(it) }
        ?.let { ImmutableSet.of(it) }
        ?: ImmutableSet.empty()
    }
  }

  override fun removeEdge(from: E, to: E): Boolean {
    return removeEdgeUnknownTyped(from, to)
  }

  override fun removeEdgeUnknownTyped(from: Any?, to: Any?): Boolean {
    return mutateState(
      { currentState -> currentState.edgeOfUnknownType(from, to) != null },
      { currentState -> currentState.toBuilder().removeEdgeUnknownEdge(from, to).build() }
    ) { previousState, _ ->
      previousState.edgeOfUnknownType(from, to)
        ?.endpoints
        ?.map { it.item }
        ?.let { RemoveEdgeFromGraph.create(it) }
        ?.let { ImmutableSet.of(it) }
        ?: ImmutableSet.empty()
    }
  }

  private fun mutateState(
    shouldChange: (ImmutableDirectedGraph<E>) -> Boolean,
    mutateState: (ImmutableDirectedGraph<E>) -> ImmutableDirectedGraph<E>,
    mutationOperations: (ImmutableDirectedGraph<E>, ImmutableDirectedGraph<E>) -> Set<GraphOperation<E>>
  ): Boolean {
    val nextState: ImmutableDirectedGraph<E>
    val previousState: ImmutableDirectedGraph<E> = state
    if (!shouldChange(previousState)) {
      return false
    }
    nextState = mutateState(previousState)
    state = nextState
    val operations = mutationOperations(previousState, nextState)
    mutationEvents.onNext(
      object : MutationEvent<E> {
        override fun state(): DirectedGraph<E> {
          return nextState
        }

        override fun operations(): Set<GraphOperation<E>> {
          return operations
        }
      })
    return true
  }

  override fun nodeOf(item: E): DirectedGraph.DirectedNode<E>? {
    return nodeOfUnknownType(item)
  }

  override fun nodeOfUnknownType(item: Any?): DirectedGraph.DirectedNode<E>? {
    return getState().nodeOfUnknownType(item)
  }

  override fun edgeOf(from: E, to: E): DirectedGraph.DirectedEdge<E>? {
    return edgeOfUnknownType(from, to)
  }

  override fun edgeOfUnknownType(from: Any?, to: Any?): DirectedGraph.DirectedEdge<E>? {
    return getState().edgeOfUnknownType(from, to)
  }

  override val contents: Set<E>
    get() {
      return getState().contents
    }

  override val nodes: Set<out DirectedGraph.DirectedNode<E>>
    get() {
      return getState().nodes
    }

  override val edges: Set<out DirectedGraph.DirectedEdge<E>>
    get() {
      return getState().edges
    }

  override fun toReadOnly(): ObservableDirectedGraph<E> {
    return object : ObservableDirectedGraph<E> {
      override fun observe(): ObservableDirectedGraph.ObservableChannels<E> {
        return this@WritableObservableDirectedGraphImpl.observe()
      }

      override fun nodeOf(item: E): DirectedGraph.DirectedNode<E>? {
        return nodeOfUnknownType(item)
      }

      override fun nodeOfUnknownType(item: Any?): DirectedGraph.DirectedNode<E>? {
        return this@WritableObservableDirectedGraphImpl.nodeOfUnknownType(item)
      }

      override fun edgeOf(from: E, to: E): DirectedGraph.DirectedEdge<E>? {
        return edgeOfUnknownType(from, to)
      }

      override fun edgeOfUnknownType(from: Any?, to: Any?): DirectedGraph.DirectedEdge<E>? {
        return this@WritableObservableDirectedGraphImpl.edgeOfUnknownType(from, to)
      }

      override val nodes: Set<out DirectedGraph.DirectedNode<E>>
        get() {
          return this@WritableObservableDirectedGraphImpl.nodes
        }

      override val edges: Set<out DirectedGraph.DirectedEdge<E>>
        get() {
          return this@WritableObservableDirectedGraphImpl.edges
        }

      override val contents: Set<E>
        get() {
          return this@WritableObservableDirectedGraphImpl.contents
        }
    }
  }

  override fun observe(): ObservableDirectedGraph.ObservableChannels<E> {
    return object : ObservableDirectedGraph.ObservableChannels<E> {
      override fun states(): Observable<DirectedGraph<E>> {
        return observable { emitter: ObservableEmitter<DirectedGraph<E>> ->
          emitter.onNext(getState())
          emitter.setDisposable(
            mutationEvents.map { it.state() }
              .doOnBeforeNext(emitter::onNext)
              .doOnBeforeError(emitter::onError)
              .doOnBeforeComplete(emitter::onComplete)
              .subscribe())
        }
      }

      override fun mutations(): Observable<MutationEvent<E>> {
        return observable { emitter: ObservableEmitter<MutationEvent<E>> ->
          val state = getState()
          val operations: Set<GraphOperation<E>> =
            state.nodes.map { AddNodeToGraph.create(it.item) }
              .plus(
                state.edges
                  .map { AddEdgeToGraph.create(it.endpoints.map { i -> i.item })})
              .toImmutableSet()
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
              .doOnBeforeNext(emitter::onNext)
              .doOnBeforeError(emitter::onError)
              .doOnBeforeComplete(emitter::onComplete)
              .subscribe())
        }
      }
    }
  }

  private fun getState(): ImmutableDirectedGraph<E> {
    return state
  }

  internal interface MutationEvent<E : Any> : ObservableDirectedGraph.MutationEvent<E> {

    override fun state(): DirectedGraph<E>

    override fun operations(): Set<GraphOperation<E>>
  }

  private abstract class NodeOperation<E : Any>(private val item: E) :
      ObservableGraph.NodeOperation<E> {

    override fun item(): E {
      return item
    }
  }

  private class AddNodeToGraph<E : Any> private constructor(item: E) : NodeOperation<E>(item),
    ObservableGraph.AddNodeToGraph<E> {

    companion object {

      fun <E : Any> create(item: E): AddNodeToGraph<E> {
        return AddNodeToGraph(item)
      }
    }
  }

  private class RemoveNodeFromGraph<E : Any> private constructor(item: E) : NodeOperation<E>(item),
    ObservableGraph.RemoveNodeFromGraph<E> {

    companion object {

      fun <E : Any> create(item: E): RemoveNodeFromGraph<E> {
        return RemoveNodeFromGraph(item)
      }
    }
  }

  private abstract class EdgeOperation<E : Any>(private val endpoints: Couplet<E>) :
    ObservableGraph.EdgeOperation<E> {

    override fun endpoints(): Couplet<E> {
      return endpoints
    }
  }

  private class AddEdgeToGraph<E : Any> private constructor(endpoints: Couplet<E>) :
    EdgeOperation<E>(endpoints), ObservableGraph.AddEdgeToGraph<E> {

    companion object {

      fun <E : Any> create(endpoints: Couplet<E>): AddEdgeToGraph<E> {
        return AddEdgeToGraph(endpoints)
      }
    }
  }

  private class RemoveEdgeFromGraph<E : Any> private constructor(endpoints: Couplet<E>) :
    EdgeOperation<E>(endpoints), ObservableGraph.RemoveEdgeFromGraph<E> {

    companion object {

      fun <E : Any> create(endpoints: Couplet<E>): RemoveEdgeFromGraph<E> {
        return RemoveEdgeFromGraph(endpoints)
      }
    }
  }
}