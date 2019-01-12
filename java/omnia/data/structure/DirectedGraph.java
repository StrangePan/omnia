package omnia.data.structure;

import java.util.Optional;

public interface DirectedGraph<E> extends Graph<E> {

  interface Node<E> extends Graph.Node<E> {

    @Override
    Set<? extends DirectedGraph.Edge<E>> edges();

    Set<? extends DirectedGraph.Edge<E>> outgoingEdges();

    Set<? extends DirectedGraph.Edge<E>> incomingEdges();

    @Override
    Set<? extends DirectedGraph.Node<E>> neighbors();

    Set<? extends DirectedGraph.Node<E>> successors();

    Set<? extends DirectedGraph.Node<E>> predecessors();
  }

  interface Edge<E> extends Graph.Edge<E> {

    DirectedGraph.Node<E> start();

    DirectedGraph.Node<E> end();

    @Override
    Collection<? extends DirectedGraph.Node<E>> endpoints();
  }

  @Override
  Optional<? extends DirectedGraph.Node<E>> nodeOf(E element);

  @Override
  Set<? extends DirectedGraph.Node<E>> nodes();

  @Override
  Set<? extends DirectedGraph.Edge<E>> edges();
}
