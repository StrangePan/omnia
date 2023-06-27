package omnia.data.structure.immutable

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isNotNull
import omnia.util.test.fluent.isNull

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
      .addEdge(4, 5)
      .addEdge(4, 6)
      .addEdge(4, 7)
      .replaceNode(4, 20)
      .build()

    assertThat(underTest.nodeOf(1)).isNotNull()
      .andThat({ node -> node.successors.map { it.item }}) { successors ->
        successors.containsExactly(20)
      }
      .andThat({ node -> node.predecessors }) { predecessors ->
        predecessors.isEmpty()
      }
    assertThat(underTest.nodeOf(2)).isNotNull()
      .andThat({ node -> node.successors.map { it.item }}) { successors ->
        successors.containsExactly(20)
      }
      .andThat({ node -> node.predecessors }) { predecessors ->
        predecessors.isEmpty()
      }
    assertThat(underTest.nodeOf(3)).isNotNull()
      .andThat({ node -> node.successors.map { it.item }}) { successors ->
        successors.containsExactly(20)
      }
      .andThat({ node -> node.predecessors }) { predecessors ->
        predecessors.isEmpty()
      }
    assertThat(underTest.nodeOf(5)).isNotNull()
      .andThat({ node -> node.successors }) { successors ->
        successors.isEmpty()
      }
      .andThat({ node -> node.predecessors.map { it.item }}) { predecessors ->
        predecessors.containsExactly(20)
      }
    assertThat(underTest.nodeOf(6)).isNotNull()
      .andThat({ node -> node.successors }) { successors ->
        successors.isEmpty()
      }
      .andThat({ node -> node.predecessors.map { it.item }}) { predecessors ->
        predecessors.containsExactly(20)
      }
    assertThat(underTest.nodeOf(7)).isNotNull()
      .andThat({ node -> node.successors }) { successors ->
        successors.isEmpty()
      }
      .andThat({ node -> node.predecessors.map { it.item }}) { predecessors ->
        predecessors.containsExactly(20)
      }
    assertThat(underTest.nodeOf(4)).isNull()
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

    assertThat(underTest.nodeOf(1)).isNotNull()
      .andThat { node -> node.successors.map { it.item }}
      .containsExactly(5, 6, 7)
    assertThat(underTest.nodeOf(2)).isNotNull()
      .andThat { node -> node.successors.map { it.item }}
      .containsExactly(5, 6, 7)
    assertThat(underTest.nodeOf(3)).isNotNull()
      .andThat { node -> node.successors.map { it.item }}
      .containsExactly(5, 6, 7)
    assertThat(underTest.nodeOf(5)).isNotNull()
      .andThat { node -> node.predecessors.map { it.item }}
      .containsExactly(1, 2, 3)
    assertThat(underTest.nodeOf(6)).isNotNull()
      .andThat { node -> node.predecessors.map { it.item }}
      .containsExactly(1, 2, 3)
    assertThat(underTest.nodeOf(7)).isNotNull()
      .andThat { node -> node.predecessors.map { it.item }}
      .containsExactly(1, 2, 3)
    assertThat(underTest.nodeOf(4)).isNull()
  }
}