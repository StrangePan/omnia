package omnia.data.structure.immutable

import java.util.Objects
import java.util.Objects.requireNonNull
import java.util.function.Consumer
import java.util.function.Function
import omnia.data.cache.WeakCache
import omnia.data.structure.DirectedGraph
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableMap.Companion.toImmutableMap
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.mutable.HashMap
import omnia.data.structure.mutable.HashMap.Companion.toHashMap
import omnia.data.structure.mutable.HashSet
import omnia.data.structure.mutable.HashSet.Companion.toHashSet
import omnia.data.structure.mutable.MutableMap
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.tuple.Couplet
import omnia.data.structure.tuple.Tuple
import omnia.data.structure.tuple.Tuplet

class ImmutableDirectedGraph<E : Any> : DirectedGraph<E> {

  private val nodes: ImmutableSet<E>
  private val neighbors: ImmutableMap<E, ImmutableSet<E>>
  private val successors: ImmutableMap<E, ImmutableSet<E>>
  private val predecessors: ImmutableMap<E, ImmutableSet<E>>

  fun toBuilder(): Builder<E> {
    return Builder(nodes, successors, predecessors)
  }

  class Builder<E : Any> {

    val nodes: MutableSet<E>
    val successors: MutableMap<E, MutableSet<E>>
    val predecessors: MutableMap<E, MutableSet<E>>

    constructor() {
      nodes = HashSet.create()
      successors = HashMap.create()
      predecessors = HashMap.create()
    }

    constructor(
      nodes: Set<out E>, successors: Map<E, out Set<E>>, predecessors: Map<E, out Set<E>>,
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
      successors.putMappingIfAbsent(from, HashSet.Companion::create).add(to)
      predecessors.putMappingIfAbsent(to, HashSet.Companion::create).add(from)
      return this
    }

    fun removeEdge(from: E, to: E): Builder<E> {
      return removeEdgeUnknownEdge(from, to)
    }

    fun removeEdgeUnknownEdge(from: Any?, to: Any?): Builder<E> {
      requireNonNull(from)
      requireNonNull(to)
      successors.valueOfUnknownTyped(from)?.removeUnknownTyped(to)
      predecessors.valueOfUnknownTyped(to)?.removeUnknownTyped(from)
      return this
    }

    fun build(): ImmutableDirectedGraph<E> {
      return ImmutableDirectedGraph(this)
    }

    companion object {

      private fun <E : Any> deepCopy(other: Map<E, out Set<E>>): MutableMap<E, MutableSet<E>> {
        return other.entries().toHashMap({ it.key() }, { HashSet.copyOf(it.value()) })
      }

      private fun <T : Any> deepRemove(map: MutableMap<T, MutableSet<T>>, item: Any?) {
        map.removeUnknownTypedKey(item)
        map.values().forEach(Consumer { list: MutableSet<T> -> list.removeUnknownTyped(item) })
      }

      private fun <T : Any> deepReplace(
        map: MutableMap<T, MutableSet<T>>, original: T, replacement: T,
      ) {
        map.removeKey(original)?.let { set -> map.putMapping(replacement, set) }
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
      successors.entries()
        .plus(predecessors.entries())
        .groupBy({ it.key() }, { it.value() })
        .entries
        .toImmutableMap({ it.key }, { it.value.flatten().toImmutableSet() }))
  }

  override fun nodeOf(item: E): DirectedNode? {
    return nodeOfUnknownType(item)
  }

  override fun nodeOfUnknownType(item: Any?): DirectedNode? {
    @Suppress("UNCHECKED_CAST")
    return if (nodes.containsUnknownTyped(item)) getOrCreateNode(item as E) else null
  }

  override fun edgeOf(from: E, to: E): DirectedEdge? {
    return edgeOfUnknownType(from, to)
  }

  override fun edgeOfUnknownType(from: Any?, to: Any?): DirectedEdge? {
    val edges = successors.valueOfUnknownTyped(from) ?: return null
    if (!edges.containsUnknownTyped(to)) {
      return null
    }
    @Suppress("UNCHECKED_CAST")
    return getOrCreateEdge(from as E, to as E)
  }

  override fun contents(): ImmutableSet<E> {
    return nodes
  }

  override fun nodes(): ImmutableSet<DirectedNode> {
    return nodes.map(toNode()).toImmutableSet()
  }

  override fun edges(): ImmutableSet<DirectedEdge> {
    return successors.entries()
      .flatMap(::toCouplets)
      .map(toEdge())
      .toImmutableSet()
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
      return (successors.valueOf(item) ?: ImmutableSet.empty())
        .map { Tuplet.of(item, it) }
        .map(toEdge())
        .toImmutableSet()
    }

    override fun incomingEdges(): ImmutableSet<DirectedEdge> {
      return (predecessors.valueOf(item) ?: ImmutableSet.empty())
        .map { Tuplet.of(it, item) }
        .map(toEdge())
        .toImmutableSet()
    }

    override fun neighbors(): ImmutableSet<DirectedNode> {
      return (neighbors.valueOf(item) ?: ImmutableSet.empty()).map(toNode()).toImmutableSet()
    }

    override fun successors(): ImmutableSet<DirectedNode> {
      return (successors.valueOf(item) ?: ImmutableSet.empty()).map(toNode()).toImmutableSet()
    }

    override fun predecessors(): Set<DirectedNode> {
      return (predecessors.valueOf(item) ?: ImmutableSet.empty()).map(toNode()).toImmutableSet()
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

  private fun toNode(): (E) -> DirectedNode {
    return { item: E -> getOrCreateNode(item) }
  }

  private val nodeCache: WeakCache<E, DirectedNode> = WeakCache()
  private fun getOrCreateNode(item: E): DirectedNode {
    return nodeCache.getOrCache(item) { DirectedNode(item) }
  }

  private fun toEdge(): (Couplet<out E>) -> DirectedEdge {
    return { getOrCreateEdge(it) }
  }

  private val edgeCache: WeakCache<Couplet<out E>, DirectedEdge> = WeakCache()

  private fun getOrCreateEdge(from: E, to: E): DirectedEdge {
    return getOrCreateEdge(Tuplet.of(from, to))
  }

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
    fun <E : Any> empty(): ImmutableDirectedGraph<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_DIRECTED_GRAPH as ImmutableDirectedGraph<E>
    }

    @JvmStatic
    fun <E : Any> copyOf(original: DirectedGraph<E>): ImmutableDirectedGraph<E> {
      return buildUpon(original).build()
    }

    @JvmStatic
    fun <E : Any, R : Any> copyOf(
      original: DirectedGraph<E>, mapper: Function<in E, out R>,
    ): ImmutableDirectedGraph<R> {
      val builder: Builder<R> = builder()
      val convertedTasks: Map<E, R> = original.contents()
        .map { Tuple.of(it, mapper.apply(it)) }
        .toImmutableMap()
      original.contents().forEach { id -> builder.addNode(convertedTasks.valueOf(id)!!) }
      original.edges().forEach {
        builder.addEdge(
          convertedTasks.valueOf(it.start().item())!!,
          convertedTasks.valueOf(it.end().item())!!
        )
      }
      return builder.build()
    }

    @JvmStatic
    fun <E : Any> builder(): Builder<E> {
      return Builder()
    }

    @JvmStatic
    fun <E : Any> buildUpon(original: DirectedGraph<out E>): Builder<E> {
      return if (original is ImmutableDirectedGraph<*>) {
        @Suppress("UNCHECKED_CAST")
        (original as ImmutableDirectedGraph<E>).toBuilder()
      } else Builder(
        original.contents(),
        original.edges()
          .groupBy({ it.start().item() }, { it.end().item() })
          .entries
          .toHashMap({ it.key }, { it.value.toHashSet() }),
        original.edges()
          .groupBy({ it.end().item() }, { it.start().item() })
          .entries
          .toHashMap({ it.key }, { it.value.toHashSet() }))
    }

    private fun <E : Any> deepCopy(other: Map<E, out Set<E>>): ImmutableMap<E, ImmutableSet<E>> {
      return other.entries()
        .map { Tuple.of(it.key(), ImmutableSet.copyOf(it.value())) }
        .toImmutableMap()
    }

    private fun <T : Any> toCouplets(entry: Map.Entry<T, out Set<T>>): Iterable<Couplet<T>> {
      return entry.value().map { Tuplet.of(entry.key(), it) }
    }
  }
}