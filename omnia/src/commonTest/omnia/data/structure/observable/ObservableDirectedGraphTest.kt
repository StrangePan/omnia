package omnia.data.structure.observable

import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.skip
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.data.structure.immutable.ImmutableDirectedGraph.UnknownNodeException
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.observable.writable.WritableObservableDirectedGraph
import omnia.data.structure.tuple.Tuplet
import omnia.util.reaktive.observable.test.assertThatValue
import omnia.util.reaktive.observable.test.assertValueCount
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.assertThatCode
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.containsExactlyElementsIn
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isA
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isTrue

class ObservableDirectedGraphTest {

  @Test
  fun contents_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create<Any>().contents.isPopulated).isFalse()
  }

  @Test
  fun nodes_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create<Any>().nodes.isPopulated).isFalse()
  }

  @Test
  fun edges_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create<Any>().edges.isPopulated).isFalse()
  }

  @Test
  fun addNode_whenInit_contentsHasOneItem() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.contents).hasCount(1)
  }

  @Test
  fun addNode_isInContents() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.contents.contains(item)).isTrue()
  }

  @Test
  fun addNode_whenInit_nodesHasOneNode() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.nodes).hasCount(1)
  }

  @Test
  fun addNode_isInNodes() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.nodes.firstOrNull()?.item).isEqualTo(item)
  }

  @Test
  fun addEdge_unrecognizedEdges_throwsException() {
    assertThatCode {
      WritableObservableDirectedGraph.create<Any>().addEdge(Any(), Any())
    }.failsWith(UnknownNodeException::class)
  }

  @Test
  fun addEdges_edgesContainsNewEdge() {
    val item1 = Any()
    val item2 = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item1)
    graph.addNode(item2)
    graph.addEdge(item1, item2)
    assertThat(graph.edges.firstOrNull()?.endpoints?.map { it.item })
        .isEqualTo(Tuplet.of(item1, item2))
  }

  @Test
  fun replaceNode_whenDoesNotContainOriginal_throwsException() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    assertThatCode {
      graph.replaceNode(original, replacement)
    }.failsWith(IllegalArgumentException::class)
  }

  @Test
  fun replaceNode_whenContainsReplacement_throwsException() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.addNode(replacement)
    assertThatCode {
      graph.replaceNode(original, replacement)
    }.failsWith(IllegalArgumentException::class)
  }

  @Test
  fun replaceNode_contentsContainsReplacementOnly() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.replaceNode(original, replacement)
    assertThat(graph.contents).containsExactlyElementsIn(ImmutableSet.of(replacement))
  }

  @Test
  fun replaceNode_withEdges_edgesContainReplacementEdgesOnly() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.addEdge(original, original)
    graph.replaceNode(original, replacement)
    assertThat(graph.edges.map { edge -> edge.endpoints.map { it.item } })
      .containsExactly(Tuplet.of(replacement, replacement))
  }

  @Test
  fun observeStates_whenInit_emitsEmpty() {
    WritableObservableDirectedGraph.create<Any>().observables.states.test()
        .assertThatValue { it.contents }
        .isEmpty()
  }

  @Test
  fun observeMutations_whenInit_emitsEmptyState() {
    WritableObservableDirectedGraph.create<Any>().observables.mutations.test()
        .assertThatValue { it.state.contents }
        .isEmpty()
  }

  @Test
  fun observeMutations_whenInit_emitsNoOperations() {
    WritableObservableDirectedGraph.create<Any>().observables.mutations.test()
        .assertThatValue { it.operations }
        .isEmpty()
  }

  @Test
  fun observeMutations_whenHasNode_emitsStateWithNode() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.observables.mutations.test().assertThatValue { it.state.contents }.contains(item)
  }

  @Test
  fun observeMutations_whenHasNode_emitsAddNodeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    val subscriber = graph.observables.mutations.test()
    subscriber.assertThatValue { it.operations }.hasCount(1)
        .andThat { it.first() }.isA(ObservableGraph.AddNodeToGraph::class)
        .andThat { it.item }.isEqualTo(item)
  }

  @Test
  fun observeMutations_whenHasEdge_emitsStateWithEdge() {
    val item = Any()
    val edge = Tuplet.of(item, item)
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(edge.first, edge.second)
    graph.observables.states.test().assertValueCount(1)
    graph.observables.mutations.test().assertValueCount(1)
    val subscriber = graph.observables.mutations.map { it.state.edges }.test()
    subscriber.assertThatValue().hasCount(1)
        .andThat { it.first().endpoints.map { endpoint -> endpoint.item} }
        .isEqualTo(edge)
  }

  @Test
  fun observeMutations_whenHasNode_thenRemoveNode_emitsRemoveNodeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    val testSubscriber = graph.observables.mutations.skip(1).test()
    graph.removeNode(item)
    testSubscriber.assertThatValue { it.operations.first() }
        .isA(ObservableGraph.RemoveNodeFromGraph::class)
        .andThat { it.item }.isEqualTo(item)
  }

  @Test
  fun observeMutations_whenHasEdge_thenRemoveEdge_emitsRemoveEdgeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(item, item)
    val testSubscriber = graph.observables.mutations.skip(1).test()
    graph.removeEdge(item, item)
    testSubscriber.assertThatValue { it.operations }.hasCount(1)
        .andThat { it.first() }.isA(ObservableGraph.RemoveEdgeFromGraph::class)
        .andThat { it.endpoints }.isEqualTo(Tuplet.of(item, item))
  }

  @Test
  fun observeMutations_whenHasEdge_thenRemoveNode_emitsRemoveEdgeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(item, item)
    val testSubscriber = graph.observables.mutations.skip(1).test()
    graph.removeNode(item)
    testSubscriber.assertThatValue { it.operations }.hasCount(2)
        .andThat { it.filterIsInstance<ObservableGraph.RemoveEdgeFromGraph<*>>() }.hasCount(1)
        .andThat { it.first().endpoints }.isEqualTo(Tuplet.of(item, item))
  }

  @Test
  fun observeMutations_whenHasNode_thenReplaceNode_emitsRemoveAndAddNodeOperations() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    val testSubscriber = graph.observables.mutations.skip(1).test()
    graph.replaceNode(original, replacement)
    testSubscriber.assertThatValue { it.operations }
        .hasCount(2)
        .andThat({ it.filterIsInstance<ObservableGraph.RemoveNodeFromGraph<*>>() }) {
          it.hasCount(1).andThat { it.first().item }.isEqualTo(original)
        }
        .andThat({ it.filterIsInstance<ObservableGraph.AddNodeToGraph<*>>() }) {
          it.hasCount(1).andThat { it.first().item }.isEqualTo(replacement)
        }
  }

  @Test
  fun observeMutations_whenHasEdge_thenReplaceNode_emitsRemoveAndAddEdgeOperations() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.addEdge(original, original)
    val testSubscriber = graph.observables.mutations.skip(1).test()
    graph.replaceNode(original, replacement)
    testSubscriber.assertThatValue { it.operations }
        .hasCount(4)
        .andThat({ it.filterIsInstance<ObservableGraph.RemoveEdgeFromGraph<*>>() }) {
          it.hasCount(1).andThat { it.first().endpoints }.isEqualTo(Tuplet.of(original, original))
        }
        .andThat({ it.filterIsInstance<ObservableGraph.AddEdgeToGraph<*>>() }) {
          it.hasCount(1).andThat { it.first().endpoints }.isEqualTo(Tuplet.of(replacement, replacement))
        }
  }
}
