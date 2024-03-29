package omnia.data.structure.immutable

import omnia.algorithm.HashAlgorithms.Companion.hash
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

class ImmutableDirectedGraph<E: Any>: DirectedGraph<E> {

  override val contents: ImmutableSet<E>
  /** Simply the merger of [successorMap] and [predecessorMap]. */
  private val neighborMap: ImmutableMap<E, ImmutableSet<E>>
  /** The canonical map of edges. */
  private val successorMap: ImmutableMap<E, ImmutableSet<E>>
  /** Simply the inverse of [successorMap]. */
  private val predecessorMap: ImmutableMap<E, ImmutableSet<E>>

  fun toBuilder(): Builder<E> {
    return Builder(contents, successorMap, predecessorMap)
  }

  class Builder<E: Any> {

    val nodes: MutableSet<E>
    val successors: MutableMap<E, MutableSet<E>>
    val predecessors: MutableMap<E, MutableSet<E>>

    constructor() {
      nodes = HashSet.create()
      successors = HashMap.create()
      predecessors = HashMap.create()
    }

    constructor(nodes: Set<out E>, successors: Map<E, out Set<E>>, predecessors: Map<E, out Set<E>>) {
      this.nodes = HashSet.copyOf(nodes)
      this.successors = successors.deepCopy()
      this.predecessors = predecessors.deepCopy()
    }

    fun addNode(element: E): Builder<E> {
      nodes.add(element)
      return this
    }

    fun removeNode(element: E) = removeUnknownTypedNode(element)

    fun removeUnknownTypedNode(element: Any?): Builder<E> {
      nodes.removeUnknownTyped(element)
      val removedSuccessors = successors.removeUnknownTypedKey(element)
      val removedPredecessors = predecessors.removeUnknownTypedKey(element)
      removedPredecessors?.forEach { predecessor ->
        successors.valueOf(predecessor)?.removeUnknownTyped(element)
      }
      removedSuccessors?.forEach { successor ->
        predecessors.valueOf(successor)?.removeUnknownTyped(element)
      }
      return this
    }

    fun removeNodeAndConnectNeighbors(element: E) = removeUnknownTypedNodeAndConnectNeighbors(element)

    fun removeUnknownTypedNodeAndConnectNeighbors(element: Any?): Builder<E> {
      val elementPredecessors = predecessors.valueOfUnknownTyped(element)?.toImmutableSet() ?: ImmutableSet.empty()
      val elementSuccessors = successors.valueOfUnknownTyped(element)?.toImmutableSet() ?: ImmutableSet.empty()
      this.removeUnknownTypedNode(element)
      elementPredecessors.forEach { predecessor ->
        elementSuccessors.forEach { successor ->
          this.addEdge(predecessor, successor)
        }
      }
      return this
    }

    fun replaceNode(original: E, replacement: E): Builder<E> {
      if (!nodes.contains(original)) {
        throw UnknownNodeException(original)
      }
      if (nodes.contains(replacement)) {
        throw DuplicateNodeException(original, replacement)
      }
      val removedSuccessors = successors.removeKey(original)
      val removedPredecessors = predecessors.removeKey(original)
      removedSuccessors?.forEach { successor ->
        predecessors.valueOf(successor)
          ?.takeIf { it.remove(original) }
          ?.add(replacement)
      }
      removedPredecessors?.forEach { predecessor ->
        successors.valueOf(predecessor)
          ?.takeIf { it.remove(original) }
          ?.add(replacement)
      }
      removedSuccessors
        ?.map { if (it == original) replacement else it }
        ?.toHashSet()
        ?.also { successors.putMapping(replacement, it) }
      removedPredecessors
        ?.map { if (it == original) replacement else it }
        ?.toHashSet()
        ?.also { predecessors.putMapping(replacement, it) }
      nodes.remove(original)
      nodes.add(replacement)
      return this
    }

    fun addEdge(from: E, to: E): Builder<E> {
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

    fun removeEdge(from: E, to: E) = removeEdgeUnknownEdge(from, to)

    fun removeEdgeUnknownEdge(from: Any?, to: Any?): Builder<E> {
      successors.valueOfUnknownTyped(from)?.removeUnknownTyped(to)
      predecessors.valueOfUnknownTyped(to)?.removeUnknownTyped(from)
      return this
    }

    fun build() = ImmutableDirectedGraph(this)

    companion object {

      private fun <E: Any> Map<E, out Set<E>>.deepCopy(): MutableMap<E, MutableSet<E>> {
        return entries.toHashMap({ it.key }, { HashSet.copyOf(it.value) })
      }
    }
  }

  private constructor() {
    contents = ImmutableSet.empty()
    neighborMap = ImmutableMap.empty()
    successorMap = ImmutableMap.empty()
    predecessorMap = ImmutableMap.empty()
  }

  private constructor(builder: Builder<E>) {
    contents = ImmutableSet.copyOf(builder.nodes)
    successorMap = deepCopy(builder.successors)
    predecessorMap = deepCopy(builder.predecessors)
    neighborMap = deepCopy(
      successorMap.entries
        .plus(predecessorMap.entries)
        .groupBy({ it.key }, { it.value })
        .entries
        .toImmutableMap({ it.key }, { it.value.flatten().toImmutableSet() }))
  }

  override fun nodeOf(item: E): DirectedNode? {
    return nodeOfUnknownType(item)
  }

  override fun nodeOfUnknownType(item: Any?): DirectedNode? {
    @Suppress("UNCHECKED_CAST")
    return if (contents.containsUnknownTyped(item)) getOrCreateNode(item as E) else null
  }

  override fun edgeOf(from: E, to: E): DirectedEdge? {
    return edgeOfUnknownType(from, to)
  }

  override fun edgeOfUnknownType(from: Any?, to: Any?): DirectedEdge? {
    val edges = successorMap.valueOfUnknownTyped(from) ?: return null
    if (!edges.containsUnknownTyped(to)) {
      return null
    }
    @Suppress("UNCHECKED_CAST")
    return getOrCreateEdge(from as E, to as E)
  }

  override val nodes: ImmutableSet<DirectedNode> get() {
    return contents.map(toNode()).toImmutableSet()
  }

  override val edges: ImmutableSet<DirectedEdge> get() {
    return successorMap.entries
      .flatMap(::toCouplets)
      .map(toEdge())
      .toImmutableSet()
  }

  override fun equals(other: Any?) =
    other === this
    || other is ImmutableDirectedGraph<*>
    && other.contents == this.contents
    && other.successorMap == this.successorMap

  override fun hashCode() = hash(contents, successorMap)

  inner class DirectedNode internal constructor(override val item: E) :
    DirectedGraph.DirectedNode<E> {

    override val edges: ImmutableSet<DirectedEdge>
      get() {
        return ImmutableSet.builder<DirectedEdge>()
          .addAll(outgoingEdges)
          .addAll(incomingEdges)
          .build()
      }

    override val outgoingEdges: ImmutableSet<DirectedEdge>
      get() {
        return (successorMap.valueOf(item) ?: ImmutableSet.empty())
          .map { Tuplet.of(item, it) }
          .map(toEdge())
          .toImmutableSet()
      }

    override val incomingEdges: ImmutableSet<DirectedEdge>
      get() {
        return (predecessorMap.valueOf(item) ?: ImmutableSet.empty())
          .map { Tuplet.of(it, item) }
          .map(toEdge())
          .toImmutableSet()
      }

    override val neighbors: ImmutableSet<DirectedNode>
      get() {
        return (neighborMap.valueOf(item) ?: ImmutableSet.empty()).map(toNode()).toImmutableSet()
      }

    override val successors: ImmutableSet<DirectedNode>
      get() {
        return (successorMap.valueOf(item) ?: ImmutableSet.empty()).map(toNode()).toImmutableSet()
      }

    override val predecessors: Set<DirectedNode>
      get() {
        return (predecessorMap.valueOf(item) ?: ImmutableSet.empty()).map(toNode()).toImmutableSet()
      }

    override fun equals(other: Any?): Boolean {
      return other is ImmutableDirectedGraph<*>.DirectedNode
          && other.graph() == graph()
          && item == other.item
    }

    override fun hashCode() = hash(item)

    override fun toString() = "ImmutableDirectedNode{$item}"

    private fun graph() = this@ImmutableDirectedGraph
  }

  inner class DirectedEdge internal constructor(private val endpointContents: Couplet<out E>) :
    DirectedGraph.DirectedEdge<E> {

    override val start: DirectedGraph.DirectedNode<E>
      get() {
        return getOrCreateNode(endpointContents.first)
      }

    override val end: DirectedGraph.DirectedNode<E>
      get() {
        return getOrCreateNode(endpointContents.second)
      }

    override val endpoints: Couplet<DirectedNode>
      get() {
        return Tuplet.of(
          getOrCreateNode(endpointContents.first),
          getOrCreateNode(endpointContents.second)
        )
      }

    override fun equals(other: Any?): Boolean {
      return other is ImmutableDirectedGraph<*>.DirectedEdge
          && other.graph() === graph()
          && endpointContents == other.endpointContents
    }

    override fun hashCode() = hash(endpointContents)

    override fun toString() =
      "ImmutableDirectedEdge{from=${endpointContents.first}, to=${endpointContents.second}}"

    private fun graph() = this@ImmutableDirectedGraph
  }

  private fun toNode(): (E) -> DirectedNode {
    return { item: E -> getOrCreateNode(item) }
  }

  private fun getOrCreateNode(item: E): DirectedNode {
    return DirectedNode(item)
  }

  private fun toEdge(): (Couplet<out E>) -> DirectedEdge {
    return { getOrCreateEdge(it) }
  }

  private fun getOrCreateEdge(from: E, to: E): DirectedEdge {
    return getOrCreateEdge(Tuplet.of(from, to))
  }

  private fun getOrCreateEdge(endpoints: Couplet<out E>): DirectedEdge {
    return DirectedEdge(endpoints)
  }

  class UnknownNodeException(node: Any?) :
    IllegalStateException("Graph does not contain the specified node: $node") {

    companion object {

      private const val serialVersionUID = -533435014961799617L
    }
  }

  class DuplicateNodeException(original: Any?, replacement: Any?): IllegalStateException(
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

    fun <E: Any> empty(): ImmutableDirectedGraph<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_DIRECTED_GRAPH as ImmutableDirectedGraph<E>
    }

    fun <E: Any> copyOf(original: DirectedGraph<E>): ImmutableDirectedGraph<E> {
      return if (original is ImmutableDirectedGraph<E>) original else buildUpon(original).build()
    }

    fun <E: Any, R: Any> copyOf(original: DirectedGraph<E>, mapper: (E) -> R): ImmutableDirectedGraph<R> {
      val builder: Builder<R> = builder()
      val convertedTasks: Map<E, R> = original.contents
        .map { Tuple.of(it, mapper(it)) }
        .toImmutableMap()
      original.contents.forEach { id -> builder.addNode(convertedTasks.valueOf(id)!!) }
      original.edges.forEach {
        builder.addEdge(
          convertedTasks.valueOf(it.start.item)!!,
          convertedTasks.valueOf(it.end.item)!!
        )
      }
      return builder.build()
    }

    fun <E: Any> builder(): Builder<E> {
      return Builder()
    }

    fun <E: Any> buildUpon(original: DirectedGraph<out E>): Builder<E> {
      return if (original is ImmutableDirectedGraph<*>) {
        @Suppress("UNCHECKED_CAST")
        (original as ImmutableDirectedGraph<E>).toBuilder()
      } else Builder(
        original.contents,
        original.edges
          .groupBy({ it.start.item }, { it.end.item })
          .entries
          .toHashMap({ it.key }, { it.value.toHashSet() }),
        original.edges
          .groupBy({ it.end.item }, { it.start.item })
          .entries
          .toHashMap({ it.key }, { it.value.toHashSet() }))
    }

    private fun <E: Any> deepCopy(other: Map<E, out Set<E>>): ImmutableMap<E, ImmutableSet<E>> {
      return other.entries
        .map { Tuple.of(it.key, ImmutableSet.copyOf(it.value)) }
        .toImmutableMap()
    }

    private fun <T: Any> toCouplets(entry: Map.Entry<T, Set<T>>): Iterable<Couplet<T>> {
      return entry.value.map { Tuplet.of(entry.key, it) }
    }
  }
}
