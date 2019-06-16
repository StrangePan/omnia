package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.cache.WeakCache;
import omnia.data.stream.Collectors;
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

  private final class Node implements DirectedGraph.Node<E> {
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
      return directedEdges.stream()
          .filter(p -> p.contains(element))
          .map(toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends Edge> outgoingEdges() {
      return directedEdges.stream()
          .filter(pair -> pair.first().equals(element))
          .map(toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends Edge> incomingEdges() {
      return directedEdges.stream()
          .filter(pair -> pair.second().equals(element))
          .map(toEdge())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.Node<E>> neighbors() {
      return directedEdges.stream()
          .filter(p -> p.contains(element))
          .map(p -> p.first().equals(element) ? p.second() : p.first())
          .map(toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.Node<E>> successors() {
      return directedEdges.stream()
          .filter(pair -> pair.first().equals(element))
          .map(Pair::second)
          .map(toNode())
          .collect(toSet());
    }

    @Override
    public Set<? extends DirectedGraph.Node<E>> predecessors() {
      return directedEdges.stream()
          .filter(pair -> pair.second().equals(element))
          .map(Pair::first)
          .map(toNode())
          .collect(toSet());
    }
  }

  private final class Edge implements DirectedGraph.Edge<E> {
    private final HomogeneousPair<E> endpoints;

    private Edge(HomogeneousPair<E> endpoints) {
      this.endpoints = endpoints;
    }

    @Override
    public DirectedGraph.Node<E> start() {
      return getOrCreateNode(endpoints.first());
    }

    @Override
    public DirectedGraph.Node<E> end() {
      return getOrCreateNode(endpoints.second());
    }

    @Override
    public Collection<? extends DirectedGraph.Node<E>> endpoints() {
      return Collection.masking(endpoints.stream().map(toNode()).collect(toList()));
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

  private Function<? super E, ? extends Node> toNode() {
    return this::getOrCreateNode;
  }

  private WeakCache<E, Node> nodeCache = new WeakCache<>();

  private Node getOrCreateNode(E element) {
    return nodeCache.getOrCache(element, () -> new Node(element));
  }

  private Function<? super HomogeneousPair<E>, ? extends Edge> toEdge() {
    return this::getOrCreateEdge;
  }

  private WeakCache<HomogeneousPair<E>, Edge> edgeCache = new WeakCache<>();

  private Edge getOrCreateEdge(HomogeneousPair<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new Edge(endpoints));
  }
}
