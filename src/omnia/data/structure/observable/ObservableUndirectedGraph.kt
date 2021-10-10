package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.Set
import omnia.data.structure.UndirectedGraph
import omnia.data.structure.observable.ObservableGraph.GraphOperation

interface ObservableUndirectedGraph<E : Any> : ObservableGraph<E>, UndirectedGraph<E> {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableGraph.ObservableChannels<E> {

    override fun states(): Observable<UndirectedGraph<E>>

    override fun mutations(): Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableGraph.MutationEvent<E> {

    override fun state(): UndirectedGraph<E>

    override fun operations(): Set<out GraphOperation<E>>
  }
}