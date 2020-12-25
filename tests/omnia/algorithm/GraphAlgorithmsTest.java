package omnia.algorithm;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.truth.Truth;
import com.google.common.truth.Truth8;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.DirectedGraph.DirectedNode;
import omnia.data.structure.List;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableDirectedGraph;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public final class GraphAlgorithmsTest {

  @RunWith(JUnit4.class)
  public static final class GraphAlgorithms_IsolatedElementsTest {

    @Test
    public void isolatedElements_whenGraphEmpty_returnsEmpty() {
      assertThat(GraphAlgorithms.isolatedElements(ImmutableDirectedGraph.empty())).isEmpty();
    }

    @Test
    public void isolatedElements_whenAllIsolated_returnsAll() {
      assertThat(
          GraphAlgorithms.isolatedElements(
              ImmutableDirectedGraph.<Integer>builder().addNode(1).addNode(2).addNode(3).build()))
          .containsExactlyElementsIn(ImmutableSet.of(1, 2, 3));
    }

    @Test
    public void isolatedElements_whenAllConnected_returnsNone() {
      assertThat(
          GraphAlgorithms.isolatedElements(
              ImmutableDirectedGraph.<Integer>builder()
                  .addNode(1)
                  .addNode(2)
                  .addNode(3)
                  .addEdge(1, 2)
                  .addEdge(2, 3)
                  .build()))
          .isEmpty();
    }
  }

  @RunWith(JUnit4.class)
  public static final class GraphAlgorithms_SourceElementsTest {

    @Test
    public void sourceElements_whenGraphEmpty_returnsEmpty () {
    assertThat(GraphAlgorithms.sourceElements(ImmutableDirectedGraph.empty())).isEmpty();
  }

    @Test
    public void sourceElements_whenGraphHasOneSource_returnsSource () {
    assertThat(
        GraphAlgorithms.sourceElements(
            ImmutableDirectedGraph.builder()
                .addNode(1)
                .addNode(2)
                .addNode(3)
                .addEdge(1, 2)
                .addEdge(2, 3)
                .build()))
        .containsExactly(1);
  }

    @Test
    public void sourceElements_whenGraphIsLoop_returnsEmpty () {
    assertThat(
        GraphAlgorithms.sourceElements(
            ImmutableDirectedGraph.builder()
                .addNode(1)
                .addNode(2)
                .addNode(3)
                .addEdge(1, 2)
                .addEdge(2, 3)
                .addEdge(3, 1)
                .build()))
        .isEmpty();
  }
  }

  @RunWith(JUnit4.class)
  public static final class GraphAlgorithms_SinkElements {

    @Test
    public void sinkElements_whenGraphEmpty_returnsEmpty() {
      assertThat(GraphAlgorithms.sinkElements(ImmutableDirectedGraph.empty())).isEmpty();
    }

    @Test
    public void sinkElements_whenGraphHasOneSink_returnsSink() {
      assertThat(
          GraphAlgorithms.sinkElements(
              ImmutableDirectedGraph.builder()
                  .addNode(1)
                  .addNode(2)
                  .addNode(3)
                  .addEdge(1, 2)
                  .addEdge(2, 3)
                  .build()))
          .containsExactly(3);
    }

    @Test
    public void sinkElements_whenGraphIsLoop_returnsSink() {
      assertThat(
          GraphAlgorithms.sinkElements(
              ImmutableDirectedGraph.builder()
                  .addNode(1)
                  .addNode(2)
                  .addNode(3)
                  .addEdge(1, 2)
                  .addEdge(2, 3)
                  .addEdge(3, 1)
                  .build()))
          .isEmpty();
    }
  }

  @RunWith(JUnit4.class)
  public static final class GraphAlgorithms_IsCyclicalTest {

    @Test
    public void isCyclical_whenEmpty_isFalse () {
    DirectedGraph<Object> graph = ImmutableDirectedGraph.empty();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

    @Test
    public void isCyclical_whenSingleNode_isFalse () {
    DirectedGraph<Object> graph = ImmutableDirectedGraph.builder().addNode(new Object()).build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

    @Test
    public void isCyclical_whenSingleNode_edgePointsTowardsSelf_isTrue () {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder().addNode(1).addEdge(1, 1).build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isTrue();
  }

    @Test
    public void isCyclical_whenChainOfTwoNodes_isFalse () {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder()
            .addNode(1)
            .addNode(2)
            .addEdge(1, 2)
            .build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

    @Test
    public void isCyclical_whenLoopOfTwoNodes_isTrue () {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder()
            .addNode(1)
            .addNode(2)
            .addEdge(1, 2)
            .addEdge(2, 1)
            .build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isTrue();
  }

    @Test
    public void isCyclical_whenChainOfTenNodes_isFalse () {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder()
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
            .build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

    @Test
    public void isCyclical_whenLoopOfTenNodes_isTrue () {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder()
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
            .build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isTrue();
  }

    @Test
    public void isCyclical_whenDisjointChains_isFalse () {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder()
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
            .build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }
  }

  @RunWith(JUnit4.class)
  public static final class GraphAlgorithms_TopologicallySortTest {

    @Test
    public void topologicallySort_whenSigleNode_whenCyclical_throwsException() {
      assertThrows(
          IllegalArgumentException.class,
          () -> GraphAlgorithms.topologicallySort(
              ImmutableDirectedGraph.builder()
                  .addNode(1)
                  .addEdge(1, 1)
                  .build()));
    }

    @Test
    public void topologicallySort_whenLollipop_throwsException() {
      assertThrows(
          IllegalArgumentException.class,
          () -> GraphAlgorithms.topologicallySort(
              ImmutableDirectedGraph.builder()
                  .addNode(1)
                  .addNode(2)
                  .addNode(3)
                  .addEdge(1, 2)
                  .addEdge(2, 3)
                  .addEdge(3, 2)
                  .build()));
    }

    @Test
    public void topologicallySort_whenCycle_withTwoNodes_throwsException() {
      assertThrows(
          IllegalArgumentException.class,
          () -> GraphAlgorithms.topologicallySort(
              ImmutableDirectedGraph.builder()
                  .addNode(1)
                  .addNode(2)
                  .addEdge(1, 2)
                  .addEdge(2, 1)
                  .build()));
    }

    @Test
    public void topologicallySort_whenCycle_withThreeNodes_throwsException() {
      assertThrows(
          IllegalArgumentException.class,
          () -> GraphAlgorithms.topologicallySort(
              ImmutableDirectedGraph.builder()
                  .addNode(1)
                  .addNode(2)
                  .addNode(3)
                  .addEdge(1, 2)
                  .addEdge(2, 3)
                  .addEdge(3, 1)
                  .build()));
    }

    @Test
    public void topologicallySort_whenEmpty_isEmpty() {
      Truth.assertThat(
              GraphAlgorithms.topologicallySort(ImmutableDirectedGraph.empty()).isPopulated())
          .isFalse();
    }

    @Test
    public void topologicallySort_whenChain_isTopologicallySorted() {
      DirectedGraph<Integer> graph =
          ImmutableDirectedGraph.<Integer>builder()
              .addNode(1)
              .addNode(2)
              .addNode(3)
              .addNode(4)
              .addEdge(1, 2)
              .addEdge(2, 3)
              .addEdge(3, 4)
              .build();

      assertThat(GraphAlgorithms.topologicallySort(graph)).isATopologicalSortOf(graph);
    }

    @Test
    public void topologicallySort_whenForked_isTopologicallySorted() {
      DirectedGraph<Integer> graph =
          ImmutableDirectedGraph.<Integer>builder()
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
              .build();

      assertThat(GraphAlgorithms.topologicallySort(graph)).isATopologicalSortOf(graph);
    }

    @Test
    public void topologicallySort_whenMerged_isTopologicallySorted() {
      DirectedGraph<Integer> graph =
          ImmutableDirectedGraph.<Integer>builder()
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
              .build();

      assertThat(GraphAlgorithms.topologicallySort(graph)).isATopologicalSortOf(graph);
    }

    @Test
    public void topologicallySort_whenForkedAndMerged_isTopologicallySorted() {
      DirectedGraph<Integer> graph =
          ImmutableDirectedGraph.<Integer>builder()
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
              .build();

      assertThat(GraphAlgorithms.topologicallySort(graph)).isATopologicalSortOf(graph);
    }

    @Test
    public void topologicallySort_whenDisconnected_isTopologicallySorted() {
      DirectedGraph<Integer> graph =
          ImmutableDirectedGraph.<Integer>builder()
              .addNode(1)
              .addNode(2)
              .addNode(3)
              .addNode(4)
              .addNode(5)
              .addNode(6)
              .addNode(7)
              .addNode(8)
              .build();

      assertThat(GraphAlgorithms.topologicallySort(graph)).isATopologicalSortOf(graph);
    }

    private static <T> TopologicalAssertion<T> assertThat(List<T> list) {
      // O(N^2)
      return graph -> {
        Truth.assertThat(list.count()).isEqualTo(graph.nodes().count());

        // O(N)
        for (T item : list) {
          Truth8.assertThat(graph.nodeOf(item)).isPresent();
        }

        MutableSet<T> cumulativePredecessors = HashSet.create();

        // O(N^2)
        for (T item : list) {

          // predecessors of item exist in cumulative predecessors: O(N)
          graph.nodeOf(item)
              .map(DirectedNode::predecessors)
              .orElse(Set.empty())
              .stream()
              .map(DirectedNode::item)
              .forEach(
                  directPredecessor ->
                      Truth.assertThat(cumulativePredecessors.contains(directPredecessor))
                          .isTrue());

          // successors of item do NOT exist in cumulative predecessors yet: O(N)
          graph.nodeOf(item)
              .map(DirectedNode::successors)
              .orElse(Set.empty())
              .stream()
              .map(DirectedNode::item)
              .forEach(
                  directPredecessor ->
                      Truth.assertThat(cumulativePredecessors.contains(directPredecessor))
                          .isFalse());

          cumulativePredecessors.add(item);
        }

        // all nodes in graph exist in the result: O(N)
        for (DirectedNode<? extends T> node : graph.nodes()) {
          Truth.assertThat(cumulativePredecessors.contains(node.item())).isTrue();
        }
      };
    }

    private interface TopologicalAssertion<T> {

      void isATopologicalSortOf(DirectedGraph<T> graph);
    }
  }
}
