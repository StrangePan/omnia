package omnia.data.structure;

import omnia.data.cache.WeakCache;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static omnia.data.stream.Collectors.toSet;

public final class HashGraph<E> implements Graph<E> {

  private final Set<E> elements;
  private final Set<UnorderedPair<E>> edges;

  private HashGraph(Builder<E> builder) {
    elements =
        Stream.concat(
                builder.nodes.stream(),
                builder.edges.stream().flatMap(UnorderedPair::stream))
            .collect(toSet());
    edges = Set.masking(builder.edges);
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
  public boolean contains(E element) {
    return elements.contains(element);
  }

  @Override
  public int count() {
    return elements.count();
  }

  private class Node implements Graph.Node<E> {
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

  private class Edge implements Graph.Edge<E> {
    private final UnorderedPair<E> endpoints;

    private Edge(UnorderedPair<E> endpoints) {
      this.endpoints = endpoints;
    }

    @Override
    public Collection<? extends Node> endpoints() {
      return Collection.of(endpoints.stream().map(toNode()).collect(toList()));
    }
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static class Builder<E> {
    java.util.Set<E> nodes = new HashSet<>();
    java.util.Set<UnorderedPair<E>> edges = new HashSet<>();

    public Builder<E> addNode(E element) {
      nodes.add(requireNonNull(element));
      return this;
    }

    public Builder<E> addEdge(E element1, E element2) {
      edges.add(UnorderedPair.of(element1, element2));
      return this;
    }

    public HashGraph<E> build() {
      return new HashGraph<>(this);
    }

    private Builder() {
    }
  }

  private Function<? super E, ? extends Node> toNode() {
    return this::getOrCreateNode;
  }

  private Function<? super UnorderedPair<E>, ? extends Edge> toEdge() {
    return this::getOrCreateEdge;
  }

  private final WeakCache<E, Node> nodeCache = new WeakCache<>();

  private Node getOrCreateNode(E element) {
    return nodeCache.getOrCache(element, () -> new Node(element));
  }

  private WeakCache<UnorderedPair<E>, Edge> edgeCache = new WeakCache<>();

  private Edge getOrCreateEdge(UnorderedPair<E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new Edge(endpoints));
  }
}
