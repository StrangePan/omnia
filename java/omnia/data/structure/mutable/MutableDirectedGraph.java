package omnia.data.structure.mutable;

import java.util.Optional;
import omnia.data.structure.DirectedGraph;

public interface MutableDirectedGraph<E> extends MutableGraph<E>, DirectedGraph<E> {

  interface MutableNode<E> extends MutableGraph.MutableNode<E>, DirectedGraph.Node<E> {

    @Override
    MutableSet<? extends MutableEdge<E>> edges();

    @Override
    MutableSet<? extends MutableEdge<E>> outgoingEdges();

    @Override
    MutableSet<? extends MutableEdge<E>> incomingEdges();

    @Override
    MutableSet<? extends MutableNode<E>> neighbors();

    @Override
    MutableSet<? extends MutableNode<E>> successors();

    @Override
    MutableSet<? extends MutableNode<E>> predecessors();
  }

  interface MutableEdge<E> extends MutableGraph.MutableEdge<E>, DirectedGraph.Edge<E> {

    @Override
    MutableNode<E> start();

    @Override
    MutableNode<E> end();

    @Override
    MutableCollection<? extends MutableNode<E>> endpoints();
  }

  @Override
  Optional<? extends MutableNode<E>> nodeOf(E element);

  @Override
  MutableSet<? extends MutableNode<E>> nodes();

  @Override
  MutableSet<? extends MutableEdge<E>> edges();
}
