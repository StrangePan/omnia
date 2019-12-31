package omnia.data.structure;

import java.util.Optional;

public interface DirectedGraph<E> extends Graph<E> {

  interface DirectedNode<E> extends Graph.Node<E> {

    @Override
    Set<? extends DirectedEdge<E>> edges();

    Set<? extends DirectedEdge<E>> outgoingEdges();

    Set<? extends DirectedEdge<E>> incomingEdges();

    @Override
    Set<? extends DirectedNode<E>> neighbors();

    Set<? extends DirectedNode<E>> successors();

    Set<? extends DirectedNode<E>> predecessors();
  }

  interface DirectedEdge<E> extends Graph.Edge<E> {

    DirectedNode<E> start();

    DirectedNode<E> end();

    @Override
    HomogeneousPair<? extends DirectedNode<E>> endpoints();
  }

  @Override
  Optional<? extends DirectedNode<E>> nodeOf(Object item);

  Optional<? extends DirectedEdge<E>> edgeOf(E from, E to);

  @Override
  Set<? extends DirectedNode<E>> nodes();

  @Override
  Set<? extends DirectedEdge<E>> edges();
}
