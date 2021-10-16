package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Set
import omnia.data.structure.observable.ObservableGraph.GraphOperation

interface ObservableDirectedGraph<E : Any> : DirectedGraph<E>, ObservableGraph<E> {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableGraph.ObservableChannels<E> {

    override fun states(): Observable<DirectedGraph<E>>

    override fun mutations(): Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableGraph.MutationEvent<E> {

    override fun state(): DirectedGraph<E>

    override fun operations(): Set<out GraphOperation<E>>
  }
}