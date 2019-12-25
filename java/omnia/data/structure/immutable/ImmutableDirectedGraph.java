package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.cache.WeakCache;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.HomogeneousPair;
import omnia.data.structure.Pair;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;

public final class ImmutableDirectedGraph<E> implements DirectedGraph<E> {

  private static final ImmutableDirectedGraph<?> EMPTY_IMMUTABLE_DIRECTED_GRAPH =
      new ImmutableDirectedGraph<>();

  private final ImmutableSet<E> elements;
  private final ImmutableSet<HomogeneousPair<E>> directedEdges;

  public static <E> ImmutableDirectedGraph<E> empty() {
    @SuppressWarnings("unchecked")
    ImmutableDirectedGraph<E> g = (ImmutableDirectedGraph<E>) EMPTY_IMMUTABLE_DIRECTED_GRAPH;
    return g;
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static <E> Builder<E> buildUpon(DirectedGraph<E> original) {
    if (original instanceof ImmutableDirectedGraph) {
      return ((ImmutableDirectedGraph<E>) original).toBuilder();
    }
    return new Builder<>(
        original.nodes().stream().map(DirectedGraph.DirectedNode::item).collect(toSet()),
        original.edges().stream()
            .map(edge -> HomogeneousPair.of(edge.start().item(), edge.end().item()))
            .collect(toSet()));
  }

  public static final class Builder<E> {
    private final MutableSet<E> nodes;
    private final MutableSet<HomogeneousPair<E>> directedEdges;

    private Builder() {
      nodes = new HashSet<>();
      directedEdges = new HashSet<>();
    }

    private Builder(Set<E> nodes, Set<HomogeneousPair<E>> directedEdges) {
      this.nodes = nodes instanceof MutableSet ? (MutableSet<E>) nodes : HashSet.copyOf(nodes);
      this.directedEdges =
          directedEdges instanceof MutableSet
              ? (MutableSet<HomogeneousPair<E>>) directedEdges
              : HashSet.copyOf(directedEdges);
    }

    public Builder<E> addNode(E element) {
      requireNonNull(element);
      nodes.add(element);
      return this;
    }

    public Builder<E> removeNode(E element) {
      requireNonNull(element);
      nodes.remove(element);
      directedEdges.stream().filter(pair -> pair.contains(element)).forEach(directedEdges::remove);
      return this;
    }

    public Builder<E> replaceNode(E original, E replacement) {
      requireNonNull(original);
      requireNonNull(replacement);
      if (!nodes.contains(original)) {
        throw new IllegalStateException("cannot replace a non-existent node. original=" + original);
      }
      if (nodes.contains(replacement)) {
        throw new IllegalStateException(
            "cannot replace a node with an already existing node. replacement=" + replacement);
      }
      Set<HomogeneousPair<E>> edgesToRemove =
          directedEdges.stream().filter(pair -> pair.contains(original)).collect(toSet());
      Function<E, E> replaceOriginalWithReplacement =
          element -> Objects.equals(element, original) ? replacement : element;
      Set<HomogeneousPair<E>> edgesToAdd =
          edgesToRemove.stream()
              .map(
                  pair -> HomogeneousPair.of(
                      replaceOriginalWithReplacement.apply(pair.first()),
                      replaceOriginalWithReplacement.apply(pair.second())))
              .collect(toSet());
      edgesToRemove.forEach(directedEdges::remove);
      edgesToAdd.forEach(directedEdges::add);
      return this;
    }

    public Builder<E> addEdge(E from, E to) {
      requireNonNull(from);
      requireNonNull(to);
      if (!nodes.contains(from)) {
        throw new IllegalStateException("cannot add an edge for a nonexistent node");
      }
      if (!nodes.contains(to)) {
        throw new IllegalStateException("cannot add an edge for a nonexistent node");
      }
      directedEdges.add(HomogeneousPair.of(from, to));
      return this;
    }

    public Builder<E> removeEdge(E from, E to) {
      requireNonNull(from);
      requireNonNull(to);
      directedEdges.remove(HomogeneousPair.of(from, to));
      return this;
    }

    public ImmutableDirectedGraph<E> build() {
      return new ImmutableDirectedGraph<>(this);
    }
  }

  private ImmutableDirectedGraph() {
    elements = ImmutableSet.empty();
    directedEdges = ImmutableSet.empty();
  }

  private ImmutableDirectedGraph(Builder<E> builder) {
    elements =
        Stream.concat(
                builder.nodes.stream(),
                builder.directedEdges.stream().flatMap(HomogeneousPair::stream))
            .collect(toImmutableSet());
    directedEdges =
        builder.directedEdges.stream()
            .filter(pair -> elements.contains(pair.first()))
            .filter(pair -> elements.contains(pair.second()))
            .collect(toImmutableSet());
  }

  @Override
  public Optional<? extends DirectedNode> nodeOf(Object item) {
    @SuppressWarnings("unchecked")
    Optional<? extends DirectedNode> node =
        elements.contains(item) ? Optional.of(getOrCreateNode((E) item)) : Optional.empty();
    return node;
  }

  @Override
  public Set<E> contents() {
    return elements;
  }

  @Override
  public Set<? extends DirectedNode> nodes() {
    return elements.stream().map(toNode()).collect(toSet());
  }

  @Override
  public Set<? extends DirectedEdge> edges() {
    return directedEdges.stream().map(toEdge()).collect(toSet());
  }

  public Builder<E> toBuilder() {
    return new Builder<>(this.elements, this.directedEdges);
  }

  private final class DirectedNode implements DirectedGraph.DirectedNode<E> {
    private final E item;

    private DirectedNode(E item) {
      this.item = item;
    }

    @Override
    public E item() {
      return item;
    }

    @Override
    public Set<? extends DirectedEdge> edges() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(p -> p.contains(item))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedEdge> outgoingEdges() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(pair -> pair.first().equals(item))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedEdge> incomingEdges() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(pair -> pair.second().equals(item))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> neighbors() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(p -> p.contains(item))
          .map(p -> p.first().equals(item) ? p.second() : p.first())
          .map(ImmutableDirectedGraph.this.toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> successors() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(pair -> pair.first().equals(item))
          .map(Pair::second)
          .map(ImmutableDirectedGraph.this.toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> predecessors() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(pair -> pair.second().equals(item))
          .map(Pair::first)
          .map(ImmutableDirectedGraph.this.toNode())
          .collect(toSet());
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof ImmutableDirectedGraph<?>.DirectedNode
          && ((ImmutableDirectedGraph<?>.DirectedNode) other).graph() == graph()
          && Objects.equals(item, ((ImmutableDirectedGraph<?>.DirectedNode) other).item);
    }

    private ImmutableDirectedGraph<E> graph() {
      return ImmutableDirectedGraph.this;
    }
  }

  private final class DirectedEdge implements DirectedGraph.DirectedEdge<E> {
    private final HomogeneousPair<E> endpoints;

    private DirectedEdge(HomogeneousPair<E> endpoints) {
      this.endpoints = endpoints;
    }

    @Override
    public DirectedGraph.DirectedNode<E> start() {
      return ImmutableDirectedGraph.this.getOrCreateNode(endpoints.first());
    }

    @Override
    public DirectedGraph.DirectedNode<E> end() {
      return ImmutableDirectedGraph.this.getOrCreateNode(endpoints.second());
    }

    @Override
    public HomogeneousPair<DirectedNode> endpoints() {
      return HomogeneousPair.of(
          ImmutableDirectedGraph.this.getOrCreateNode(endpoints.first()),
          ImmutableDirectedGraph.this.getOrCreateNode(endpoints.second()));
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof ImmutableDirectedGraph.DirectedEdge
          && ((ImmutableDirectedGraph<?>.DirectedEdge) other).graph() == graph()
          && Objects.equals(endpoints, ((ImmutableDirectedGraph<?>.DirectedEdge) other).endpoints);
    }

    @Override
    public int hashCode() {
      return Objects.hash(endpoints);
    }

    private ImmutableDirectedGraph<E> graph() {
      return ImmutableDirectedGraph.this;
    }
  }

  private Function<? super E, ? extends DirectedNode> toNode() {
    return this::getOrCreateNode;
  }

  private WeakCache<E, DirectedNode> nodeCache = new WeakCache<>();

  private DirectedNode getOrCreateNode(E item) {
    return nodeCache.getOrCache(item, () -> new DirectedNode(item));
  }

  private Function<? super HomogeneousPair<E>, ? extends DirectedEdge> toEdge() {
    return this::getOrCreateEdge;
  }

  private WeakCache<HomogeneousPair<E>, DirectedEdge> edgeCache = new WeakCache<>();

  private DirectedEdge getOrCreateEdge(HomogeneousPair<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new DirectedEdge(endpoints));
  }
}
