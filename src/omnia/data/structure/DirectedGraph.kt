package omnia.data.structure

import java.util.function.Function
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.tuple.Couplet

/**
 * A [Graph] whose edges have a start and an end.
 *
 * @param E the type of item that makes up the graph's nodes
 */
interface DirectedGraph<E : Any> : Graph<E> {

  interface DirectedNode<E : Any> : Graph.Node<E> {

    /**
     * The set of edges connected to this node.
     */
    override fun edges(): Set<out DirectedEdge<E>>

    /**
     * The set of edges connected to this node where this node is at the tail / start of the edge.
     * In other words, [DirectedEdge.start] will return this node.
     *
     * <pre>`[this node] -> [..]
    `</pre> *
     *
     * @see successors
     */
    fun outgoingEdges(): Set<out DirectedEdge<E>>

    /**
     * The set of edges connected to this node where this node is at the head / end of this edge.
     * In other words, [DirectedEdge.end] will return this node.
     *
     * <pre>`[..] -> [this node]
    `</pre> *
     *
     * @see predecessors
     */
    fun incomingEdges(): Set<out DirectedEdge<E>>

    /**
     * The set of nodes connected to this node. If this node is connected to itself, then this set
     * will contain this node.
     */
    override fun neighbors(): Set<out DirectedNode<E>>

    /**
     * The set of nodes connected to this node via an edge where this node is at the tail / start
     * of the edge. In other words, [DirectedEdge.start] will return this node. If this node
     * is connected to itself via a circular edge, then this set will contain this node.
     *
     * <pre>`[this node] -> [..]
    `</pre> *
     *
     * @see outgoingEdges
     */
    fun successors(): Set<out DirectedNode<E>>

    /**
     * The set of nodes connected to this node via an edge where this node is at the head / end
     * of the edge. In other words, [DirectedEdge.end] will return this node. If this node
     * is connected to itself via a circular edge, then this set will contain this node.
     *
     * <pre>`[..] -> [this node]
    `</pre> *
     */
    fun predecessors(): Set<out DirectedNode<E>>
  }

  interface DirectedEdge<E : Any> : Graph.Edge<E> {

    /**
     * The node at the head / start of this edge.
     *
     * <pre>`[this node] -> [..]
    `</pre> *
     */
    fun start(): DirectedNode<E>

    /**
     * The node at the tail / end of this edge.
     *
     * <pre>`[..] -> [this node]
    `</pre> *
     */
    fun end(): DirectedNode<E>

    /**
     * The two nodes that compose this edge. [Couplet.first] returns the head / start of
     * this edge like [start], while [Couplet.second] returns the tail / end of
     * this edge like [end].
     */
    override fun endpoints(): Couplet<out DirectedNode<E>>
  }

  /**
   * Retrieves the [DirectedNode] representation of the given item in the graph, if the graph
   * contains this item. If this graph does not contain a node for the provided item, the empty
   * Optional is returned.
   */
  override fun nodeOf(item: E): DirectedNode<E>?

  override fun nodeOfUnknownType(item: Any?): DirectedNode<E>?

  /**
   * Retrieves the [DirectedEdge] representation of the given items in the graph. In a
   * [DirectedGraph], this means that the first parameter is the head / start of the edge, and
   * the second parameter is the tail / end of the edge. If this graph does not contain an edge for
   * the provided item, the empty Optional is returned.
   *
   * @param from the starting node of the edge
   * @param to the ending node of the edge
   */
  fun edgeOf(from: E, to: E): DirectedEdge<E>?

  fun edgeOfUnknownType(from: Any?, to: Any?): DirectedEdge<E>?

  /** All nodes contained in the graph.  */
  override fun nodes(): Set<out DirectedNode<E>>

  /** All edges contained in the graph.  */
  override fun edges(): Set<out DirectedEdge<E>>

  companion object {

    /**
     * Compares if two [DirectedGraph]s are equal per this interface's definitions. This is a
     * more reliable method for comparing directed graphs than [Object.equals].
     *
     * The following conditions are required for two graphs to be considered equal:
     *  1. both graphs contain equal sets of nodes
     *  2. both graphs contain equal sets of edges
     */
    fun <T> areEqual(a: DirectedGraph<out Any>?, b: DirectedGraph<out Any>?): Boolean {
      return (a === b
          || (a != null && b != null && Set.areEqual(a.contents(), b.contents())
          && Set.areEqual(
              a.edges()
                .map { it.endpoints() }
                .map { it.map(Function { node -> node.item() }) }
                .toImmutableSet(),
              b.edges()
                .map { it.endpoints() }
                .map { it.map(Function { node -> node.item() }) }
                .toImmutableSet())))
    }
  }
}