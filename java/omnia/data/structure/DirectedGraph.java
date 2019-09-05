package omnia.data.structure;

import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Objects;
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

    static boolean areEqual(Node<?> a, Node<?> b) {
      if (a == b) {
        return true;
      }
      if (a == null || b == null) {
        return false;
      }
      return Objects.equals(a.element(), b.element());
    }
  }

  interface Edge<E> extends Graph.Edge<E> {

    DirectedGraph.Node<E> start();

    DirectedGraph.Node<E> end();

    @Override
    Collection<? extends DirectedGraph.Node<E>> endpoints();

    static boolean areEqual(Edge<?> a, Edge<?> b) {
      if (a == b) {
        return true;
      }
      if (a == null || b == null) {
        return false;
      }
      return Node.areEqual(a.start(), b.start())
          && Node.areEqual(a.end(), b.end());
    }
  }

  @Override
  Optional<? extends DirectedGraph.Node<E>> nodeOf(E element);

  @Override
  Set<? extends DirectedGraph.Node<E>> nodes();

  @Override
  Set<? extends DirectedGraph.Edge<E>> edges();

  static <E> boolean areEqual(DirectedGraph<E> first, DirectedGraph<E> second) {
    if (first == second) {
      return true;
    }
    if (first == null || second == null) {
      return false;
    }
    return Set.areEqual(first.contents(), second.contents())
        && Set.areEqual(
            first.edges().stream()
                .map(edge -> edge.endpoints().stream().map(Node::element).collect(toImmutableSet()))
                .collect(toSet()),
            second.edges().stream()
                .map(edge -> edge.endpoints().stream().map(Node::element).collect(toImmutableSet()))
                .collect(toSet()));
  }
}
