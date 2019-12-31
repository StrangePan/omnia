package omnia.algorithm;

import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Graph;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.mutable.ArrayQueue;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.mutable.Queue;

public final class GraphAlgorithms {

  public static <E> ImmutableSet<E> sinkElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoOutgoingEdges)
        .filter(GraphAlgorithms::hasIncomingEdges)
        .map(DirectedGraph.DirectedNode::item)
        .collect(toImmutableSet());
  }

  public static <E> ImmutableSet<E> sourceElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoIncomingEdges)
        .filter(GraphAlgorithms::hasOutgoingEdges)
        .map(DirectedGraph.DirectedNode::item)
        .collect(toImmutableSet());
  }

  public static <E> ImmutableSet<E> isolatedElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoNeighbors)
        .map(DirectedGraph.DirectedNode::item)
        .collect(toImmutableSet());
  }

  public static <E> boolean isCyclical(DirectedGraph<E> graph) {
    MutableSet<DirectedGraph.DirectedNode<E>> seenNodes = new HashSet<>();

    for (DirectedGraph.DirectedNode<E> directedNode : graph.nodes()) {
      if (seenNodes.contains(directedNode)) {
        continue;
      }
      Queue<DirectedGraph.DirectedNode<E>> queue = new ArrayQueue<>();
      queue.enqueue(directedNode);
      while (queue.isPopulated()) {
        for (DirectedGraph.DirectedNode<E> n :
            queue.dequeue().get().outgoingEdges().stream()
                .map(DirectedGraph.DirectedEdge::end)
                .collect(toSet())) {
          if (seenNodes.contains(n)) {
            return true;
          }
          seenNodes.add(n);
        }
      }
    }
    return false;
  }

  public static <E> boolean isAcyclical(DirectedGraph<E> graph) {
    return !isCyclical(graph);
  }

  private static boolean hasNeighbors(Graph.Node<?> node) {
    return node.neighbors().isPopulated();
  }

  private static boolean hasNoNeighbors(Graph.Node<?> node) {
    return !hasNeighbors(node);
  }

  private static boolean hasOutgoingEdges(DirectedGraph.DirectedNode<?> directedNode) {
    return directedNode.outgoingEdges().isPopulated();
  }

  private static boolean hasNoOutgoingEdges(DirectedGraph.DirectedNode<?> directedNode) {
    return !hasOutgoingEdges(directedNode);
  }

  private static boolean hasIncomingEdges(DirectedGraph.DirectedNode<?> directedNode) {
    return directedNode.incomingEdges().isPopulated();
  }

  private static boolean hasNoIncomingEdges(DirectedGraph.DirectedNode<?> directedNode) {
    return !hasIncomingEdges(directedNode);
  }

  private GraphAlgorithms() {}
}
