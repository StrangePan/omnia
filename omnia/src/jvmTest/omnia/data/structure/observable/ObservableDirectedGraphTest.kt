package omnia.data.structure.observable

import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.skip
import com.badoo.reaktive.test.observable.test
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.data.structure.immutable.ImmutableDirectedGraph.UnknownNodeException
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.observable.writable.WritableObservableDirectedGraph
import omnia.data.structure.tuple.Couplet.Companion.of
import omnia.data.structure.tuple.Tuplet
import omnia.util.reaktive.observable.test.assertValue

class ObservableDirectedGraphTest {

  @Test
  fun contents_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create<Any>().contents().isPopulated).isFalse()
  }

  @Test
  fun nodes_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create<Any>().nodes().isPopulated).isFalse()
  }

  @Test
  fun edges_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create<Any>().edges().isPopulated).isFalse()
  }

  @Test
  fun addNode_whenInit_contentsHasOneItem() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.contents().count()).isEqualTo(1)
  }

  @Test
  fun addNode_isInContents() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.contents().contains(item)).isTrue()
  }

  @Test
  fun addNode_whenInit_nodesHasOneNode() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.nodes().count()).isEqualTo(1)
  }

  @Test
  fun addNode_isInNodes() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    assertThat(graph.nodes().firstOrNull()?.item()).isEqualTo(item)
  }

  @Test
  fun addEdge_unrecognizedEdges_throwsException() {
    assertFailsWith(UnknownNodeException::class) {
      WritableObservableDirectedGraph.create<Any>().addEdge(Any(), Any())
    }
  }

  @Test
  fun addEdges_edgesContainsNewEdge() {
    val item1 = Any()
    val item2 = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item1)
    graph.addNode(item2)
    graph.addEdge(item1, item2)
    assertThat(graph.edges().firstOrNull()?.endpoints()?.map { it.item() })
        .isEqualTo(of(item1, item2))
  }

  @Test
  fun replaceNode_whenDoesNotContainOriginal_throwsException() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    assertFailsWith(IllegalArgumentException::class) {
      graph.replaceNode(original, replacement)
    }
  }

  @Test
  fun replaceNode_whenContainsReplacement_throwsException() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.addNode(replacement)
    assertFailsWith(IllegalArgumentException::class) {
      graph.replaceNode(original, replacement)
    }
  }

  @Test
  fun replaceNode_contentsContainsReplacementOnly() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.replaceNode(original, replacement)
    assertThat(graph.contents()).containsExactlyElementsIn(ImmutableSet.of(replacement))
  }

  @Test
  fun replaceNode_withEdges_edgesContainReplacementEdgesOnly() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.addEdge(original, original)
    graph.replaceNode(original, replacement)
    assertThat(graph.edges().map { edge -> edge.endpoints().map<Any> { it.item() } })
      .containsExactly(of(replacement, replacement))
  }

  @Test
  fun observeStates_whenInit_emitsEmpty() {
    WritableObservableDirectedGraph.create<Any>().observe().states().test()
      .assertValue { !it.contents().isPopulated }
  }

  @Test
  fun observeMutations_whenInit_emitsEmptyState() {
    WritableObservableDirectedGraph.create<Any>().observe().mutations().map { obj -> obj.state() }
      .test()
      .assertValue { !it.contents().isPopulated }
  }

  @Test
  fun observeMutations_whenInit_emitsNoOperations() {
    WritableObservableDirectedGraph.create<Any>().observe().mutations()
      .map { it.operations() }
      .test()
      .assertValue { !it.isPopulated }
  }

  @Test
  fun observeMutations_whenHasNode_emitsStateWithNode() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.observe().mutations().map { it.state() }.test()
      .assertValue { it.contents().contains(item) }
  }

  @Test
  fun observeMutations_whenHasNode_emitsAddNodeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    val subscriber = graph.observe().mutations().test()
    subscriber.assertValue { it.operations().count() == 1 }
    subscriber.assertValue { it.operations().first() is ObservableGraph.AddNodeToGraph<*> }
    subscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.AddNodeToGraph::class.java)
        .first()
        .item() == item
    }
  }

  @Test
  fun observeMutations_whenHasEdge_emitsStateWithEdge() {
    val item = Any()
    val edge = of(item, item)
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(edge.first(), edge.second())
    val subscriber = graph.observe().mutations().map { it.state() }.test()
    subscriber.assertValue { it.edges().count() == 1 }
    subscriber.assertValue { it.edges().first().endpoints().map<Any> { it.item() } == edge }
  }

  @Test
  fun observeMutations_whenHasNode_thenRemoveNode_emitsRemoveNodeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    val testSubscriber = graph.observe().mutations().skip(1).test()
    graph.removeNode(item)
    testSubscriber.assertValue { it.operations().first() is ObservableGraph.RemoveNodeFromGraph<*> }
    testSubscriber.assertValue {
      (it.operations().first() as ObservableGraph.RemoveNodeFromGraph<Any>).item() == item
    }
  }

  @Test
  fun observeMutations_whenHasEdge_thenRemoveEdge_emitsRemoveEdgeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(item, item)
    val testSubscriber = graph.observe().mutations().skip(1).test()
    graph.removeEdge(item, item)
    testSubscriber.assertValue { it.operations().count() == 1 }
    testSubscriber.assertValue { it.operations().first() is ObservableGraph.RemoveEdgeFromGraph<*> }
    testSubscriber.assertValue {
      (it.operations().first() as ObservableGraph.RemoveEdgeFromGraph<Any>).endpoints() ==
          Tuplet.of(item, item)
    }
  }

  @Test
  fun observeMutations_whenHasEdge_thenRemoveNode_emitsRemoveEdgeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(item, item)
    val testSubscriber = graph.observe().mutations().skip(1).test()
    graph.removeNode(item)
    testSubscriber.assertValue { it.operations().count() == 2 }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.RemoveEdgeFromGraph::class.java)
        .count() == 1
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.RemoveEdgeFromGraph::class.java)
        .first()
        .endpoints() == of(item, item)
    }
  }

  @Test
  fun observeMutations_whenHasNode_thenReplaceNode_emitsRemoveAndAddNodeOperations() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    val testSubscriber = graph.observe().mutations().skip(1).test()
    graph.replaceNode(original, replacement)
    testSubscriber.assertValue { it.operations().count() == 2 }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.RemoveNodeFromGraph::class.java)
        .count() == 1
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.AddNodeToGraph::class.java)
        .count() == 1
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.RemoveNodeFromGraph::class.java)
        .first()
        .item() == original
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.AddNodeToGraph::class.java)
        .first()
        .item() == replacement
    }
  }

  @Test
  fun observeMutations_whenHasEdge_thenReplaceNode_emitsRemoveAndAddEdgeOperations() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(original)
    graph.addEdge(original, original)
    val testSubscriber = graph.observe().mutations().skip(1).test()
    graph.replaceNode(original, replacement)
    testSubscriber.assertValue { it.operations().count() == 4 }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.RemoveEdgeFromGraph::class.java)
        .count() == 1
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.AddEdgeToGraph::class.java)
        .count() == 1
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.RemoveEdgeFromGraph::class.java)
        .first()
        .endpoints() == of(original, original)
    }
    testSubscriber.assertValue {
      it.operations()
        .filterIsInstance(ObservableGraph.AddEdgeToGraph::class.java)
        .first()
        .endpoints() == of(replacement, replacement)
    }
  }
}