package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.Set
import omnia.data.structure.UndirectedGraph
import omnia.data.structure.observable.ObservableGraph.GraphOperation

interface ObservableUndirectedGraph<E : Any> : ObservableGraph<E>, UndirectedGraph<E> {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableGraph.ObservableChannels<E> {

    override fun states(): Observable<out UndirectedGraph<E>>

    override fun mutations(): Observable<out MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableGraph.MutationEvent<E> {

    override fun state(): UndirectedGraph<E>

    override fun operations(): Set<out GraphOperation<E>>
  }
}