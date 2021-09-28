package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.Graph
import omnia.data.structure.Set
import omnia.data.structure.tuple.Couplet

interface ObservableGraph<E : Any> : Graph<E>, ObservableDataStructure {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableDataStructure.ObservableChannels {

    override fun states(): Observable<out Graph<E>>

    override fun mutations(): Observable<out MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableDataStructure.MutationEvent {

    override fun state(): Graph<E>

    override fun operations(): Set<out GraphOperation<E>>
  }

  interface GraphOperation<E : Any> {
    companion object {

      fun <E : Any> justAddNodeToGraphOperations(
        flowable: Observable<out GraphOperation<E>>
      ): Observable<AddNodeToGraph<E>> {
        return flowable.flatMap { mutation: GraphOperation<E> ->
          if (mutation is AddNodeToGraph<*>) Observable.just(
            mutation as AddNodeToGraph<E>
          ) else Observable.empty()
        }
      }

      fun <E : Any> justRemoveNodeFromGraphOperations(
        flowable: Observable<out GraphOperation<E>>
      ): Observable<RemoveNodeFromGraph<E>> {
        return flowable.flatMap { mutation: GraphOperation<E> ->
          if (mutation is RemoveNodeFromGraph<*>) Observable.just(
            mutation as RemoveNodeFromGraph<E>
          ) else Observable.empty()
        }
      }

      fun <E : Any> justAddEdgeToGraphOperations(
        flowable: Observable<out GraphOperation<E>>
      ): Observable<AddEdgeToGraph<E>> {
        return flowable.flatMap { mutation: GraphOperation<E> ->
          if (mutation is AddEdgeToGraph<*>) Observable.just(
            mutation as AddEdgeToGraph<E>
          ) else Observable.empty()
        }
      }

      fun <E : Any> justRemoveEdgeFromGraphOperations(
        flowable: Observable<out GraphOperation<E>>
      ): Observable<RemoveEdgeFromGraph<E>> {
        return flowable.flatMap { mutation: GraphOperation<E> ->
          if (mutation is RemoveEdgeFromGraph<*>) Observable.just(
            mutation as RemoveEdgeFromGraph<E>
          ) else Observable.empty()
        }
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