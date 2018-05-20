package omnia.data.structure;

import java.util.Optional;

public interface DirectedGraph<E> extends Graph<E> {

  interface Node<E> extends Graph.Node<E> {

    @Override
    Collection<? extends Edge<E>> edges();

    Collection<? extends Edge<E>> outgoingEdges();

    Collection<? extends Edge<E>> incomingEdges();

    @Override
    Collection<? extends Node<E>> neighbors();

    Collection<? extends Node<E>> successors();

    Collection<? extends Node<E>> predecessors();
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
  Collection<? extends Node<E>> nodes();

  @Override
  Collection<? extends Edge<E>> edges();
}
