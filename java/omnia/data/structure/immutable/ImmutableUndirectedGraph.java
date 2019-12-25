package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Objects;
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

  public Builder<E> toBuilder() {
    Builder<E> builder = builder();
    elements.forEach(builder::addNode);
    edges.forEach(pair -> builder.addEdge(pair.first(), pair.second()));
    return builder;
  }

  public static final class Builder<E> {
    MutableSet<E> nodes = new HashSet<>();
    MutableSet<ImmutableUnorderedPair<E>> edges = new HashSet<>();

    public Builder<E> addNode(E element) {
      nodes.add(requireNonNull(element));
      return this;
    }

    public Builder<E> removeNode(E element) {
      nodes.remove(requireNonNull(element));
      edges.stream().filter(pair -> pair.contains(element)).forEach(edges::remove);
      return this;
    }

    public Builder<E> replaceNode(E original, E replacement) {
      requireNonNull(original);
      requireNonNull(replacement);
      if (!nodes.contains(original)) {
        throw new IllegalStateException("cannot replace a node that does not exist. node=" + original);
      }
      if (nodes.contains(replacement)) {
        throw new IllegalStateException("element already contained as node in graph. node=" + replacement);
      }
      nodes.remove(original);
      nodes.add(replacement);
      Set<ImmutableUnorderedPair<E>> edgesToRemove =
          edges.stream().filter(pair -> pair.contains(original)).collect(toSet());
      Function<E, E> replaceOriginalWithReplacement =
          element -> Objects.equals(element, original) ? replacement : element;
      Set<ImmutableUnorderedPair<E>> edgesToAdd =
          edgesToRemove.stream()
              .map(pair -> pair.map(replaceOriginalWithReplacement))
              .collect(toSet());
      edgesToRemove.forEach(edges::remove);
      edgesToAdd.forEach(edges::add);
      return this;
    }

    public Builder<E> addEdge(E element1, E element2) {
      requireNonNull(element1);
      requireNonNull(element2);
      if (!nodes.contains(element1)) {
        throw new IllegalStateException("cannot add edge for nonexistent edge");
      }
      if (!nodes.contains(element2)) {
        throw new IllegalStateException("cannot add edge for nonexistent edge");
      }
      edges.add(ImmutableUnorderedPair.of(element1, element2));
      return this;
    }

    public Builder<E> removeEdge(E element1, E element2) {
      requireNonNull(element1);
      requireNonNull(element2);
      edges.remove(ImmutableUnorderedPair.of(element1, element2));
      return this;
    }

    public ImmutableUndirectedGraph<E> build() {
      return nodes.isPopulated() ? new ImmutableUndirectedGraph<>(this) : empty();
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
  public Optional<? extends UndirectedNode> nodeOf(Object item) {
    @SuppressWarnings("unchecked")
    Optional<? extends UndirectedNode> node =
        elements.contains(item) ? Optional.of(getOrCreateNode((E) item)) : Optional.empty();
    return node;
  }

  @Override
  public Set<E> contents() {
    return elements;
  }

  @Override
  public Set<? extends UndirectedGraph.UndirectedNode<E>> nodes() {
    return elements.stream().map(toNode()).collect(toSet());
  }

  @Override
  public Set<? extends UndirectedGraph.UndirectedEdge<E>> edges() {
    return edges.stream().map(toEdge()).collect(toSet());
  }

  private class UndirectedNode implements UndirectedGraph.UndirectedNode<E> {
    private final E item;

    private UndirectedNode(E item) {
      this.item = requireNonNull(item);
    }

    @Override
    public E item() {
      return item;
    }

    @Override
    public Set<? extends UndirectedEdge> edges() {
      return edges.stream().filter(p -> p.contains(item)).map(toEdge()).collect(toSet());
    }

    @Override
    public Set<? extends UndirectedNode> neighbors() {
      return edges.stream()
          .filter(p -> p.contains(item))
          .map(p -> p.first().equals(item) ? p.second() : p.first())
          .map(toNode())
          .collect(toSet());
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof ImmutableUndirectedGraph.UndirectedNode
          && ((ImmutableUndirectedGraph<?>.UndirectedNode) other).graph() == graph()
          && Objects.equals(((ImmutableUndirectedGraph<?>.UndirectedNode) other).item, item);
    }

    @Override
    public int hashCode() {
      return Objects.hash(item);
    }

    private ImmutableUndirectedGraph<E> graph() {
      return ImmutableUndirectedGraph.this;
    }
  }

  private class UndirectedEdge implements UndirectedGraph.UndirectedEdge<E> {
    private final ImmutableUnorderedPair<E> endpoints;

    private UndirectedEdge(ImmutableUnorderedPair<E> endpoints) {
      this.endpoints = endpoints;
    }

    @Override
    public UnorderedPair<? extends UndirectedNode> endpoints() {
      return UnorderedPair.of(
          getOrCreateNode(endpoints.first()), getOrCreateNode(endpoints.second()));
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof ImmutableUndirectedGraph<?>.UndirectedEdge
          && ((ImmutableUndirectedGraph<?>.UndirectedEdge) other).graph() == graph()
          && Objects.equals(
              ((ImmutableUndirectedGraph<?>.UndirectedEdge) other).endpoints, endpoints);
    }

    @Override
    public int hashCode() {
      return Objects.hash(endpoints);
    }

    private ImmutableUndirectedGraph<E> graph() {
      return ImmutableUndirectedGraph.this;
    }
  }

  private Function<? super E, ? extends UndirectedNode> toNode() {
    return this::getOrCreateNode;
  }

  private final WeakCache<E, UndirectedNode> nodeCache = new WeakCache<>();

  private UndirectedNode getOrCreateNode(E element) {
    return nodeCache.getOrCache(element, () -> new UndirectedNode(element));
  }

  private Function<? super ImmutableUnorderedPair<E>, ? extends UndirectedEdge> toEdge() {
    return this::getOrCreateEdge;
  }

  private WeakCache<ImmutableUnorderedPair<E>, UndirectedEdge> edgeCache = new WeakCache<>();

  private UndirectedEdge getOrCreateEdge(ImmutableUnorderedPair<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new UndirectedEdge(endpoints));
  }
}
