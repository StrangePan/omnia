package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toHashMap;
import static omnia.data.stream.Collectors.toImmutableMap;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.cache.WeakCache;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Map;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.HashMap;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableMap;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.tuple.Couplet;
import omnia.data.structure.tuple.Tuple;
import omnia.data.structure.tuple.Tuplet;

public final class ImmutableDirectedGraph<E> implements DirectedGraph<E> {

  private static final ImmutableDirectedGraph<?> EMPTY_IMMUTABLE_DIRECTED_GRAPH =
      new ImmutableDirectedGraph<>();

  private final ImmutableSet<E> nodes;
  private final ImmutableMap<E, ImmutableSet<E>> neighbors;
  private final ImmutableMap<E, ImmutableSet<E>> successors;
  private final ImmutableMap<E, ImmutableSet<E>> predecessors;

  public static <E> ImmutableDirectedGraph<E> empty() {
    @SuppressWarnings("unchecked")
    ImmutableDirectedGraph<E> g = (ImmutableDirectedGraph<E>) EMPTY_IMMUTABLE_DIRECTED_GRAPH;
    return g;
  }

  public static <E> ImmutableDirectedGraph<E> copyOf(DirectedGraph<E> original) {
    return buildUpon(original).build();
  }

  public static <E, R> ImmutableDirectedGraph<R> copyOf(
      DirectedGraph<E> original, Function<? super E, ? extends R> mapper) {
    ImmutableDirectedGraph.Builder<R> builder = ImmutableDirectedGraph.builder();
    Map<E, R> convertedTasks =
        original.contents().stream()
            .map(e -> Tuple.of(e, mapper.apply(e)))
            .collect(toImmutableMap());
    original.contents().forEach(
        id -> builder.addNode(convertedTasks.valueOf(id).orElseThrow()));
    original.edges().forEach(
        edge -> builder.addEdge(
            convertedTasks.valueOf(edge.start().item()).orElseThrow(),
            convertedTasks.valueOf(edge.end().item()).orElseThrow()));
    return builder.build();
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static <E> Builder<E> buildUpon(DirectedGraph<E> original) {
    if (original instanceof ImmutableDirectedGraph) {
      return ((ImmutableDirectedGraph<E>) original).toBuilder();
    }
    return new Builder<>(
        original.contents(),
        original.edges()
            .stream()
            .<HashMap<E, HashSet<E>>>collect(
                HashMap::create,
                (map, edge) ->
                    map.putMappingIfAbsent(edge.start().item(), HashSet::create)
                        .add(edge.end().item()),
                (map1, map2) ->
                    map2.entries().forEach(entry -> map1.putMapping(entry.key(), entry.value()))),
        original.edges()
            .stream()
            .<HashMap<E, HashSet<E>>>collect(
                HashMap::create,
                (map, edge) ->
                    map.putMappingIfAbsent(edge.end().item(), HashSet::create)
                        .add(edge.start().item()),
                (map1, map2) ->
                    map2.entries().forEach(entry -> map1.putMapping(entry.key(), entry.value())))
    );
  }

  public Builder<E> toBuilder() {
    return new Builder<>(this.nodes, this.successors, this.predecessors);
  }

  public static final class Builder<E> {
    private final MutableSet<E> nodes;
    private final MutableMap<E, MutableSet<E>> successors;
    private final MutableMap<E, MutableSet<E>> predecessors;

    private Builder() {
      nodes = HashSet.create();
      successors = HashMap.create();
      predecessors = HashMap.create();
    }

    private Builder(
        Set<E> nodes, Map<E, ? extends Set<E>> successors, Map<E, ? extends Set<E>> predecessors) {
      this.nodes = HashSet.copyOf(nodes);
      this.successors = deepCopy(successors);
      this.predecessors = deepCopy(predecessors);
    }

    private static <E> MutableMap<E, MutableSet<E>> deepCopy(Map<E, ? extends Set<E>> other) {
      return other.entries()
          .stream()
          .collect(toHashMap(Map.Entry::key, entry -> HashSet.copyOf(entry.value())));
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
      deepRemove(successors, element);
      deepRemove(predecessors, element);
      return this;
    }

    public Builder<E> replaceNode(E original, E replacement) {
      requireNonNull(original);
      requireNonNull(replacement);

      if (!nodes.contains(original)) {
        throw new UnknownNodeException(original);
      }
      if (nodes.contains(replacement)) {
        throw new DuplicateNodeException(original, replacement);
      }

      deepReplace(successors, original, replacement);
      deepReplace(predecessors, original, replacement);
      nodes.remove(original);
      nodes.add(replacement);

      return this;
    }

    private static <T> void deepRemove(MutableMap<T, MutableSet<T>> map, Object item) {
      map.removeUnknownTypedKey(item);
      map.values().forEach(list -> list.removeUnknownTyped(item));
    }

    private static <T> void deepReplace(
        MutableMap<T, MutableSet<T>> map, T original, T replacement) {
      map.removeKey(original).ifPresent(set -> map.putMapping(replacement, set));
      map.values().forEach(set -> {
        if (set.removeUnknownTyped(original)) {
          set.add(replacement);
        }
      });
    }

    public Builder<E> addEdge(E from, E to) {
      requireNonNull(from);
      requireNonNull(to);
      if (!nodes.contains(from)) {
        throw new UnknownNodeException(from);
      }
      if (!nodes.contains(to)) {
        throw new UnknownNodeException(to);
      }
      successors.putMappingIfAbsent(from, HashSet::create).add(to);
      predecessors.putMappingIfAbsent(to, HashSet::create).add(from);
      return this;
    }

    public Builder<E> removeEdge(E from, E to) {
      return removeEdgeUnknownEdge(from, to);
    }

    public Builder<E> removeEdgeUnknownEdge(Object from, Object to) {
      requireNonNull(from);
      requireNonNull(to);
      successors.valueOfUnknownTyped(from).ifPresent(set -> set.removeUnknownTyped(to));
      predecessors.valueOfUnknownTyped(to).ifPresent(set -> set.removeUnknownTyped(from));
      return this;
    }

    public ImmutableDirectedGraph<E> build() {
      return new ImmutableDirectedGraph<>(this);
    }
  }

  private ImmutableDirectedGraph() {
    nodes = ImmutableSet.empty();
    neighbors = ImmutableMap.empty();
    successors = ImmutableMap.empty();
    predecessors = ImmutableMap.empty();
  }

  private ImmutableDirectedGraph(Builder<E> builder) {
    nodes = ImmutableSet.copyOf(builder.nodes);
    successors = deepCopy(builder.successors);
    predecessors = deepCopy(builder.predecessors);
    neighbors = deepCopy(
        Stream.concat(successors.entries().stream(), predecessors.entries().stream())
            .<HashMap<E, HashSet<E>>>collect(
                HashMap::create,
                (map, entry) ->
                    map.putMappingIfAbsent(entry.key(), HashSet::create).addAll(entry.value()),
                (map1, map2) ->
                    map2.entries()
                        .forEach(
                            entry ->
                                map1.putMappingIfAbsent(entry.key(), HashSet::create)
                                    .addAll(entry.value()))));

  }

  private static <E> ImmutableMap<E, ImmutableSet<E>> deepCopy(Map<E, ? extends Set<E>> other) {
    return other.entries()
        .stream()
        .map(entry -> Tuple.of(entry.key(), ImmutableSet.copyOf(entry.value())))
        .collect(toImmutableMap());
  }

  @Override
  public Optional<? extends DirectedNode> nodeOf(Object item) {
    @SuppressWarnings("unchecked")
    Optional<? extends DirectedNode> node =
        nodes.containsUnknownTyped(item)
            ? Optional.of(getOrCreateNode((E) item))
            : Optional.empty();
    return node;
  }

  @Override
  public Optional<? extends DirectedEdge> edgeOf(Object from, Object to) {
    @SuppressWarnings("unchecked")
    Optional<? extends DirectedEdge> edge =
        successors.valueOfUnknownTyped(from)
            .filter(set -> set.containsUnknownTyped(to))
            .map(u -> Tuplet.of((E) from, (E) to))
            .map(toEdge());
    return edge;
  }

  @Override
  public Set<E> contents() {
    return nodes;
  }

  @Override
  public Set<? extends DirectedNode> nodes() {
    return nodes.stream().map(toNode()).collect(toSet());
  }

  @Override
  public Set<? extends DirectedEdge> edges() {
    return successors.entries()
        .stream()
        .<Couplet<E>>flatMap(ImmutableDirectedGraph::toCouplets)
        .map(toEdge())
        .collect(toSet());
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
      return ImmutableSet.<DirectedEdge>builder()
          .addAll(outgoingEdges())
          .addAll(incomingEdges())
          .build();
    }

    @Override
    public Set<? extends DirectedEdge> outgoingEdges() {
      return ImmutableDirectedGraph.this.successors.valueOf(item)
          .orElse(ImmutableSet.empty())
          .stream()
          .map(to -> Tuplet.of(item, to))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toImmutableSet());
    }

    @Override
    public Set<? extends DirectedEdge> incomingEdges() {
      return ImmutableDirectedGraph.this.predecessors.valueOf(item)
          .orElse(ImmutableSet.empty())
          .stream()
          .map(from -> Tuplet.of(from, item))
          .map(ImmutableDirectedGraph.this.toEdge())
          .collect(toImmutableSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> neighbors() {
      return ImmutableDirectedGraph.this.neighbors.valueOf(item)
          .orElse(ImmutableSet.empty())
          .stream()
          .map(toNode())
          .collect(toImmutableSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> successors() {
      return ImmutableDirectedGraph.this.successors.valueOf(item)
          .orElse(ImmutableSet.empty())
          .stream()
          .map(toNode())
          .collect(toImmutableSet());
    }

    @Override
    public Set<? extends DirectedGraph.DirectedNode<E>> predecessors() {
      return ImmutableDirectedGraph.this.predecessors.valueOf(item)
          .orElse(ImmutableSet.empty())
          .stream()
          .map(toNode())
          .collect(toImmutableSet());
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
    private final Couplet<? extends E> endpoints;

    private DirectedEdge(Couplet<? extends E> endpoints) {
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

  private final WeakCache<E, DirectedNode> nodeCache = new WeakCache<>();

  private DirectedNode getOrCreateNode(E item) {
    return nodeCache.getOrCache(item, () -> new DirectedNode(item));
  }

  private static <T> Stream<Couplet<T>> toCouplets(Map.Entry<T, ? extends Set<T>> entry) {
    return entry.value().stream().map(value -> Tuplet.of(entry.key(), value));
  }

  private Function<? super Couplet<? extends E>, ? extends DirectedEdge> toEdge() {
    return this::getOrCreateEdge;
  }

  private final WeakCache<Couplet<? extends E>, DirectedEdge> edgeCache = new WeakCache<>();

  private DirectedEdge getOrCreateEdge(Couplet<? extends E> endpoints) {
    return edgeCache.getOrCache(endpoints, () -> new DirectedEdge(endpoints));
  }

  public static final class UnknownNodeException extends IllegalStateException {
    private UnknownNodeException(Object node) {
      super("Graph does not contain the specified node: " + node);
    }
  }

  public static final class DuplicateNodeException extends IllegalStateException {
    private DuplicateNodeException(Object original, Object replacement) {
      super(
          "Attempt to replace an existing node with itself or an equal node. Original: "
              + original
              + ", replacement: "
              + replacement);
    }
  }
}
