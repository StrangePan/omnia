package omnia.algorithm;

import static com.google.common.truth.Truth.assertThat;

import omnia.data.structure.DirectedGraph;
import omnia.data.structure.immutable.ImmutableDirectedGraph;
import omnia.data.structure.immutable.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class GraphAlgorithmsTest {

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

  @Test
  public void sourceElements_whenGraphEmpty_returnsEmpty() {
    assertThat(GraphAlgorithms.sourceElements(ImmutableDirectedGraph.empty())).isEmpty();
  }

  @Test
  public void sourceElements_whenGraphHasOneSource_returnsSource() {
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
  public void sourceElements_whenGraphIsLoop_returnsEmpty() {
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

  @Test
  public void isCyclical_whenEmpty_isFalse() {
    DirectedGraph<Object> graph = ImmutableDirectedGraph.empty();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

  @Test
  public void isCyclical_whenSingleNode_isFalse() {
    DirectedGraph<Object> graph = ImmutableDirectedGraph.builder().addNode(new Object()).build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

  @Test
  public void isCyclical_whenSingleNode_edgePointsTowardsSelf_isTrue() {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder().addNode(1).addEdge(1, 1).build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isTrue();
  }

  @Test
  public void isCyclical_whenChainOfTwoNodes_isFalse() {
    DirectedGraph<Integer> graph =
        ImmutableDirectedGraph.<Integer>builder()
            .addNode(1)
            .addNode(2)
            .addEdge(1, 2)
            .build();

    assertThat(GraphAlgorithms.isCyclical(graph)).isFalse();
  }

  @Test
  public void isCyclical_whenLoopOfTwoNodes_isTrue() {
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
  public void isCyclical_whenChainOfTenNodes_isFalse() {
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
  public void isCyclical_whenLoopOfTenNodes_isTrue() {
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
  public void isCyclical_whenDisjointChains_isFalse() {
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
