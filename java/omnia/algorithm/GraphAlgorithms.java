package omnia.algorithm;

import static omnia.data.stream.Collectors.toImmutableSet;

import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Graph;
import omnia.data.structure.immutable.ImmutableSet;

public final class GraphAlgorithms {

  public static <E> ImmutableSet<E> sinkElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoOutgoingEdges)
        .filter(GraphAlgorithms::hasIncomingEdges)
        .map(DirectedGraph.Node::element)
        .collect(toImmutableSet());
  }

  public static <E> ImmutableSet<E> sourceElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoIncomingEdges)
        .filter(GraphAlgorithms::hasOutgoingEdges)
        .map(DirectedGraph.Node::element)
        .collect(toImmutableSet());
  }

  public static <E> ImmutableSet<E> isolatedElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoNeighbors)
        .map(DirectedGraph.Node::element)
        .collect(toImmutableSet());
  }

  private static boolean hasNeighbors(Graph.Node<?> node) {
    return node.neighbors().isPopulated();
  }

  private static boolean hasNoNeighbors(Graph.Node<?> node) {
    return !hasNeighbors(node);
  }

  private static boolean hasOutgoingEdges(DirectedGraph.Node<?> node) {
    return node.outgoingEdges().isPopulated();
  }

  private static boolean hasNoOutgoingEdges(DirectedGraph.Node<?> node) {
    return !hasOutgoingEdges(node);
  }

  private static boolean hasIncomingEdges(DirectedGraph.Node<?> node) {
    return node.incomingEdges().isPopulated();
  }

  private static boolean hasNoIncomingEdges(DirectedGraph.Node<?> node) {
    return !hasIncomingEdges(node);
  }

  private GraphAlgorithms() {}
}
