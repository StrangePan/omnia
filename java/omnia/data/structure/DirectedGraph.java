package omnia.data.structure;

import java.util.Optional;

public interface DirectedGraph<E> extends Graph<E> {

  interface Node<E> extends Graph.Node<E> {

    @Override
    Set<? extends Edge<E>> edges();

    Set<? extends Edge<E>> outgoingEdges();

    Set<? extends Edge<E>> incomingEdges();

    @Override
    Set<? extends Node<E>> neighbors();

    Set<? extends Node<E>> successors();

    Set<? extends Node<E>> predecessors();
  }

  interface Edge<E> extends Graph.Edge<E> {

    Node<E> start();

    Node<E> end();

    @Override
    Collection<? extends Node<E>> endpoints();
  }

  @Override
  Optional<? extends Node<E>> nodeOf(E element);

  @Override
  Set<? extends Node<E>> nodes();

  @Override
  Set<? extends Edge<E>> edges();
}
