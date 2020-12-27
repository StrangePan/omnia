package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Set
import omnia.data.structure.observable.ObservableGraph.GraphOperation

interface ObservableDirectedGraph<E> : DirectedGraph<E>, ObservableGraph<E> {
    override fun observe(): ObservableChannels<E>
    interface ObservableChannels<E> : ObservableGraph.ObservableChannels<E> {
        override fun states(): Observable<out DirectedGraph<E>>
        override fun mutations(): Observable<out MutationEvent<E>>
    }

    interface MutationEvent<E> : ObservableGraph.MutationEvent<E> {
        override fun state(): DirectedGraph<E>
        override fun operations(): Set<out GraphOperation<E>>
    }
}