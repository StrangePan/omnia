package omnia.algorithm

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth8
import java.util.Arrays
import java.util.Optional
import omnia.algorithm.GraphAlgorithms.findOtherNodesInSubgraphContaining
import omnia.algorithm.GraphAlgorithms.isCyclical
import omnia.algorithm.GraphAlgorithms.isolatedElements
import omnia.algorithm.GraphAlgorithms.sinkElements
import omnia.algorithm.GraphAlgorithms.sourceElements
import omnia.algorithm.GraphAlgorithms.topologicallySort
import omnia.data.stream.Collectors.toList
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Graph
import omnia.data.structure.List
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableDirectedGraph
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.mutable.HashSet
import omnia.data.structure.mutable.MutableSet
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

class GraphAlgorithmsTest {
  @RunWith(JUnit4::class)
  class GraphAlgorithms_IsolatedElementsTest {

    @Test
    fun isolatedElements_whenGraphEmpty_returnsEmpty() {
      assertThat(isolatedElements(ImmutableDirectedGraph.empty<Any?>())).isEmpty()
    }

    @Test
    fun isolatedElements_whenAllIsolated_returnsAll() {
      assertThat(
        isolatedElements(
          ImmutableDirectedGraph.builder<Int?>().addNode(1).addNode(2).addNode(3).build()
        )
      )
        .containsExactlyElementsIn(ImmutableSet.of<Int?>(1, 2, 3))
    }

    @Test
    fun isolatedElements_whenAllConnected_returnsNone() {
      assertThat(
        isolatedElements(
          ImmutableDirectedGraph.builder<Int?>()
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
  }

  @RunWith(JUnit4::class)
  class GraphAlgorithms_SourceElementsTest {

    @Test
    fun sourceElements_whenGraphEmpty_returnsEmpty() {
      assertThat(sourceElements(ImmutableDirectedGraph.empty<Any?>())).isEmpty()
    }

    @Test
    fun sourceElements_whenGraphHasOneSource_returnsSource() {
      assertThat(
        sourceElements(
          ImmutableDirectedGraph.builder<Any?>()
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
          ImmutableDirectedGraph.builder<Any?>()
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
  }

  @RunWith(JUnit4::class)
  class GraphAlgorithms_SinkElements {

    @Test
    fun sinkElements_whenGraphEmpty_returnsEmpty() {
      assertThat(sinkElements(ImmutableDirectedGraph.empty<Any?>())).isEmpty()
    }

    @Test
    fun sinkElements_whenGraphHasOneSink_returnsSink() {
      assertThat(
        sinkElements(
          ImmutableDirectedGraph.builder<Any?>()
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
          ImmutableDirectedGraph.builder<Any?>()
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
  }

  @RunWith(JUnit4::class)
  class GraphAlgorithms_IsCyclicalTest {

    @Test
    fun isCyclical_whenEmpty_isFalse() {
      val graph: DirectedGraph<Any?> = ImmutableDirectedGraph.empty()
      assertThat(isCyclical(graph)).isFalse()
    }

    @Test
    fun isCyclical_whenSingleNode_isFalse() {
      val graph: DirectedGraph<Any?> = ImmutableDirectedGraph.builder<Any?>().addNode(Any()).build()
      assertThat(isCyclical(graph)).isFalse()
    }

    @Test
    fun isCyclical_whenSingleNode_edgePointsTowardsSelf_isTrue() {
      val graph: DirectedGraph<Int?> =
        ImmutableDirectedGraph.builder<Int?>().addNode(1).addEdge(1, 1).build()
      assertThat(isCyclical(graph)).isTrue()
    }

    @Test
    fun isCyclical_whenChainOfTwoNodes_isFalse() {
      val graph: DirectedGraph<Int?> = ImmutableDirectedGraph.builder<Int?>()
        .addNode(1)
        .addNode(2)
        .addEdge(1, 2)
        .build()
      assertThat(isCyclical(graph)).isFalse()
    }

    @Test
    fun isCyclical_whenLoopOfTwoNodes_isTrue() {
      val graph: DirectedGraph<Int?> = ImmutableDirectedGraph.builder<Int?>()
        .addNode(1)
        .addNode(2)
        .addEdge(1, 2)
        .addEdge(2, 1)
        .build()
      assertThat(isCyclical(graph)).isTrue()
    }

    @Test
    fun isCyclical_whenChainOfTenNodes_isFalse() {
      val graph: DirectedGraph<Int?> = ImmutableDirectedGraph.builder<Int?>()
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
      val graph: DirectedGraph<Int?> = ImmutableDirectedGraph.builder<Int?>()
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
      val graph: DirectedGraph<Int?> = ImmutableDirectedGraph.builder<Int?>()
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
  }

  @RunWith(JUnit4::class)
  class GraphAlgorithms_TopologicallySortTest {

    @Test
    fun topologicallySort_whenSingleNode_whenCyclical_throwsException() {
      assertThrows(
        IllegalArgumentException::class.java
      ) {
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
      assertThrows(
        IllegalArgumentException::class.java
      ) {
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
      assertThrows(
        IllegalArgumentException::class.java
      ) {
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
      assertThrows(
        IllegalArgumentException::class.java
      ) {
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
        topologicallySort(ImmutableDirectedGraph.empty<Any>()).isPopulated
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

    private interface TopologicalAssertion<T> {

      fun isATopologicalSortOf(graph: DirectedGraph<T>)
    }

    companion object {

      private fun <T> assertThat(list: List<T>): TopologicalAssertion<T> {
        // O(N^2)
        return object : TopologicalAssertion<T> {
          override fun isATopologicalSortOf(graph: DirectedGraph<T>) {
            assertThat(list.count()).isEqualTo(graph.nodes().count())

            // O(N)
            for (item in list) {
              Truth8.assertThat(graph.nodeOf(item)).isPresent()
            }
            val cumulativePredecessors: MutableSet<T> = HashSet.create()

            // O(N^2)
            for (item in list) {

              // predecessors of item exist in cumulative predecessors: O(N)
              graph.nodeOf(item)
                .map { obj: DirectedGraph.DirectedNode<*> -> obj.predecessors() }
                .orElse(Set.empty())
                .stream()
                .map { obj: DirectedGraph.DirectedNode<*> -> obj.item() }
                .forEach { directPredecessor ->
                  assertThat(cumulativePredecessors.contains(directPredecessor))
                    .isTrue()
                }

              // successors of item do NOT exist in cumulative predecessors yet: O(N)
              graph.nodeOf(item)
                .map { obj: DirectedGraph.DirectedNode<*> -> obj.successors() }
                .orElse(Set.empty())
                .stream()
                .map { obj: DirectedGraph.DirectedNode<*> -> obj.item() }
                .forEach { directPredecessor ->
                  assertThat(directPredecessor in cumulativePredecessors)
                    .isFalse()
                }
              cumulativePredecessors.add(item)
            }

            // all nodes in graph exist in the result: O(N)
            for (node in graph.nodes()) {
              assertThat(cumulativePredecessors.contains(node.item())).isTrue()
            }
          }
        }
      }
    }
  }

  @RunWith(JUnit4::class)
  class GraphAlgorithms_FindOtherNodesInSubgraphContainingTest {

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

      assertThat(findOtherNodesInSubgraphContaining(graph.nodeOf(2).orElseThrow()))
        .containsExactlyElementsIn(nodesFor(graph, 1, 2, 3));
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

      assertThat(findOtherNodesInSubgraphContaining(graph.nodeOf(2).orElseThrow()))
        .containsExactlyElementsIn(nodesFor(graph, 2));
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

      assertThat(findOtherNodesInSubgraphContaining(graph.nodeOf(1).orElseThrow()))
        .containsExactlyElementsIn(nodesFor(graph, 1, 2, 3, 4));
    }

    private fun <T> nodesFor(graph: Graph<T>, vararg items: T): List<Graph.Node<T>>  {
      return Arrays.stream(items)
        .map(graph::nodeOf)
        .flatMap(Optional<out Graph.Node<T>>::stream)
        .collect(toList())
    }
  }
}