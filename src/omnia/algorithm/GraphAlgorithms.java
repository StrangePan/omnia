package omnia.algorithm;

import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.lang.reflect.Array;
import java.util.Iterator;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Graph;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.mutable.ArrayList;
import omnia.data.structure.mutable.ArrayQueue;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableList;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.mutable.Queue;
import omnia.data.structure.mutable.Stack;

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
    MutableSet<E> visitedItems = HashSet.create();

    for (DirectedGraph.DirectedNode<E> directedNode : graph.nodes()) {
      if (visitedItems.contains(directedNode.item())) {
        continue;
      }

      MutableSet<E> itemsInStack = HashSet.create();
      MutableList<E> itemStack = new ArrayList<>();
      MutableList<Iterator<? extends DirectedGraph.DirectedNode<E>>> iteratorStack =
          new ArrayList<>();

      itemStack.add(directedNode.item());
      itemsInStack.add(directedNode.item());
      iteratorStack.add(directedNode.successors().iterator());

      while (iteratorStack.isPopulated()) {
        E item = itemStack.itemAt(itemStack.count() - 1);
        Iterator<? extends DirectedGraph.DirectedNode<E>> iterator =
            iteratorStack.itemAt(iteratorStack.count() - 1);

        if (iterator.hasNext()) {
          DirectedGraph.DirectedNode<E> nextNode = iterator.next();
          E nextItem = nextNode.item();

          if (itemsInStack.contains(nextItem)) {
            return true;
          }
          if (visitedItems.contains(nextItem)) {
            continue;
          }

          itemStack.add(nextItem);
          itemsInStack.add(nextItem);
          iteratorStack.add(nextNode.successors().iterator());
        } else {
          visitedItems.add(item);
          itemStack.removeAt(itemStack.count() - 1);
          itemsInStack.remove(item);
          iteratorStack.removeAt(iteratorStack.count() - 1);
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
