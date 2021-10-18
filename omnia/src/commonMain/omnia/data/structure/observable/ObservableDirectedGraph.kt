package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Set
import omnia.data.structure.observable.ObservableGraph.GraphOperation

interface ObservableDirectedGraph<E : Any> : DirectedGraph<E>, ObservableGraph<E> {

  override val observables: Observables<E>

  interface Observables<E : Any> : ObservableGraph.Observables<E> {

    override val states: Observable<DirectedGraph<E>>

    override val mutations: Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableGraph.MutationEvent<E> {

    override val state: DirectedGraph<E>

    override val operations: Set<out GraphOperation<E>>
  }
}