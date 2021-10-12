package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapNotNull
import omnia.data.structure.Graph
import omnia.data.structure.Set
import omnia.data.structure.tuple.Couplet

interface ObservableGraph<E : Any> : Graph<E>, ObservableDataStructure {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableDataStructure.ObservableChannels {

    override fun states(): Observable<Graph<E>>

    override fun mutations(): Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableDataStructure.MutationEvent {

    override fun state(): Graph<E>

    override fun operations(): Set<out GraphOperation<E>>
  }

  interface GraphOperation<E : Any> {
    companion object {

      fun <E : Any> justAddNodeToGraphOperations(operations: Observable<GraphOperation<E>>):
          Observable<AddNodeToGraph<E>> {
        return operations.mapNotNull { it as? AddNodeToGraph<E> }
      }

      fun <E : Any> justRemoveNodeFromGraphOperations(operations: Observable<GraphOperation<E>>):
          Observable<RemoveNodeFromGraph<E>> {
        return operations.mapNotNull { it as? RemoveNodeFromGraph<E> }
      }

      fun <E : Any> justAddEdgeToGraphOperations(operations: Observable<GraphOperation<E>>):
          Observable<AddEdgeToGraph<E>> {
        return operations.mapNotNull { it as? AddEdgeToGraph<E> }
      }

      fun <E : Any> justRemoveEdgeFromGraphOperations(operations: Observable<GraphOperation<E>>):
          Observable<RemoveEdgeFromGraph<E>> {
        return operations.mapNotNull { it as? RemoveEdgeFromGraph<E> }
      }
    }
  }

  interface NodeOperation<E : Any> : GraphOperation<E> {

    fun item(): E
  }

  interface EdgeOperation<E : Any> : GraphOperation<E> {

    fun endpoints(): Couplet<E>
  }

  interface AddNodeToGraph<E : Any> : NodeOperation<E>

  interface RemoveNodeFromGraph<E : Any> : NodeOperation<E>

  interface AddEdgeToGraph<E : Any> : EdgeOperation<E>

  interface RemoveEdgeFromGraph<E : Any> : EdgeOperation<E>
}