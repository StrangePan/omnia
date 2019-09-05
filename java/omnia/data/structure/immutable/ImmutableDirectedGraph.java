package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.cache.WeakCache;
import omnia.data.structure.Collection;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.HomogeneousPair;
import omnia.data.structure.Pair;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;

public final class ImmutableDirectedGraph<E> implements DirectedGraph<E> {

  private final ImmutableSet<E> elements;
  private final ImmutableSet<HomogeneousPair<E>> directedEdges;

  private ImmutableDirectedGraph(Builder<E> builder) {
    elements =
        Stream.concat(
                builder.nodes.stream(),
                builder.directedEdges.stream().flatMap(HomogeneousPair::stream))
            .collect(toImmutableSet());
    directedEdges = builder.directedEdges.stream().collect(toImmutableSet());
  }

  @Override
  public Optional<? extends Node<E>> nodeOf(E element) {
    return elements.contains(element) ? Optional.of(getOrCreateNode(element)) : Optional.empty();
  }

  @Override
  public Set<E> contents() {
    return elements;
  }

  @Override
  public Set<? extends Node<E>> nodes() {
    return elements.stream().map(toNode()).collect(toSet());
  }

  @Override
  public Set<? extends Edge<E>> edges() {
    return directedEdges.stream().map(toEdge()).collect(toSet());
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

  public Builder<E> toBuilder() {
    return new Builder<>(this.elements, this.directedEdges);
  }

  private static final class Node<E> implements DirectedGraph.Node<E> {
    private final E element;
    private final ImmutableDirectedGraph<E> graph;

    private Node(E element, ImmutableDirectedGraph<E> graph) {
      this.element = element;
      this.graph = graph;
    }

    @Override
    public E element() {
      return element;
    }

    @Override
    public Set<? extends Edge<E>> edges() {
      return graph.directedEdges.stream()
          .filter(p -> p.contains(element))
          .map(graph.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends Edge<E>> outgoingEdges() {
      return graph.directedEdges.stream()
          .filter(pair -> pair.first().equals(element))
          .map(graph.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends Edge<E>> incomingEdges() {
      return graph.directedEdges.stream()
          .filter(pair -> pair.second().equals(element))
          .map(graph.toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.Node<E>> neighbors() {
      return graph.directedEdges.stream()
          .filter(p -> p.contains(element))
          .map(p -> p.first().equals(element) ? p.second() : p.first())
          .map(graph.toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.Node<E>> successors() {
      return graph.directedEdges.stream()
          .filter(pair -> pair.first().equals(element))
          .map(Pair::second)
          .map(graph.toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.Node<E>> predecessors() {
      return graph.directedEdges.stream()
          .filter(pair -> pair.second().equals(element))
          .map(Pair::first)
          .map(graph.toNode())
          .collect(toSet());
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Node
          && Objects.equals(element, ((Node<?>) other).element);
    }
  }

  private static final class Edge<E> implements DirectedGraph.Edge<E> {
    private final HomogeneousPair<E> endpoints;
    private final ImmutableDirectedGraph<E> graph;

    private Edge(HomogeneousPair<E> endpoints, ImmutableDirectedGraph<E> graph) {
      this.endpoints = endpoints;
      this.graph = graph;
    }

    @Override
    public DirectedGraph.Node<E> start() {
      return graph.getOrCreateNode(endpoints.first());
    }

    @Override
    public DirectedGraph.Node<E> end() {
      return graph.getOrCreateNode(endpoints.second());
    }

    @Override
    public Collection<? extends DirectedGraph.Node<E>> endpoints() {
      return HomogeneousPair.of(
          graph.getOrCreateNode(endpoints.first()),
          graph.getOrCreateNode(endpoints.second()));
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Edge
          && Objects.equals(endpoints, ((Edge<?>) other).endpoints);
    }
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static <E> Builder<E> buildUpon(DirectedGraph<E> original) {
    if (original instanceof ImmutableDirectedGraph) {
      return ((ImmutableDirectedGraph<E>) original).toBuilder();
    }
    return new Builder<>(
        original.nodes().stream().map(DirectedGraph.Node::element).collect(toSet()),
        original.edges().stream()
            .map(edge -> HomogeneousPair.of(edge.start().element(), edge.end().element()))
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
      nodes.add(requireNonNull(element));
      return this;
    }

    public Builder<E> removeNode(E element) {
      nodes.remove(requireNonNull(element));
      return this;
    }

    public Builder<E> addEdge(E from, E to) {
      directedEdges.add(HomogeneousPair.of(from, to));
      return this;
    }

    public Builder<E> removeEdge(E from, E to) {
      directedEdges.remove(HomogeneousPair.of(from, to));
      return this;
    }

    public ImmutableDirectedGraph<E> build() {
      return new ImmutableDirectedGraph<>(this);
    }
  }

  private Function<? super E, ? extends Node<E>> toNode() {
    return this::getOrCreateNode;
  }

  private WeakCache<E, Node<E>> nodeCache = new WeakCache<>();

  private Node<E> getOrCreateNode(E element) {
    return nodeCache.getOrCache(element, () -> new Node<>(element, this));
  }

  private Function<? super HomogeneousPair<E>, ? extends Edge<E>> toEdge() {
    return this::getOrCreateEdge;
  }

  private WeakCache<HomogeneousPair<E>, Edge<E>> edgeCache = new WeakCache<>();

  private Edge<E> getOrCreateEdge(HomogeneousPair<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new Edge<>(endpoints, this));
  }
}
