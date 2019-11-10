package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.cache.WeakCache;
import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.UnorderedPair;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;

public final class ImmutableUndirectedGraph<E> implements UndirectedGraph<E> {

  private static final ImmutableUndirectedGraph<?> EMPTY_IMMUTABLE_UNDIRECTED_GRAPH =
      new ImmutableUndirectedGraph<>();

  private final ImmutableSet<E> elements;
  private final ImmutableSet<ImmutableUnorderedPair<E>> edges;

  public static <E> ImmutableUndirectedGraph<E> empty() {
    @SuppressWarnings("unchecked")
    ImmutableUndirectedGraph<E> g = (ImmutableUndirectedGraph<E>) EMPTY_IMMUTABLE_UNDIRECTED_GRAPH;
    return g;
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static class Builder<E> {
    MutableSet<E> nodes = new HashSet<>();
    MutableSet<ImmutableUnorderedPair<E>> edges = new HashSet<>();

    public Builder<E> addNode(E element) {
      nodes.add(requireNonNull(element));
      return this;
    }

    public Builder<E> addEdge(E element1, E element2) {
      edges.add(ImmutableUnorderedPair.of(element1, element2));
      return this;
    }

    public ImmutableUndirectedGraph<E> build() {
      return nodes.isPopulated() || edges.isPopulated()
          ? new ImmutableUndirectedGraph<>(this)
          : empty();
    }

    private Builder() {}
  }

  private ImmutableUndirectedGraph() {
    elements = ImmutableSet.empty();
    edges = ImmutableSet.empty();
  }

  private ImmutableUndirectedGraph(Builder<E> builder) {
    elements =
        Stream.concat(
                builder.nodes.stream(),
                builder.edges.stream().flatMap(ImmutableUnorderedPair::stream))
            .collect(toImmutableSet());
    edges = builder.edges.stream().collect(toImmutableSet());
  }

  @Override
  public Optional<? extends Node> nodeOf(E element) {
    return elements.contains(element) ? Optional.of(getOrCreateNode(element)) : Optional.empty();
  }

  @Override
  public Set<E> contents() {
    return elements;
  }

  @Override
  public Set<? extends Node> nodes() {
    return elements.stream().map(toNode()).collect(toSet());
  }

  @Override
  public Set<? extends Edge> edges() {
    return edges.stream().map(toEdge()).collect(toSet());
  }

  @Override
  public boolean contains(Object element) {
    return elements.contains(element);
  }

  @Override
  public int count() {
    return elements.count();
  }

  @Override
  public boolean isPopulated() {
    return elements.isPopulated();
  }

  private class Node implements UndirectedGraph.UndirectedNode<E> {
    private final E element;

    private Node(E element) {
      this.element = element;
    }

    @Override
    public E element() {
      return element;
    }

    @Override
    public Set<? extends Edge> edges() {
      return edges.stream().filter(p -> p.contains(element)).map(toEdge()).collect(toSet());
    }

    @Override
    public Set<? extends Node> neighbors() {
      return edges.stream()
          .filter(p -> p.contains(element))
          .map(p -> p.first().equals(element) ? p.second() : p.first())
          .map(toNode())
          .collect(toSet());
    }
  }

  private class Edge implements UndirectedGraph.UndirectedEdge<E> {
    private final ImmutableUnorderedPair<E> endpoints;

    private Edge(ImmutableUnorderedPair<E> endpoints) {
      this.endpoints = endpoints;
    }

    @Override
    public UnorderedPair<? extends Node> endpoints() {
      return UnorderedPair.of(
          getOrCreateNode(endpoints.first()), getOrCreateNode(endpoints.second()));
    }
  }

  private Function<? super E, ? extends Node> toNode() {
    return this::getOrCreateNode;
  }

  private final WeakCache<E, Node> nodeCache = new WeakCache<>();

  private Node getOrCreateNode(E element) {
    return nodeCache.getOrCache(element, () -> new Node(element));
  }

  private Function<? super ImmutableUnorderedPair<E>, ? extends Edge> toEdge() {
    return this::getOrCreateEdge;
  }

  private WeakCache<ImmutableUnorderedPair<E>, Edge> edgeCache = new WeakCache<>();

  private Edge getOrCreateEdge(ImmutableUnorderedPair<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new Edge(endpoints));
  }
}
