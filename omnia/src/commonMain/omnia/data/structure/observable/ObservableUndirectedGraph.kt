package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.Set
import omnia.data.structure.UndirectedGraph
import omnia.data.structure.observable.ObservableGraph.GraphOperation

interface ObservableUndirectedGraph<E : Any> : ObservableGraph<E>, UndirectedGraph<E> {

  override val observables: Observables<E>

  interface Observables<E : Any> : ObservableGraph.Observables<E> {

    override val states: Observable<UndirectedGraph<E>>

    override val mutations: Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableGraph.MutationEvent<E> {

    override val state: UndirectedGraph<E>

    override val operations: Set<out GraphOperation<E>>
  }
}