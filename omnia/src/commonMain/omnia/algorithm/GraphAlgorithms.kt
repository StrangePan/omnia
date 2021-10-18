package omnia.algorithm

import omnia.data.structure.DirectedGraph
import omnia.data.structure.DirectedGraph.DirectedNode
import omnia.data.structure.Graph
import omnia.data.structure.List
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.mutable.ArrayList
import omnia.data.structure.mutable.ArrayQueue
import omnia.data.structure.mutable.ArrayStack
import omnia.data.structure.mutable.HashSet
import omnia.data.structure.mutable.MutableList
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.mutable.Queue
import omnia.data.structure.mutable.Stack
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

/**
 * A collection of useful algorithms for analyzing and querying the contents of graph data
 * structures.
 */
object GraphAlgorithms {

  /**
   * Searches the graph for items that has no outgoing edges but do have incoming edges.
   *
   * @param graph the graph to search
   * @param E the type of items the graph contains
   * @return The set of items in the graph that have no outgoing edges but has incoming edges. May
   * return the empty set.
   */
  fun <E : Any> sinkElements(graph: DirectedGraph<out E>): ImmutableSet<E> {
    return graph.nodes
        .filter { hasNoOutgoingEdges(it) }
        .filter { hasIncomingEdges(it) }
        .map { it.item }
        .toImmutableSet()
  }

  /**
   * Searches the graph for items that have no incoming edges but do have outgoing edges.
   *
   * @param graph the graph to search
   * @param E the type of items the graph contains
   * @return The set of items in the graph that have no incoming edges but has outgoing edges. May
   * return the empty set.
   */
  fun <E : Any> sourceElements(graph: DirectedGraph<E>): Set<E> {
    return graph.nodes
        .filter { hasNoIncomingEdges(it) }
        .filter { hasOutgoingEdges(it) }
        .map { it.item }
        .toImmutableSet()
  }

  /**
   * Returns the items from the graph that has no attached edges.
   *
   * @param graph the graph to search
   * @param E the type of items the graph contains
   * @return The set of items in the graph that have no edges. May return the empty set.
   */
  fun <E : Any> isolatedElements(graph: Graph<E>): Set<E> {
    return graph.nodes
      .filter { hasNoNeighbors(it) }
      .map { it.item }
      .toImmutableSet()
  }

  /**
   * Tests if the given directed graph is cyclical, meaning there exists a set of directed edges
   * that forms a loop.
   *
   * @param graph the graph to test
   * @return true if the graph is cyclical, false if not
   */
  fun isCyclical(graph: DirectedGraph<*>): Boolean {
    return findAnyCycle(graph) != null
  }

  /**
   * Returns a list of nodes that form a cycle in a directed graph. This method's determinism is
   * entirely dependent on the iteration determinism of the provided graph. If the graph's edges
   * are deterministically iterated over, then this method's results will also be deterministic.
   *
   * If a cycle is found, a List containing the nodes forming the cycle is returned. The list
   * is ordered such that each node in the list is followed by its successor. Each item appears in
   * the list at most once.
   *
   * @param graph the graph to search for a cycle
   * @param T the type of item contained in the graph
   * @return a list of nodes that form a cycle, or nothing if no cycles were found
   */
  fun <T : Any> findAnyCycle(graph: DirectedGraph<T>): List<T>? {
    val visitedItems: MutableSet<T> = HashSet.create()

    // iterate over every node, skipping over those we've already visited in another inner loop
    for (directedNode in graph.nodes) {
      if (visitedItems.contains(directedNode.item)) {
        continue
      }

      // depth-first traversal
      val itemStack: MutableList<T> = ArrayList.create() // stack of nodes in this current traversal
      val itemsInStack: MutableSet<T> = HashSet.create() // for quick lookups
      // correlates to itemStack. iterators track state.
      val iteratorStack: MutableList<Iterator<DirectedNode<T>>> =
        ArrayList.create()
      itemStack.add(directedNode.item)
      itemsInStack.add(directedNode.item)
      iteratorStack.add(directedNode.successors.iterator())
      while (iteratorStack.isPopulated) {
        val item = itemStack.itemAt(itemStack.count - 1)
        val iterator = iteratorStack.itemAt(iteratorStack.count - 1)
        if (iterator.hasNext()) {
          val nextNode = iterator.next()
          val nextItem = nextNode.item
          if (itemsInStack.contains(nextItem)) {
            // navigate back up the stack, building a list representing the cycle
            return ListAlgorithms.sublistOf(
              itemStack,
              itemStack.indexOf(nextItem)!!,
              itemStack.count
            )
          }
          if (visitedItems.contains(nextItem)) {
            continue
          }
          itemStack.add(nextItem)
          itemsInStack.add(nextItem)
          iteratorStack.add(nextNode.successors.iterator())
        } else {
          // only if all possible paths from the current node are acyclical
          visitedItems.add(item)
          itemStack.removeAt(itemStack.count - 1)
          itemsInStack.remove(item)
          iteratorStack.removeAt(iteratorStack.count - 1)
        }
      }
    }
    return null
  }

  /**
   * Traverses the given graph, producing a list of its nodes sorted topologically such that for
   * every edge AB where A is the node at the edge start and B is the node at the edge end, A
   * will precede B in the result (A will have a lower index than B).
   *
   * If the given graph is disconnected, then each subgraph will be locally grouped in the
   * result.
   *
   * @param graph The graph whose nodes to topologically sort. Must be acyclic.
   * @return an ordered list containing all nodes from the given graph sorted topologically such
   *   that every node will appear in the list before its successors
   * @throws IllegalArgumentException if the graph is given graph is cyclic
   */
  fun <T : Any> topologicallySort(graph: DirectedGraph<out T>): ImmutableList<T> {
    // iterative depth-first search with back tracking
    val result: ImmutableList.Builder<T> = ImmutableList.builder()
    val itemsInResult: MutableSet<DirectedNode<out T>> = HashSet.create()
    val stack: Stack<Couple<DirectedNode<out T>, Iterator<DirectedNode<out T>>>> =
      ArrayStack.create()
    val itemsInStack: MutableSet<DirectedNode<out T>> = HashSet.create()

    // all starting nodes
    for (sourceNode in graph.nodes) {
      if (itemsInResult.contains(sourceNode)) {
        continue
      }

      // traverse entire sub-graph to help cluster nodes
      val subgraphNodes: Set<DirectedNode<out T>> = findOtherNodesInSubgraphContaining(sourceNode)
      for (rootNode in subgraphNodes) {
        if (rootNode.outgoingEdges.isPopulated) {
          continue
        }
        stack.push(Tuple.of(rootNode, rootNode.predecessors.iterator()))
        itemsInStack.add(rootNode)

        // core loop
        var frame = stack.peek()
        while (frame != null /* base case*/) {
          val iterator = frame.second
          if (iterator.hasNext()) {
            val next = iterator.next()

            // validation: cyclic graph detection
            require(!itemsInStack.contains(next)) { "graph must be acyclic to perform a topological sort" }

            // deduplication: helps reduce complexity, corrects output
            if (!itemsInResult.contains(next)) {

              // put successors in the stack for future processing
              stack.push(Tuple.of(next, next.predecessors.iterator()))
              itemsInStack.add(next)
            }
          } else {
            val current = frame.first

            // no other successors, add to result
            result.add(current.item)
            itemsInResult.add(current)
            stack.pop()
            itemsInStack.remove(current)
          }
          frame = stack.peek()
        }
      }
    }
    val resultList = result.build()
    require(
      resultList.count == graph.nodes.count
    ) { "graph must be acyclic to perform a topological sort" }
    return resultList
  }

  /**
   *  Isolates a specific subgraph in a larger graph by starting at the provided node and traversing
   * all neighbors and trimming all others. Returns the set of nodes contained in the subgraph, but
   * does not return the subgraph itself. Callers need to know how to assemble a graph from the
   * returned results.
   */
  fun <T : Graph.Node<*>> findOtherNodesInSubgraphContaining(source: T): ImmutableSet<T> {
    val set: MutableSet<T> = HashSet.create()
    val queue: Queue<T> = ArrayQueue.create()
    queue.enqueue(source)
    var node = queue.dequeue()
    while (node != null) {
      if (set.contains(node)) {
        node = queue.dequeue()
        continue
      }
      set.add(node)
      for (neighbor in node.neighbors) {
        if (set.containsUnknownTyped(neighbor)) {
          continue
        }
        @Suppress("UNCHECKED_CAST")
        queue.enqueue(neighbor as T)
      }
      node = queue.dequeue()
    }
    return ImmutableSet.copyOf(set)
  }

  /** Finds and returns all transitive predecessors of the specified nodes. */
  fun <T : DirectedNode<*>> findAllPredecessorsOf(nodes: Iterable<T>): ImmutableSet<T> {
    val seenNodes = HashSet.copyOf(nodes)
    val queue = ArrayQueue.create<T>()
    for (node in nodes) {
      queue.enqueue(node)
    }
    while (queue.isPopulated) {
      val node = queue.dequeue()!!
      for (predecessor in node.predecessors) {
        @Suppress("UNCHECKED_CAST")
        if (predecessor as T !in seenNodes) {
          seenNodes.add(predecessor)
          queue.enqueue(predecessor)
        }
      }
    }
    return ImmutableSet.copyOf(seenNodes)
  }

  /** Finds all transitive successors of the specified nodes. */
  fun <T : DirectedNode<*>> findAllSuccessorsOf(nodes: Iterable<T>): ImmutableSet<T> {
    val seenNodes = HashSet.copyOf(nodes)
    val queue = ArrayQueue.create<T>()
    for (node in nodes) {
      queue.enqueue(node)
    }
    while (queue.isPopulated) {
      val node = queue.dequeue()!!
      for (successor in node.successors) {
        @Suppress("UNCHECKED_CAST")
        if (successor as T !in seenNodes) {
          seenNodes.add(successor)
          queue.enqueue(successor)
        }
      }
    }
    return ImmutableSet.copyOf(seenNodes)
  }

  /**
   * Returns all nodes that are transitive successors or transitive predecessors of the specified
   * nodes. this differs from [findOtherNodesInSubgraphContaining] in that this will not include
   * nodes that are not transitive successors or predecessors of the specified nodes; neighboring
   * nodes are not intrinsically included.
   */
  fun <T : DirectedNode<*>> findAllPredecessorsAndSuccessorsOf(nodes: Iterable<T>):
      ImmutableSet<T> {
    return SetAlgorithms.unionOf(findAllPredecessorsOf(nodes), findAllSuccessorsOf(nodes))
  }

  /**
   * Returns all nodes that are transitive successors or transitive predecessors of the specified
   * nodes. this differs from [findOtherNodesInSubgraphContaining] in that this will not include
   * nodes that are not transitive successors or predecessors of the specified nodes; neighboring
   * nodes are not intrinsically included.
   */
  fun <T : DirectedNode<*>> findAllPredecessorsAndSuccessorsOf(node: T):
      ImmutableSet<T> {
    return SetAlgorithms.unionOf(
      findAllPredecessorsOf(ImmutableSet.of(node)), findAllSuccessorsOf(ImmutableSet.of(node)))
  }

  /**
   * Tests if the provided graph is acyclical, that is NOT cyclical. Simply returns the inverse of
   * [findAnyCycle]
   *
   * @param graph the graph to test
   * @return true if the graph is acyclical, false if the graph is cyclical
   */
  fun isAcyclical(graph: DirectedGraph<*>): Boolean {
    return !isCyclical(graph)
  }

  private fun hasNeighbors(node: Graph.Node<*>): Boolean {
    return node.neighbors.isPopulated
  }

  private fun hasNoNeighbors(node: Graph.Node<*>): Boolean {
    return !hasNeighbors(node)
  }

  private fun hasOutgoingEdges(directedNode: DirectedNode<*>): Boolean {
    return directedNode.outgoingEdges.isPopulated
  }

  private fun hasNoOutgoingEdges(directedNode: DirectedNode<*>): Boolean {
    return !hasOutgoingEdges(directedNode)
  }

  private fun hasIncomingEdges(directedNode: DirectedNode<*>): Boolean {
    return directedNode.incomingEdges.isPopulated
  }

  private fun hasNoIncomingEdges(directedNode: DirectedNode<*>): Boolean {
    return !hasIncomingEdges(directedNode)
  }
}