package omnia.data.structure.mutable;

import java.util.Optional;
import omnia.data.structure.DirectedGraph;

public interface MutableDirectedGraph<E> extends MutableGraph<E>, DirectedGraph<E> {

  interface Node<E> extends MutableGraph.Node<E>, DirectedGraph.Node<E> {

    @Override
    MutableSet<? extends Edge<E>> edges();

    @Override
    MutableSet<? extends Edge<E>> outgoingEdges();

    @Override
    MutableSet<? extends Edge<E>> incomingEdges();

    @Override
    MutableSet<? extends Node<E>> neighbors();

    @Override
    MutableSet<? extends Node<E>> successors();

    @Override
    MutableSet<? extends Node<E>> predecessors();
  }

  interface Edge<E> extends MutableGraph.Edge<E>, DirectedGraph.Edge<E> {

    @Override
    Node<E> start();

    @Override
    Node<E> end();

    @Override
    MutableCollection<? extends Node<E>> endpoints();
  }

  @Override
  Optional<? extends Node<E>> nodeOf(E element);

  @Override
  MutableSet<? extends Node<E>> nodes();

  @Override
  MutableSet<? extends Edge<E>> edges();
}
