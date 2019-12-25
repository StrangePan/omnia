package omnia.data.structure.mutable;

import omnia.data.structure.UndirectedGraph;

public interface MutableUndirectedGraph<E> extends MutableGraph<E>, UndirectedGraph<E> {

  @Override
  UndirectedNode<E> addNode(E item);

  @Override
  UndirectedNode<E> addNodeIfAbsent(E item);

  void addEdge(E first, E second);

  boolean removeEdge(E first, E second);
}
