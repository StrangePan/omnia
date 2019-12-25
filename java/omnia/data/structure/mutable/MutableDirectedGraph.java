package omnia.data.structure.mutable;

import omnia.data.structure.DirectedGraph;

public interface MutableDirectedGraph<E> extends MutableGraph<E>, DirectedGraph<E> {

  @Override
  DirectedNode<E> addNode(E item);

  @Override
  DirectedNode<E> addNodeIfAbsent(E item);

  void addEdge(E first, E second);

  boolean removeEdge(E first, E second);
}
