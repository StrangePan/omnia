package omnia.data.structure;

import static omnia.data.stream.Collectors.toSet;

import java.util.Optional;
import omnia.data.structure.tuple.Couplet;

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
    Couplet<? extends DirectedNode<E>> endpoints();
  }

  @Override
  Optional<? extends DirectedNode<E>> nodeOf(Object item);

  Optional<? extends DirectedEdge<E>> edgeOf(E from, E to);

  @Override
  Set<? extends DirectedNode<E>> nodes();

  @Override
  Set<? extends DirectedEdge<E>> edges();

  static boolean areEqual(DirectedGraph<?> a, DirectedGraph<?> b) {
    return a == b
        || a != null
        && b != null
        && Set.areEqual(a.contents(), b.contents())
        && Set.areEqual(
            a.edges().stream()
                .map(DirectedEdge::endpoints)
                .map(couplet -> couplet.map(DirectedNode::item))
                .collect(toSet()),
            b.edges().stream()
                .map(DirectedEdge::endpoints)
                .map(couplet -> couplet.map(DirectedNode::item))
                .collect(toSet()));
  }
}
