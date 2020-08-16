package omnia.algorithm;

import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Iterator;
import java.util.Optional;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Graph;
import omnia.data.structure.List;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableList;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.mutable.ArrayList;
import omnia.data.structure.mutable.HashMap;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableList;
import omnia.data.structure.mutable.MutableMap;
import omnia.data.structure.mutable.MutableSet;

/**
 * A collection of useful algorithms for nalyzing and querying the contents of graph data
 * structures.
 */
public final class GraphAlgorithms {

  /**
   * Searches the graph for items that has no outgoing edges but do have incoming edges.
   *
   * @param graph the graph to search
   * @param <E> the type of items the graph contains
   * @return The set of items in the graph that have no outgoing edges but has incoming edges. May
   *     return the empty set.
   */
  public static <E> ImmutableSet<E> sinkElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoOutgoingEdges)
        .filter(GraphAlgorithms::hasIncomingEdges)
        .map(DirectedGraph.DirectedNode::item)
        .collect(toImmutableSet());
  }

  /**
   * Searches the graph for items that have no incoming edges but do have outgoing edges.
   *
   * @param graph the graph to search
   * @param <E> the type of items the graph contains
   * @return The set of items in the graph that have no incoming edges but has outgoing edges. May
   *     return the empty set.
   */
  public static <E> Set<E> sourceElements(DirectedGraph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoIncomingEdges)
        .filter(GraphAlgorithms::hasOutgoingEdges)
        .map(DirectedGraph.DirectedNode::item)
        .collect(toSet());
  }

  /**
   * Returns the items from the graph that has no attached edges.
   *
   * @param graph the graph to search
   * @param <E> the type of items the graph contains
   * @return The set of items in the graph that have no edges. May return the empty set.
   */
  public static <E> Set<E> isolatedElements(Graph<E> graph) {
    return graph.nodes()
        .stream()
        .filter(GraphAlgorithms::hasNoNeighbors)
        .map(Graph.Node::item)
        .collect(toSet());
  }

  /**
   * Tests if the given directed graph is cyclical, meaning there exists a set of directed edges
   * that forms a loop.
   *
   * @param graph the graph to test
   * @return true if the graph is cyclical, false if not
   */
  public static boolean isCyclical(DirectedGraph<?> graph) {
    return findAnyCycle(graph).isPresent();
  }

  /**
   * Returns a list of nodes that form a cycle in a directed graph. This method's determinism is
   * entirely dependent on the iteration determinism of the provided graph. If the graph's edges
   * are deterministically iterated over, then this method's results will also be deterministic.
   *
   * <p>If a cycle is found, a List containing the nodes forming the cycle is returned. The list
   * is ordered such that each node in the list is followed by its successor. Each item appears in
   * the list at most once.</p>
   *
   * @param graph the graph to search for a cycle
   * @param <T> the type of item contained in the graph
   * @return a list of nodes that form a cycle, or nothing if no cycles were found
   */
  public static <T> Optional<List<T>> findAnyCycle(DirectedGraph<T> graph) {
    MutableSet<T> visitedItems = HashSet.create();
    MutableMap<T, T> visitedFrom = HashMap.create();

    // iterate over every node, skipping over those we've already visited in another inner loop
    for (DirectedGraph.DirectedNode<T> directedNode : graph.nodes()) {
      if (visitedItems.contains(directedNode.item())) {
        continue;
      }

      // depth-first traversal
      MutableList<T> itemStack = ArrayList.create();  // stack of nodes in this current traversal
      MutableSet<T> itemsInStack = HashSet.create();  // for quick lookups
      MutableList<Iterator<? extends DirectedGraph.DirectedNode<T>>> iteratorStack =
          ArrayList.create();  // correlates to itemStack. iterators track state.

      itemStack.add(directedNode.item());
      itemsInStack.add(directedNode.item());
      iteratorStack.add(directedNode.successors().iterator());

      while (iteratorStack.isPopulated()) {
        T item = itemStack.itemAt(itemStack.count() - 1);
        Iterator<? extends DirectedGraph.DirectedNode<T>> iterator =
            iteratorStack.itemAt(iteratorStack.count() - 1);

        if (iterator.hasNext()) {
          DirectedGraph.DirectedNode<T> nextNode = iterator.next();
          T nextItem = nextNode.item();

          if (itemsInStack.contains(nextItem)) {
            // navigate back up the stack, building a list representing the cycle
            return Optional.of(
                ListAlgorithms.sublistOf(
                    itemStack, itemStack.indexOf(nextItem).getAsInt(), itemStack.count()));
          }
          if (visitedItems.contains(nextItem)) {
            continue;
          }

          itemStack.add(nextItem);
          itemsInStack.add(nextItem);
          iteratorStack.add(nextNode.successors().iterator());
        } else {
          // only if all possible paths from the current node are acyclical
          visitedItems.add(item);
          itemStack.removeAt(itemStack.count() - 1);
          itemsInStack.remove(item);
          iteratorStack.removeAt(iteratorStack.count() - 1);
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Tests if the provided graph is acyclical, that is NOT cyclical. Simply returns the inverse of
   * {@link #findAnyCycle(DirectedGraph)}
   *
   * @param graph the graph to test
   * @return true if the graph is acyclical, false if the graph is cyclical
   */
  public static boolean isAcyclical(DirectedGraph<?> graph) {
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
