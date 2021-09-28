package omnia.data.structure.observable

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth8.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.data.structure.DirectedGraph
import omnia.data.structure.immutable.ImmutableDirectedGraph.UnknownNodeException
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.observable.ObservableGraph.GraphOperation
import omnia.data.structure.observable.writable.WritableObservableDirectedGraph
import omnia.data.structure.tuple.Couplet.Companion.of

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
    assertThat(
      graph.nodes().stream().findFirst().map { obj: DirectedGraph.DirectedNode<*> -> obj.item() })
      .hasValue(item)
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
    assertThat(
      graph.edges().stream().findFirst().map { edge: DirectedGraph.DirectedEdge<Any>? ->
        edge!!.endpoints().map<Any> { obj: DirectedGraph.DirectedNode<*> -> obj.item()!! }
      })
      .hasValue(of(item1, item2))
  }

  @Test
  fun replaceNode_whenDoesNotContainOriginal_throwsException() {
    val original = Any()
    val replacement = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    assertFailsWith(IllegalArgumentException::class) {
      graph.replaceNode(
        original,
        replacement
      )
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
      graph.replaceNode(
        original,
        replacement
      )
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
    assertThat(
      graph.edges().stream()
        .map { obj: DirectedGraph.DirectedEdge<*> -> obj.endpoints() }
        .map { couplet -> couplet.map<Any> { obj -> obj.item() } })
      .containsExactlyElementsIn(ImmutableSet.of(of(replacement, replacement)))
  }

  @Test
  fun observeStates_whenInit_emitsEmpty() {
    WritableObservableDirectedGraph.create<Any>().observe().states().test()
      .assertValue { state: DirectedGraph<Any> -> !state.contents().isPopulated }
  }

  @Test
  fun observeMutations_whenInit_emitsEmptyState() {
    WritableObservableDirectedGraph.create<Any>().observe().mutations().map { obj -> obj.state() }
      .test()
      .assertValue { state: DirectedGraph<Any> -> !state.contents().isPopulated }
  }

  @Test
  fun observeMutations_whenInit_emitsNoOperations() {
    WritableObservableDirectedGraph.create<Any>().observe().mutations()
      .map { obj -> obj.operations() }.test()
      .assertValue { operations -> !operations.isPopulated }
  }

  @Test
  fun observeMutations_whenHasNode_emitsStateWithNode() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.observe().mutations().map { obj -> obj.state() }.test()
      .assertValue { state -> state.contents().contains(item) }
  }

  @Test
  fun observeMutations_whenHasNode_emitsAddNodeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    val subscriber = graph.observe().mutations().test()
    subscriber.assertValue { event -> event.operations().count() == 1 }
    subscriber.assertValue { event ->
      event.operations().stream().findFirst().get() is ObservableGraph.AddNodeToGraph<*>
    }
    subscriber.assertValue { event ->
      (event.operations().stream()
        .findFirst()
        .map { op: GraphOperation<Any>? -> op as ObservableGraph.AddNodeToGraph<Any>? }
        .get()
        .item()
          == item)
    }
  }

  @Test
  fun observeMutations_whenHasEdge_emitsStateWithEdge() {
    val item = Any()
    val edge = of(item, item)
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    graph.addEdge(edge.first(), edge.second())
    val subscriber = graph.observe().mutations().map { obj -> obj.state() }.test()
    subscriber.assertValue { state -> state.edges().count() == 1 }
    subscriber.assertValue { state ->
      (state.edges().stream()
        .findFirst()
        .map { obj -> obj.endpoints() }
        .get()
        .map<Any> { obj -> obj.item() }
          == edge)
    }
  }

  @Test
  fun observeMutations_whenHasNode_thenRemoveNode_emitsRemoveNodeOperation() {
    val item = Any()
    val graph = WritableObservableDirectedGraph.create<Any>()
    graph.addNode(item)
    val testSubscriber = graph.observe().mutations().skip(1).test()
    graph.removeNode(item)
    testSubscriber.assertValue { event ->
      event.operations().stream().findFirst().get() is ObservableGraph.RemoveNodeFromGraph<*>
    }
    testSubscriber.assertValue { event ->
      (event.operations().stream()
        .findFirst()
        .map { op -> op as ObservableGraph.RemoveNodeFromGraph<Any>? }
        .get()
        .item()
          == item)
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
    testSubscriber.assertValue { event -> event.operations().count() == 1 }
    testSubscriber.assertValue { event ->
      event.operations().stream().findFirst().get() is ObservableGraph.RemoveEdgeFromGraph<*>
    }
    testSubscriber.assertValue { event ->
      (event.operations().stream()
        .findFirst()
        .map { op -> op as ObservableGraph.RemoveEdgeFromGraph<Any> }
        .get()
        .endpoints()
          == of(item, item))
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
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      event!!.operations().count() == 2
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.RemoveEdgeFromGraph<*> }.count()
          == 1L)
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.RemoveEdgeFromGraph<*> }
        .map { op: GraphOperation<Any>? -> op as ObservableGraph.RemoveEdgeFromGraph<Any>? }
        .findFirst()
        .get()
        .endpoints()
          == of(item, item))
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
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      event!!.operations().count() == 2
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.RemoveNodeFromGraph<*> }.count()
          == 1L)
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.AddNodeToGraph<*> }.count()
          == 1L)
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.RemoveNodeFromGraph<*> }
        .map { op: GraphOperation<Any>? -> op as ObservableGraph.RemoveNodeFromGraph<Any>? }
        .findFirst()
        .get()
        .item()
          == original)
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.AddNodeToGraph<*> }
        .map { op: GraphOperation<Any>? -> op as ObservableGraph.AddNodeToGraph<Any>? }
        .findFirst()
        .get()
        .item()
          == replacement)
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
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      event!!.operations().count() == 4
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.RemoveEdgeFromGraph<*> }.count()
          == 1L)
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.AddEdgeToGraph<*> }.count()
          == 1L)
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.RemoveEdgeFromGraph<*> }
        .map { op: GraphOperation<Any>? -> op as ObservableGraph.RemoveEdgeFromGraph<Any>? }
        .findFirst()
        .get()
        .endpoints()
          == of(original, original))
    }
    testSubscriber.assertValue { event: ObservableDirectedGraph.MutationEvent<Any>? ->
      (event!!.operations().stream()
        .filter { op: GraphOperation<Any>? -> op is ObservableGraph.AddEdgeToGraph<*> }
        .map { op: GraphOperation<Any>? -> op as ObservableGraph.AddEdgeToGraph<Any>? }
        .findFirst()
        .get()
        .endpoints()
          == of(replacement, replacement))
    }
  }
}