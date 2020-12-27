package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.Graph
import omnia.data.structure.Set
import omnia.data.structure.tuple.Couplet

interface ObservableGraph<E> : Graph<E>, ObservableDataStructure {
    override fun observe(): ObservableChannels<E>
    interface ObservableChannels<E> : ObservableDataStructure.ObservableChannels {
        override fun states(): Observable<out Graph<E>>
        override fun mutations(): Observable<out MutationEvent<E>>
    }

    interface MutationEvent<E> : ObservableDataStructure.MutationEvent {
        override fun state(): Graph<E>
        override fun operations(): Set<out GraphOperation<E>>
    }

    interface GraphOperation<E> {
        companion object {
            fun <E> justAddNodeToGraphOperations(
                    flowable: Observable<out GraphOperation<E>>): Observable<AddNodeToGraph<E>> {
                return flowable.flatMap { mutation: GraphOperation<E> -> if (mutation is AddNodeToGraph<*>) Observable.just(mutation as AddNodeToGraph<E>) else Observable.empty() }
            }

            fun <E> justRemoveNodeFromGraphOperations(
                    flowable: Observable<out GraphOperation<E>>): Observable<RemoveNodeFromGraph<E>> {
                return flowable.flatMap { mutation: GraphOperation<E> -> if (mutation is RemoveNodeFromGraph<*>) Observable.just(mutation as RemoveNodeFromGraph<E>) else Observable.empty() }
            }

            fun <E> justAddEdgeToGraphOperations(
                    flowable: Observable<out GraphOperation<E>>): Observable<AddEdgeToGraph<E>> {
                return flowable.flatMap { mutation: GraphOperation<E> -> if (mutation is AddEdgeToGraph<*>) Observable.just(mutation as AddEdgeToGraph<E>) else Observable.empty() }
            }

            fun <E> justRemoveEdgeFromGraphOperations(
                    flowable: Observable<out GraphOperation<E>>): Observable<RemoveEdgeFromGraph<E>> {
                return flowable.flatMap { mutation: GraphOperation<E> -> if (mutation is RemoveEdgeFromGraph<*>) Observable.just(mutation as RemoveEdgeFromGraph<E>) else Observable.empty() }
            }
        }
    }

    interface NodeOperation<E> : GraphOperation<E> {
        fun item(): E
    }

    interface EdgeOperation<E> : GraphOperation<E> {
        fun endpoints(): Couplet<E>
    }

    interface AddNodeToGraph<E> : NodeOperation<E>
    interface RemoveNodeFromGraph<E> : NodeOperation<E>
    interface AddEdgeToGraph<E> : EdgeOperation<E>
    interface RemoveEdgeFromGraph<E> : EdgeOperation<E>
}