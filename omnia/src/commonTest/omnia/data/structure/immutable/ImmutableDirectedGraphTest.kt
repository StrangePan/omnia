package omnia.data.structure.immutable

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo

class ImmutableDirectedGraphTest {

  @Test
  fun replaceNode_replacesNodeAndEdges() {
    val underTest = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addEdge(1, 4)
      .addEdge(2, 4)
      .addEdge(3, 4)
      .addEdge(4, 4)
      .addEdge(4, 5)
      .addEdge(4, 6)
      .addEdge(4, 7)
      .replaceNode(4, 20)
      .build()
    
    val expected = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(20)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addEdge(1, 20)
      .addEdge(2, 20)
      .addEdge(3, 20)
      .addEdge(20, 20)
      .addEdge(20, 5)
      .addEdge(20, 6)
      .addEdge(20, 7)
      .build()

    assertThat(underTest).isEqualTo(expected)
  }

  @Test
  fun removeNodeAndConnectNeighbors_removesNodeAndConnectsNeighbors() {
    val underTest = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(4)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addEdge(1, 4)
      .addEdge(2, 4)
      .addEdge(3, 4)
      .addEdge(4, 5)
      .addEdge(4, 6)
      .addEdge(4, 7)
      .removeNodeAndConnectNeighbors(4)
      .build()

    val expected = ImmutableDirectedGraph.builder<Int>()
      .addNode(1)
      .addNode(2)
      .addNode(3)
      .addNode(5)
      .addNode(6)
      .addNode(7)
      .addEdge(1, 5)
      .addEdge(1, 6)
      .addEdge(1, 7)
      .addEdge(2, 5)
      .addEdge(2, 6)
      .addEdge(2, 7)
      .addEdge(3, 5)
      .addEdge(3, 6)
      .addEdge(3, 7)
      .build()

    assertThat(underTest).isEqualTo(expected)
  }
}