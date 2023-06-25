package omnia.algorithm

import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.algorithm.GraphAlgorithms.findAnyCycle
import omnia.algorithm.GraphAlgorithms.findOtherNodesInSubgraphContaining
import omnia.algorithm.GraphAlgorithms.isCyclical
import omnia.algorithm.GraphAlgorithms.isolatedElements
import omnia.algorithm.GraphAlgorithms.sinkElements
import omnia.algorithm.GraphAlgorithms.sourceElements
import omnia.algorithm.GraphAlgorithms.topologicallySort
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Graph
import omnia.data.structure.List
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableDirectedGraph
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.mutable.HashSet
import omnia.data.structure.mutable.MutableSet
import omnia.util.test.fluent.Assertion
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.containsExactlyElementsIn
import omnia.util.test.fluent.doesNotContain
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isNotNull
import omnia.util.test.fluent.isNull
import omnia.util.test.fluent.isTrue

class GraphAlgorithmsTest {

  @Test
  fun isolatedElements_whenGraphEmpty_returnsEmpty() {
    assertThat(isolatedElements(ImmutableDirectedGraph.empty())).isEmpty()
  }

  @Test
  fun isolatedElements_whenAllIsolated_returnsAll() {
    assertThat(
      isolatedElements(
        ImmutableDirectedGraph.builder<Int>().addNode(1).addNode(2).addNode(3).build()
      )
    )
      .containsExactly(1, 2, 3)
  }

  @Test
  fun isolatedElements_whenAllConnected_returnsNone() {
    assertThat(
      isolatedElements(
        ImmutableDirectedGraph.builder<Int>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .build()
      )
    )
      .isEmpty()
  }

  @Test
  fun sourceElements_whenGraphEmpty_returnsEmpty() {
    assertThat(sourceElements(ImmutableDirectedGraph.empty())).isEmpty()
  }

  @Test
  fun sourceElements_whenGraphHasOneSource_returnsSource() {
    assertThat(
      sourceElements(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .build()
      )
    )
      .containsExactly(1)
  }

  @Test
  fun sourceElements_whenGraphIsLoop_returnsEmpty() {
    assertThat(
      sourceElements(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .addEdge(3, 1)
          .build()
      )
    )
      .isEmpty()
  }

  @Test
  fun sinkElements_whenGraphEmpty_returnsEmpty() {
    assertThat(sinkElements(ImmutableDirectedGraph.empty())).isEmpty()
  }

  @Test
  fun sinkElements_whenGraphHasOneSink_returnsSink() {
    assertThat(
      sinkElements(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .build()
      )
    )
      .containsExactly(3)
  }

  @Test
  fun sinkElements_whenGraphIsLoop_returnsSink() {
    assertThat(
      sinkElements(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .addEdge(3, 1)
          .build()
      )
    )
      .isEmpty()
  }

  @Test
  fun isCyclical_whenEmpty_isFalse() {
    val graph: DirectedGraph<Any> = ImmutableDirectedGraph.empty()
    assertThat(isCyclical(graph)).isFalse()
  }

  @Test
  fun isCyclical_whenSingleNode_isFalse() {
    val graph: DirectedGraph<Any> = ImmutableDirectedGraph.builder<Any>().addNode(Any()).build()
    assertThat(isCyclical(graph)).isFalse()
  }

  @Test
  fun isCyclical_whenSingleNode_edgePointsTowardsSelf_isTrue() {
    val graph: DirectedGraph<Int> =
      ImmutableDirectedGraph.builder<Int>().addNode(1).addEdge(1, 1).build()
    assertThat(isCyclical(graph)).isTrue()
  }

  @Test
  fun isCyclical_whenChainOfTwoNodes_isFalse() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addEdge(1, 2)
      .build()
    assertThat(isCyclical(graph)).isFalse()
  }

  @Test
  fun isCyclical_whenLoopOfTwoNodes_isTrue() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addEdge(1, 2)
      .addEdge(2, 1)
      .build()
    assertThat(isCyclical(graph)).isTrue()
  }

  @Test
  fun isCyclical_whenChainOfTenNodes_isFalse() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addNode(8)
      .addNode(9)
      .addNode(10)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .addEdge(4, 5)
      .addEdge(5, 6)
      .addEdge(6, 7)
      .addEdge(7, 8)
      .addEdge(8, 9)
      .addEdge(9, 10)
      .build()
    assertThat(isCyclical(graph)).isFalse()
  }

  @Test
  fun isCyclical_whenLoopOfTenNodes_isTrue() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addNode(8)
      .addNode(9)
      .addNode(10)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .addEdge(4, 5)
      .addEdge(5, 6)
      .addEdge(6, 7)
      .addEdge(7, 8)
      .addEdge(8, 9)
      .addEdge(9, 10)
      .addEdge(10, 1)
      .build()
    assertThat(isCyclical(graph)).isTrue()
  }

  @Test
  fun isCyclical_whenDisjointChains_isFalse() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addNode(8)
      .addNode(9)
      .addNode(10)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .addEdge(4, 5)
      .addEdge(6, 7)
      .addEdge(7, 8)
      .addEdge(8, 9)
      .addEdge(9, 10)
      .build()
    assertThat(isCyclical(graph)).isFalse()
  }

  @Test
  fun findAnyCycle_whenEmpty_isEmpty() {
    assertThat(findAnyCycle(ImmutableDirectedGraph.empty<Int>())).isNull()
  }

  @Test
  fun findAnyCycle_whenSingleNode_isEmpty() {
    assertThat(findAnyCycle(ImmutableDirectedGraph.builder<Int>().addNode(1).build())).isNull()
  }

  @Test
  fun findAnyCycle_whenSingleNode_whenCyclical_containsNode() {
    assertThat(
            findAnyCycle(ImmutableDirectedGraph.builder<Int>().addNode(1).addEdge(1, 1).build()))
        .isEqualTo(ImmutableList.of(1))
  }

  @Test
  fun findAnyCycle_whenTwoNodes_whenCyclical_containsNodes() {
    assertThat(
            findAnyCycle(
                ImmutableDirectedGraph.builder<Int>()
                    .addNode(1)
                    .addNode(2)
                    .addEdge(1, 2)
                    .addEdge(2, 1)
                    .build()))
        .isEqualTo(ImmutableList.of(1, 2))
  }

  @Test
  fun topologicallySort_whenSingleNode_whenCyclical_throwsException() {
    assertFailsWith(IllegalArgumentException::class) {
      topologicallySort(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addEdge(1, 1)
          .build()
      )
    }
  }

  @Test
  fun topologicallySort_whenLollipop_throwsException() {
    assertFailsWith(IllegalArgumentException::class) {
      topologicallySort(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .addEdge(3, 2)
          .build()
      )
    }
  }

  @Test
  fun topologicallySort_whenCycle_withTwoNodes_throwsException() {
    assertFailsWith(IllegalArgumentException::class) {
      topologicallySort(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addEdge(1, 2)
          .addEdge(2, 1)
          .build()
      )
    }
  }

  @Test
  fun topologicallySort_whenCycle_withThreeNodes_throwsException() {
    assertFailsWith(IllegalArgumentException::class) {
      topologicallySort(
        ImmutableDirectedGraph.builder<Any>()
          .addNode(1)
          .addNode(2)
          .addNode(3)
          .addEdge(1, 2)
          .addEdge(2, 3)
          .addEdge(3, 1)
          .build()
      )
    }
  }

  @Test
  fun topologicallySort_whenEmpty_isEmpty() {
    assertThat(
      topologicallySort(ImmutableDirectedGraph.empty()).isPopulated
    )
      .isFalse()
  }

  @Test
  fun topologicallySort_whenChain_isTopologicallySorted() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .build()
    assertThat(topologicallySort(graph)).isATopologicalSortOf(graph)
  }

  @Test
  fun topologicallySort_whenForked_isTopologicallySorted() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .addEdge(2, 5)
      .addEdge(5, 6)
      .build()
    assertThat(topologicallySort(graph)).isATopologicalSortOf(graph)
  }

  @Test
  fun topologicallySort_whenMerged_isTopologicallySorted() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addEdge(1, 2)
      .addEdge(2, 5)
      .addEdge(3, 4)
      .addEdge(4, 5)
      .addEdge(5, 6)
      .build()
    assertThat(topologicallySort(graph)).isATopologicalSortOf(graph)
  }

  @Test
  fun topologicallySort_whenForkedAndMerged_isTopologicallySorted() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addNode(8)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(2, 5)
      .addEdge(3, 4)
      .addEdge(5, 6)
      .addEdge(4, 7)
      .addEdge(6, 7)
      .addEdge(7, 8)
      .build()
    assertThat(topologicallySort(graph)).isATopologicalSortOf(graph)
  }

  @Test
  fun topologicallySort_whenDisconnected_isTopologicallySorted() {
    val graph: DirectedGraph<Int> = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addNode(8)
      .build()
    assertThat(topologicallySort(graph)).isATopologicalSortOf(graph)
  }

  @Test
  fun findOtherNodesInSubgraphContaining_returnsNeighboringNodesAndSelf() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(4, 5)
      .addEdge(5, 6)
      .build()

    assertThat(findOtherNodesInSubgraphContaining(graph.nodeOf(2)!!))
      .containsExactlyElementsIn(nodesFor(graph, 1, 2, 3))
  }

  @Test
  fun findOtherNodesInSubgraphContaining_whenDisconnected_returnsOnlyItem() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .build()

    assertThat(findOtherNodesInSubgraphContaining(graph.nodeOf(2)!!))
      .containsExactlyElementsIn(nodesFor(graph, 2))
  }

  @Test
  fun findOtherNodesInSubgraphContaining_returnsItemsTwiceRemoved() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .build()

    assertThat(findOtherNodesInSubgraphContaining(graph.nodeOf(1)!!))
      .containsExactlyElementsIn(nodesFor(graph, 1, 2, 3, 4))
  }

  private fun <T : Any, G : Graph<T>, N : Graph.Node<T>> nodesFor(graph: G, vararg items: T):
      ImmutableList<N>  {
    @Suppress("UNCHECKED_CAST")
    return items.asIterable().map { graph.nodeOf(it) as N }.toImmutableList()
  }

  @Test
  fun findAllPredecessorsOf_whenEmpty_returnsEmpty() {
    assertThat(GraphAlgorithms.findAllPredecessorsOf<DirectedGraph.DirectedNode<Int>>(emptyList())).isEmpty()
  }

  @Test
  fun findAllPredecessorsOf_returnsPredecessors() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addEdge(1, 3)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .build()

    assertThat(GraphAlgorithms.findAllPredecessorsOf(listOf(graph.nodeOf(3)!!)))
      .containsExactlyElementsIn(listOf(1, 2, 3).map { graph.nodeOf(it)!! })
  }

  @Test
  fun findAllPredecessorsOf_withFilter_returnsTrimmedPredecessors() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .build()

    assertThat(GraphAlgorithms.findAllPredecessorsOf(listOf(graph.nodeOf(4)!!)) { it.item != 2 })
      .containsExactlyElementsIn(listOf(3, 4).map { graph.nodeOf(it)!! })
  }

  @Test
  fun findAllSuccessorsOf_whenEmpty_returnsEmpty() {
    assertThat(GraphAlgorithms.findAllSuccessorsOf<DirectedGraph.DirectedNode<Int>>(emptyList())).isEmpty()
  }

  @Test
  fun findAllSuccessorsOf_returnsSuccessors() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(2, 4)
      .build()

    assertThat(GraphAlgorithms.findAllSuccessorsOf(listOf(graph.nodeOf(2)!!)))
      .containsExactlyElementsIn(listOf(2, 3, 4).map { graph.nodeOf(it)!! })
  }

  @Test
  fun findAllSuccessorsOf_withFilter_returnsTrimmedSuccessors() {
    val graph = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)
      .build()

    assertThat(GraphAlgorithms.findAllSuccessorsOf(listOf(graph.nodeOf(1)!!)) { it.item != 3 })
      .containsExactlyElementsIn(listOf(1, 2).map { graph.nodeOf(it)!! })
  }
}

private fun <T : Any, C : List<T>> Assertion<C>.isATopologicalSortOf(graph: DirectedGraph<T>):
    Assertion<C> {
  this.hasCount(graph.nodes.count)

  // O(N)
  for (item in actual) {
    assertThat(graph.nodeOf(item)).isNotNull()
  }
  val cumulativePredecessors: MutableSet<T> = HashSet.create()

  // O(N^2)
  for (item in actual) {

    // predecessors of item exist in cumulative predecessors: O(N)
    (graph.nodeOf(item)?.predecessors ?: Set.empty())
      .map { it.item }
      .forEach { assertThat(cumulativePredecessors).contains(it) }

    // successors of item do NOT exist in cumulative predecessors yet: O(N)
    (graph.nodeOf(item)?.successors ?: Set.empty())
      .map { it.item }
      .forEach { assertThat(cumulativePredecessors).doesNotContain(it) }

    cumulativePredecessors.add(item)
  }

  // all nodes in graph exist in the result: O(N)
  for (node in graph.nodes) {
    assertThat(cumulativePredecessors).contains(node.item)
  }

  return this
}
