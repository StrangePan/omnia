package omnia.data.structure.mutable;

import omnia.data.structure.DirectedGraph;

public interface MutableDirectedGraph<E> extends MutableGraph<E>, DirectedGraph<E> {

  void addEdge(E first, E second);

  boolean removeEdge(Object first, Object second);
}
