package omnia.data.structure.immutable

import java.util.Objects
import java.util.Objects.requireNonNull
import java.util.Optional
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Stream
import omnia.data.cache.WeakCache
import omnia.data.stream.Collectors
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.mutable.HashMap
import omnia.data.structure.mutable.HashSet
import omnia.data.structure.mutable.MutableMap
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.tuple.Couplet
import omnia.data.structure.tuple.Tuple
import omnia.data.structure.tuple.Tuplet

class ImmutableDirectedGraph<E> : DirectedGraph<E> {

  private val nodes: ImmutableSet<E>
  private val neighbors: ImmutableMap<E, ImmutableSet<E>>
  private val successors: ImmutableMap<E, ImmutableSet<E>>
  private val predecessors: ImmutableMap<E, ImmutableSet<E>>

  fun toBuilder(): Builder<E> {
    return Builder(nodes, successors, predecessors)
  }

  class Builder<E> {

    val nodes: MutableSet<E>
    val successors: MutableMap<E, MutableSet<E>>
    val predecessors: MutableMap<E, MutableSet<E>>

    constructor() {
      nodes = HashSet.create()
      successors = HashMap.create()
      predecessors = HashMap.create()
    }

    constructor(
      nodes: Set<E>, successors: Map<E, out Set<E>>, predecessors: Map<E, out Set<E>>,
    ) {
      this.nodes = HashSet.copyOf(nodes)
      this.successors = deepCopy(successors)
      this.predecessors = deepCopy(predecessors)
    }

    fun addNode(element: E): Builder<E> {
      requireNonNull(element)
      nodes.add(element)
      return this
    }

    fun removeNode(element: E): Builder<E> {
      return removeUnknownTypedNode(element)
    }

    fun removeUnknownTypedNode(element: Any?): Builder<E> {
      requireNonNull(element)
      nodes.removeUnknownTyped(element)
      deepRemove(successors, element)
      deepRemove(predecessors, element)
      return this
    }

    fun replaceNode(original: E, replacement: E): Builder<E> {
      requireNonNull(original)
      requireNonNull(replacement)
      if (!nodes.contains(original)) {
        throw UnknownNodeException(original)
      }
      if (nodes.contains(replacement)) {
        throw DuplicateNodeException(original, replacement)
      }
      deepReplace(successors, original, replacement)
      deepReplace(predecessors, original, replacement)
      nodes.remove(original)
      nodes.add(replacement)
      return this
    }

    fun addEdge(from: E, to: E): Builder<E> {
      requireNonNull(from)
      requireNonNull(to)
      if (!nodes.contains(from)) {
        throw UnknownNodeException(from)
      }
      if (!nodes.contains(to)) {
        throw UnknownNodeException(to)
      }
      successors.putMappingIfAbsent(from, { HashSet.create() }).add(to)
      predecessors.putMappingIfAbsent(to, { HashSet.create() }).add(from)
      return this
    }

    fun removeEdge(from: E, to: E): Builder<E> {
      return removeEdgeUnknownEdge(from, to)
    }

    fun removeEdgeUnknownEdge(from: Any?, to: Any?): Builder<E> {
      requireNonNull(from)
      requireNonNull(to)
      successors.valueOfUnknownTyped(from).ifPresent { set -> set.removeUnknownTyped(to) }
      predecessors.valueOfUnknownTyped(to).ifPresent { set -> set.removeUnknownTyped(from) }
      return this
    }

    fun build(): ImmutableDirectedGraph<E> {
      return ImmutableDirectedGraph(this)
    }

    companion object {

      private fun <E> deepCopy(other: Map<E, out Set<E>>): MutableMap<E, MutableSet<E>> {
        return other.entries()
          .stream()
          .collect(
            Collectors.toHashMap(
              { e -> e.key() },
              { entry -> HashSet.copyOf(entry.value()) })
          )
      }

      private fun <T> deepRemove(map: MutableMap<T, MutableSet<T>>, item: Any?) {
        map.removeUnknownTypedKey(item)
        map.values().forEach(Consumer { list: MutableSet<T> -> list.removeUnknownTyped(item) })
      }

      private fun <T> deepReplace(
        map: MutableMap<T, MutableSet<T>>, original: T, replacement: T,
      ) {
        map.removeKey(original).ifPresent { set: MutableSet<T> -> map.putMapping(replacement, set) }
        map.values().forEach(Consumer { set: MutableSet<T> ->
          if (set.removeUnknownTyped(original)) {
            set.add(replacement)
          }
        })
      }
    }
  }

  private constructor() {
    nodes = ImmutableSet.empty()
    neighbors = ImmutableMap.empty()
    successors = ImmutableMap.empty()
    predecessors = ImmutableMap.empty()
  }

  private constructor(builder: Builder<E>) {
    nodes = ImmutableSet.copyOf(builder.nodes)
    successors = deepCopy(builder.successors)
    predecessors = deepCopy(builder.predecessors)
    neighbors = deepCopy(
      Stream.concat(successors.entries().stream(), predecessors.entries().stream())
        .collect<HashMap<E, HashSet<E>>>(
          { HashMap.create() },
          { map, entry ->
            map.putMappingIfAbsent(entry.key(), HashSet.Companion::create)
              .addAll(entry.value())
          },
          { map1, map2 ->
            map2.entries().forEach(Consumer { entry ->
              map1.putMappingIfAbsent(entry.key(), HashSet.Companion::create)
                .addAll(entry.value())
            })
          })
    )
  }

  override fun nodeOf(item: E): Optional<out DirectedNode> {
    return nodeOfUnknownType(item)
  }

  override fun nodeOfUnknownType(item: Any?): Optional<out DirectedNode> {
    @Suppress("UNCHECKED_CAST")
    return if (nodes.containsUnknownTyped(item)) Optional.of(getOrCreateNode(item as E)) else Optional.empty()
  }

  override fun edgeOf(from: E, to: E): Optional<DirectedEdge> {
    return edgeOfUnknownType(from, to)
  }

  override fun edgeOfUnknownType(from: Any?, to: Any?): Optional<DirectedEdge> {
    return successors.valueOfUnknownTyped(from)
      .filter { set -> set.containsUnknownTyped(to) }
      .map {
        @Suppress("UNCHECKED_CAST")
        Tuplet.of(from as E, to as E)
      }
      .map(toEdge())
  }

  override fun contents(): Set<E> {
    return nodes
  }

  override fun nodes(): Set<DirectedNode> {
    return nodes.stream().map(toNode()).collect(Collectors.toSet())
  }

  override fun edges(): Set<DirectedEdge> {
    return successors.entries()
      .stream()
      .flatMap(::toCouplets)
      .map(toEdge())
      .collect(Collectors.toSet())
  }

  inner class DirectedNode internal constructor(private val item: E) :
    DirectedGraph.DirectedNode<E> {

    override fun item(): E {
      return item
    }

    override fun edges(): ImmutableSet<DirectedEdge> {
      return ImmutableSet.builder<DirectedEdge>()
        .addAll(outgoingEdges())
        .addAll(incomingEdges())
        .build()
    }

    override fun outgoingEdges(): ImmutableSet<DirectedEdge> {
      return successors.valueOf(item)
        .orElse(ImmutableSet.empty())
        .stream()
        .map { to: E -> Tuplet.of(item, to) }
        .map(toEdge())
        .collect(Collectors.toImmutableSet())
    }

    override fun incomingEdges(): ImmutableSet<DirectedEdge> {
      return predecessors.valueOf(item)
        .orElse(ImmutableSet.empty())
        .stream()
        .map { from -> Tuplet.of(from, item) }
        .map(toEdge())
        .collect(Collectors.toImmutableSet())
    }

    override fun neighbors(): ImmutableSet<DirectedNode> {
      return neighbors.valueOf(item)
        .orElse(ImmutableSet.empty())
        .stream()
        .map(toNode())
        .collect(Collectors.toImmutableSet())
    }

    override fun successors(): ImmutableSet<DirectedNode> {
      return successors.valueOf(item)
        .orElse(ImmutableSet.empty())
        .stream()
        .map(toNode())
        .collect(Collectors.toImmutableSet())
    }

    override fun predecessors(): Set<DirectedNode> {
      return predecessors.valueOf(item)
        .orElse(ImmutableSet.empty())
        .stream()
        .map(toNode())
        .collect(Collectors.toImmutableSet())
    }

    override fun equals(other: Any?): Boolean {
      return other is ImmutableDirectedGraph<*>.DirectedNode
          && other.graph() == graph()
          && item == other.item
    }

    override fun hashCode(): Int {
      return Objects.hash(item)
    }

    override fun toString(): String {
      return String.format("ImmutableDirectedNode{%s}", item)
    }

    private fun graph(): ImmutableDirectedGraph<E> {
      return this@ImmutableDirectedGraph
    }
  }

  inner class DirectedEdge internal constructor(private val endpoints: Couplet<out E>) :
    DirectedGraph.DirectedEdge<E> {

    override fun start(): DirectedGraph.DirectedNode<E> {
      return getOrCreateNode(endpoints.first())
    }

    override fun end(): DirectedGraph.DirectedNode<E> {
      return getOrCreateNode(endpoints.second())
    }

    override fun endpoints(): Couplet<DirectedNode> {
      return Tuplet.of(
        getOrCreateNode(endpoints.first()),
        getOrCreateNode(endpoints.second())
      )
    }

    override fun equals(other: Any?): Boolean {
      return other is ImmutableDirectedGraph<*>.DirectedEdge
          && other.graph() == graph()
          && endpoints == other.endpoints
    }

    override fun hashCode(): Int {
      return Objects.hash(endpoints)
    }

    override fun toString(): String {
      return String.format(
        "ImmutableDirectedEdge{from=%s, to=%s}",
        endpoints.first(),
        endpoints.second()
      )
    }

    private fun graph(): ImmutableDirectedGraph<E> {
      return this@ImmutableDirectedGraph
    }
  }

  private fun toNode(): Function<in E, out DirectedNode> {
    return Function<E, DirectedNode> { item: E -> getOrCreateNode(item) }
  }

  private val nodeCache: WeakCache<E, DirectedNode> = WeakCache()
  private fun getOrCreateNode(item: E): DirectedNode {
    return nodeCache.getOrCache(item) { DirectedNode(item) }
  }

  private fun toEdge(): Function<in Couplet<out E>, out DirectedEdge> {
    return Function<Couplet<out E>, DirectedEdge> { endpoints: Couplet<out E> ->
      getOrCreateEdge(
        endpoints
      )
    }
  }

  private val edgeCache: WeakCache<Couplet<out E>, DirectedEdge> = WeakCache()
  private fun getOrCreateEdge(endpoints: Couplet<out E>): DirectedEdge {
    return edgeCache.getOrCache(endpoints) { DirectedEdge(endpoints) }
  }

  class UnknownNodeException(node: Any?) :
    IllegalStateException("Graph does not contain the specified node: $node") {

    companion object {

      private const val serialVersionUID = -533435014961799617L
    }
  }

  class DuplicateNodeException(original: Any?, replacement: Any?) : IllegalStateException(
    "Attempt to replace an existing node with itself or an equal node. Original: "
        + original
        + ", replacement: "
        + replacement
  ) {

    companion object {

      private const val serialVersionUID = 1879962996101718193L
    }
  }

  companion object {

    private val EMPTY_IMMUTABLE_DIRECTED_GRAPH: ImmutableDirectedGraph<*> =
      ImmutableDirectedGraph<Any>()

    @JvmStatic
    fun <E> empty(): ImmutableDirectedGraph<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_DIRECTED_GRAPH as ImmutableDirectedGraph<E>
    }

    @JvmStatic
    fun <E> copyOf(original: DirectedGraph<E>): ImmutableDirectedGraph<E> {
      return buildUpon(original).build()
    }

    @JvmStatic
    fun <E, R : Any> copyOf(
      original: DirectedGraph<E>, mapper: Function<in E, out R>,
    ): ImmutableDirectedGraph<R> {
      val builder: Builder<R> = builder()
      val convertedTasks: Map<E, R> = original.contents().stream()
        .map { e -> Tuple.of(e, mapper.apply(e)) }
        .collect(Collectors.toImmutableMap())
      original.contents().forEach(
        Consumer { id: E -> builder.addNode(convertedTasks.valueOf(id).orElseThrow()) })
      original.edges().forEach { edge: DirectedGraph.DirectedEdge<E> ->
        builder.addEdge(
          convertedTasks.valueOf(edge.start().item()).orElseThrow(),
          convertedTasks.valueOf(edge.end().item()).orElseThrow()
        )
      }
      return builder.build()
    }

    @JvmStatic
    fun <E> builder(): Builder<E> {
      return Builder()
    }

    @JvmStatic
    fun <E> buildUpon(original: DirectedGraph<E>): Builder<E> {
      return if (original is ImmutableDirectedGraph<*>) {
        (original as ImmutableDirectedGraph<E>).toBuilder()
      } else Builder(
        original.contents(),
        original.edges()
          .stream()
          .collect(
            { HashMap.create<E, HashSet<E>>() },
            { map, edge ->
              map.putMappingIfAbsent(edge.start().item(), { HashSet.create() })
                .add(edge.end().item())
            },
            { map1, map2 ->
              map2.entries()
                .forEach(Consumer { entry -> map1.putMapping(entry.key(), entry.value()) })
            }),
        original.edges()
          .stream()
          .collect(
            { HashMap.create<E, HashSet<E>>() },
            { map, edge ->
              map.putMappingIfAbsent(edge.end().item(), { HashSet.create() })
                .add(edge.start().item())
            },
            { map1, map2 ->
              map2.entries()
                .forEach(Consumer { entry -> map1.putMapping(entry.key(), entry.value()) })
            })
      )
    }

    private fun <E> deepCopy(other: Map<E, out Set<E>>): ImmutableMap<E, ImmutableSet<E>> {
      return other.entries()
        .stream()
        .map { entry -> Tuple.of(entry.key(), ImmutableSet.copyOf(entry.value())) }
        .collect(Collectors.toImmutableMap())
    }

    private fun <T> toCouplets(entry: Map.Entry<T, out Set<T>>): Stream<Couplet<T>> {
      return entry.value().stream().map { value -> Tuplet.of(entry.key(), value) }
    }
  }
}