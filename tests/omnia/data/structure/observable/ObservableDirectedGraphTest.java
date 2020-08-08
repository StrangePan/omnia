package omnia.data.structure.observable;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.reactivex.subscribers.TestSubscriber;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.DirectedGraph.DirectedEdge;
import omnia.data.structure.DirectedGraph.DirectedNode;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.observable.ObservableDirectedGraph.MutationEvent;
import omnia.data.structure.observable.ObservableGraph.AddEdgeToGraph;
import omnia.data.structure.observable.ObservableGraph.AddNodeToGraph;
import omnia.data.structure.observable.ObservableGraph.RemoveEdgeFromGraph;
import omnia.data.structure.observable.ObservableGraph.RemoveNodeFromGraph;
import omnia.data.structure.observable.writable.WritableObservableDirectedGraph;
import omnia.data.structure.tuple.Couplet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ObservableDirectedGraphTest {

  @Test
  public void contents_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create().contents().isPopulated()).isFalse();
  }

  @Test
  public void nodes_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create().nodes().isPopulated()).isFalse();
  }

  @Test
  public void edges_whenInit_areEmpty() {
    assertThat(WritableObservableDirectedGraph.create().edges().isPopulated()).isFalse();
  }

  @Test
  public void addNode_whenNull_throwsException() {
    assertThrows(NullPointerException.class, () -> WritableObservableDirectedGraph.create().addNode(null));
  }

  @Test
  public void addNode_whenInit_contentsHasOneItem() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);

    assertThat(graph.contents().count()).isEqualTo(1);
  }

  @Test
  public void addNode_isInContents() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);

    assertThat(graph.contents().contains(item)).isTrue();
  }

  @Test
  public void addNode_whenInit_nodesHasOneNode() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);

    assertThat(graph.nodes().count()).isEqualTo(1);
  }

  @Test
  public void addNode_isInNodes() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);

    assertThat(graph.nodes().stream().findFirst().map(DirectedNode::item)).hasValue(item);
  }

  @Test
  public void addEdge_unrecognizedEdges_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> WritableObservableDirectedGraph.create().addEdge(new Object(), new Object()));
  }

  @Test
  public void addEdges_edgesContainsNewEdge() {
    Object item1 = new Object();
    Object item2 = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item1);
    graph.addNode(item2);
    graph.addEdge(item1, item2);

    assertThat(
        graph.edges().stream().findFirst().map(edge -> edge.endpoints().map(DirectedNode::item)))
        .hasValue(Couplet.of(item1, item2));
  }

  @Test
  public void replaceNode_whenDoesNotContainOriginal_throwsException() {
    Object original = new Object();
    Object replacement = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    assertThrows(IllegalArgumentException.class, () -> graph.replaceNode(original, replacement));
  }

  @Test
  public void replaceNode_whenContainsReplacement_throwsException() {
    Object original = new Object();
    Object replacement = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(original);
    graph.addNode(replacement);

    assertThrows(IllegalArgumentException.class, () -> graph.replaceNode(original, replacement));
  }

  @Test
  public void replaceNode_contentsContainsReplacementOnly() {
    Object original = new Object();
    Object replacement = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(original);
    graph.replaceNode(original, replacement);

    assertThat(graph.contents()).containsExactlyElementsIn(ImmutableSet.of(replacement));
  }

  @Test
  public void replaceNode_withEdges_edgesContainsReplacementEdgesOnly() {
    Object original = new Object();
    Object replacement = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(original);
    graph.addEdge(original, original);
    graph.replaceNode(original, replacement);

    assertThat(
        graph.edges().stream()
            .map(DirectedEdge::endpoints)
            .map(pair -> pair.map(DirectedNode::item)))
        .containsExactlyElementsIn(ImmutableSet.of(Couplet.of(replacement, replacement)));
  }

  @Test
  public void observeStates_whenInit_emitsEmpty() {
    WritableObservableDirectedGraph.create().observe().states().test()
        .assertValue(state -> !state.contents().isPopulated());
  }

  @Test
  public void observeMutations_whenInit_emitsEmptyState() {
    WritableObservableDirectedGraph.create().observe().mutations().map(MutationEvent::state).test()
        .assertValue(state -> !state.contents().isPopulated());
  }

  @Test
  public void observeMutations_whenInit_emitsNoOperations() {
    WritableObservableDirectedGraph.create().observe().mutations().map(MutationEvent::operations).test()
        .assertValue(operations -> !operations.isPopulated());
  }

  @Test
  public void observeMutations_whenHasNode_emitsStateWithNode() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);

    graph.observe().mutations().map(MutationEvent::state).test()
        .assertValue(state -> state.contents().contains(item));
  }

  @Test
  public void observeMutations_whenHasNode_emitsAddNodeOperation() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);
    TestSubscriber<? extends MutationEvent<Object>> subscriber = graph.observe().mutations().test();

    subscriber.assertValue(event -> event.operations().count() == 1);
    subscriber.assertValue(
        event -> event.operations().stream().findFirst().get() instanceof AddNodeToGraph);
    subscriber.assertValue(
        event -> event.operations().stream()
            .findFirst()
            .map(op -> (AddNodeToGraph<Object>) op)
            .get()
            .item()
            .equals(item));
  }

  @Test
  public void observeMutations_whenHasEdge_emitsStateWithEdge() {
    Object item = new Object();
    Couplet<Object> edge = Couplet.of(item, item);
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);
    graph.addEdge(edge.first(), edge.second());

    TestSubscriber<DirectedGraph<Object>> subscriber =
        graph.observe().mutations().map(MutationEvent::state).test();

    subscriber.assertValue(state -> state.edges().count() == 1);
    subscriber.assertValue(
        state ->
            state.edges().stream()
                .findFirst()
                .map(DirectedEdge::endpoints)
                .get()
                .map(DirectedNode::item)
                .equals(edge));
  }

  @Test
  public void observeMutations_whenHasNode_thenRemoveNode_emitsRemoveNodeOperation() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        graph.observe().mutations().skip(1).test();
    graph.removeNode(item);

    testSubscriber.assertValue(
        event -> event.operations().stream().findFirst().get() instanceof RemoveNodeFromGraph);
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .findFirst()
            .map(op -> (RemoveNodeFromGraph<Object>) op)
            .get()
            .item()
            .equals(item));
  }

  @Test
  public void observeMutations_whenHasEdge_thenRemoveEdge_emitsRemoveEdgeOperation() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);
    graph.addEdge(item, item);
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        graph.observe().mutations().skip(1).test();
    graph.removeEdge(item, item);

    testSubscriber.assertValue(event -> event.operations().count() == 1);
    testSubscriber.assertValue(
        event -> event.operations().stream().findFirst().get() instanceof RemoveEdgeFromGraph);
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .findFirst()
            .map(op -> (RemoveEdgeFromGraph<Object>) op)
            .get()
            .endpoints()
            .equals(Couplet.of(item, item)));
  }

  @Test
  public void observeMutations_whenHasEdge_thenRemoveNode_emitsRemoveEdgeOperation() {
    Object item = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(item);
    graph.addEdge(item, item);
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        graph.observe().mutations().skip(1).test();
    graph.removeNode(item);

    testSubscriber.assertValue(event -> event.operations().count() == 2);
    testSubscriber.assertValue(
        event ->
            event.operations().stream().filter(op -> op instanceof RemoveEdgeFromGraph).count()
                == 1);
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .filter(op -> op instanceof RemoveEdgeFromGraph)
            .map(op -> (RemoveEdgeFromGraph<Object>) op)
            .findFirst()
            .get()
            .endpoints()
            .equals(Couplet.of(item, item)));
  }

  @Test
  public void observeMutations_whenHasNode_thenReplaceNode_emitsRemoveAndAddNodeOperations() {
    Object original = new Object();
    Object replacement = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(original);
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        graph.observe().mutations().skip(1).test();
    graph.replaceNode(original, replacement);

    testSubscriber.assertValue(event -> event.operations().count() == 2);
    testSubscriber.assertValue(
        event ->
            event.operations().stream().filter(op -> op instanceof RemoveNodeFromGraph).count()
                == 1);
    testSubscriber.assertValue(
        event ->
            event.operations().stream().filter(op -> op instanceof AddNodeToGraph).count()
                == 1);
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .filter(op -> op instanceof RemoveNodeFromGraph)
            .map(op -> (RemoveNodeFromGraph<Object>) op)
            .findFirst()
            .get()
            .item()
            .equals(original));
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .filter(op -> op instanceof AddNodeToGraph)
            .map(op -> (AddNodeToGraph<Object>) op)
            .findFirst()
            .get()
            .item()
            .equals(replacement));
  }

  @Test
  public void observeMutations_whenHasEdge_thenReplaceNode_emitsRemoveAndAddEdgeOperations() {
    Object original = new Object();
    Object replacement = new Object();
    WritableObservableDirectedGraph<Object> graph = WritableObservableDirectedGraph.create();

    graph.addNode(original);
    graph.addEdge(original, original);
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        graph.observe().mutations().skip(1).test();
    graph.replaceNode(original, replacement);

    testSubscriber.assertValue(event -> event.operations().count() == 4);
    testSubscriber.assertValue(
        event ->
            event.operations().stream().filter(op -> op instanceof RemoveEdgeFromGraph).count()
                == 1);
    testSubscriber.assertValue(
        event ->
            event.operations().stream().filter(op -> op instanceof AddEdgeToGraph).count()
                == 1);
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .filter(op -> op instanceof RemoveEdgeFromGraph)
            .map(op -> (RemoveEdgeFromGraph<Object>) op)
            .findFirst()
            .get()
            .endpoints()
            .equals(Couplet.of(original, original)));
    testSubscriber.assertValue(
        event -> event.operations().stream()
            .filter(op -> op instanceof AddEdgeToGraph)
            .map(op -> (AddEdgeToGraph<Object>) op)
            .findFirst()
            .get()
            .endpoints()
            .equals(Couplet.of(replacement, replacement)));
  }
}
