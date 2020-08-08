package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.contract.Streamable;
import omnia.data.cache.WeakCache;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.tuple.Couplet;
import omnia.data.structure.tuple.Tuplet;

public final class ImmutableDirectedGraph<E> implements DirectedGraph<E> {

  private static final ImmutableDirectedGraph<?> EMPTY_IMMUTABLE_DIRECTED_GRAPH =
      new ImmutableDirectedGraph<>();

  private final ImmutableSet<E> elements;
  private final ImmutableSet<Couplet<E>> directedEdges;

  public static <E> ImmutableDirectedGraph<E> empty() {
    @SuppressWarnings("unchecked")
    ImmutableDirectedGraph<E> g = (ImmutableDirectedGraph<E>) EMPTY_IMMUTABLE_DIRECTED_GRAPH;
    return g;
  }

  public static <E> ImmutableDirectedGraph<E> copyOf(DirectedGraph<E> original) {
    return buildUpon(original).build();
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
            .map(edge -> Tuplet.of(edge.start().item(), edge.end().item()))
            .collect(toSet()));
  }

  public static final class Builder<E> {
    private final MutableSet<E> nodes;
    private final MutableSet<Couplet<E>> directedEdges;

    private Builder() {
      nodes = new HashSet<>();
      directedEdges = new HashSet<>();
    }

    private Builder(Set<E> nodes, Set<Couplet<E>> directedEdges) {
      this.nodes = nodes instanceof MutableSet ? (MutableSet<E>) nodes : HashSet.copyOf(nodes);
      this.directedEdges =
          directedEdges instanceof MutableSet
              ? (MutableSet<Couplet<E>>) directedEdges
              : HashSet.copyOf(directedEdges);
    }

    public Builder<E> addNode(E element) {
      requireNonNull(element);
      nodes.add(element);
      return this;
    }

    public Builder<E> removeNode(E element) {
      return removeUnknownTypedNode(element);
    }

    public Builder<E> removeUnknownTypedNode(Object element) {
      requireNonNull(element);
      nodes.removeUnknownTyped(element);
      directedEdges.stream()
          .filter(pair -> pair.containsUnknownTyped(element))
          .collect(toSet())
          .forEach(directedEdges::remove);
      return this;
    }

    public Builder<E> replaceNode(E original, E replacement) {
      requireNonNull(original);
      requireNonNull(replacement);
      if (!nodes.contains(original)) {
        throw new IllegalArgumentException("cannot replace a non-existent node. original=" + original);
      }
      if (nodes.contains(replacement)) {
        throw new IllegalArgumentException(
            "cannot replace a node with an already existing node. replacement=" + replacement);
      }
      Set<Couplet<E>> edgesToRemove =
          directedEdges.stream().filter(pair -> pair.contains(original)).collect(toSet());
      Set<Couplet<E>> edgesToAdd =
          edgesToRemove.stream()
              .map(
                  couplet ->
                      couplet.map(
                          element -> Objects.equals(element, original) ? replacement : element))
              .collect(toSet());
      edgesToRemove.forEach(directedEdges::remove);
      edgesToAdd.forEach(directedEdges::add);
      nodes.remove(original);
      nodes.add(replacement);
      return this;
    }

    public Builder<E> addEdge(E from, E to) {
      requireNonNull(from);
      requireNonNull(to);
      if (!nodes.contains(from)) {
        throw new IllegalArgumentException("cannot add an edge for a nonexistent node");
      }
      if (!nodes.contains(to)) {
        throw new IllegalStateException("cannot add an edge for a nonexistent node");
      }
      directedEdges.add(Tuplet.of(from, to));
      return this;
    }

    public Builder<E> removeEdge(Object from, Object to) {
      requireNonNull(from);
      requireNonNull(to);
      directedEdges.removeUnknownTyped(Tuplet.of(from, to));
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
                builder.directedEdges.stream().flatMap(Streamable::stream))
            .collect(toImmutableSet());
    directedEdges =
        builder.directedEdges.stream()
            .filter(couple -> elements.contains(couple.first()))
            .filter(couple -> elements.contains(couple.second()))
            .collect(toImmutableSet());
  }

  @Override
  public Optional<? extends DirectedNode> nodeOf(Object item) {
    @SuppressWarnings("unchecked")
    Optional<? extends DirectedNode> node =
        elements.containsUnknownTyped(item) ? Optional.of(getOrCreateNode((E) item)) : Optional.empty();
    return node;
  }

  @Override
  public Optional<? extends DirectedEdge> edgeOf(Object from, Object to) {
    @SuppressWarnings("unchecked")
    Optional<? extends DirectedEdge> edge =
        Stream.of(Tuplet.of(from, to))
            .filter(directedEdges::containsUnknownTyped)
            .map(p -> (Couplet<E>) p)
            .findFirst()
            .map(this::getOrCreateEdge);
    return edge;
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

  public final class DirectedNode implements DirectedGraph.DirectedNode<E> {
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
          .filter(couplet -> couplet.first().equals(item))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedEdge> incomingEdges() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(couplet -> couplet.second().equals(item))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> neighbors() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(couplet -> couplet.contains(item))
          .map(couplet -> couplet.first().equals(item) ? couplet.second() : couplet.first())
          .map(ImmutableDirectedGraph.this.toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> successors() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(couplet -> couplet.first().equals(item))
          .map(Couplet::second)
          .map(ImmutableDirectedGraph.this.toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> predecessors() {
      return ImmutableDirectedGraph.this.directedEdges.stream()
          .filter(couplet -> couplet.second().equals(item))
          .map(Couplet::first)
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

  public final class DirectedEdge implements DirectedGraph.DirectedEdge<E> {
    private final Couplet<E> endpoints;

    private DirectedEdge(Couplet<E> endpoints) {
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
    public Couplet<DirectedNode> endpoints() {
      return Tuplet.of(
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

  private Function<? super Couplet<E>, ? extends DirectedEdge> toEdge() {
    return this::getOrCreateEdge;
  }

  private WeakCache<Couplet<E>, DirectedEdge> edgeCache = new WeakCache<>();

  private DirectedEdge getOrCreateEdge(Couplet<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new DirectedEdge(endpoints));
  }
}
