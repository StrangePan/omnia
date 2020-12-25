package omnia.data.structure;

import static omnia.data.stream.Collectors.toSet;

import java.util.Optional;
import omnia.data.structure.tuple.Couplet;

/**
 * A {@link Graph} whose edges have a start and an end.
 *
 * @param <E> the type of item that makes up the graph's nodes
 */
public interface DirectedGraph<E> extends Graph<E> {

  interface DirectedNode<E> extends Graph.Node<E> {

    /**
     * The set of edges connected to this node.
     */
    @Override
    Set<? extends DirectedEdge<E>> edges();

    /**
     * The set of edges connected to this node where this node is at the tail / start of the edge.
     * In other words, {@link DirectedEdge#start()} will return this node.
     *
     * <pre>{@code
     *  [this node] -> [...]
     * }</pre>
     *
     * @see #successors()
     */
    Set<? extends DirectedEdge<E>> outgoingEdges();

    /**
     * The set of edges connected to this node where this node is at the head / end of this edge.
     * In other words, {@link DirectedEdge#end()} will return this node.
     *
     * <pre>{@code
     *  [...] -> [this node]
     * }</pre>
     *
     * @see #predecessors()
     */
    Set<? extends DirectedEdge<E>> incomingEdges();

    /**
     * The set of nodes connected to this node. If this node is connected to itself, then this set
     * will contain this node.
     */
    @Override
    Set<? extends DirectedNode<E>> neighbors();

    /**
     * The set of nodes connected to this node via an edge where this node is at the tail / start
     * of the edge. In other words, {@link DirectedEdge#start()} will return this node. If this node
     * is connected to itself via a circular edge, then this set will contain this node.
     *
     * <pre>{@code
     *  [this node] -> [...]
     * }</pre>
     *
     * @see #outgoingEdges()
     */
    Set<? extends DirectedNode<E>> successors();

    /**
     * The set of nodes connected to this node via an edge where this node is at the head / end
     * of the edge. In other words, {@link DirectedEdge#end()} will return this node. If this node
     * is connected to itself via a circular edge, then this set will contain this node.
     *
     * <pre>{@code
     *  [...] -> [this node]
     * }</pre>
     */
    Set<? extends DirectedNode<E>> predecessors();
  }

  interface DirectedEdge<E> extends Graph.Edge<E> {

    /**
     * The node at the head / start of this edge.
     *
     * <pre>{@code
     *  [this node] -> [...]
     * }</pre>
     */
    DirectedNode<E> start();

    /**
     * The node at the tail / end of this edge.
     *
     * <pre>{@code
     *  [...] -> [this node]
     * }</pre>
     */
    DirectedNode<E> end();

    /**
     * The two nodes that compose this edge. {@link Couplet#first()} returns the head / start of
     * this edge like {@link #start()}, while {@link Couplet#second()} returns the tail / end of
     * this edge like {@link #end()}.
     */
    @Override
    Couplet<? extends DirectedNode<E>> endpoints();
  }

  /**
   * Retrieves the {@link DirectedNode} representation of the given item in the graph, if the graph
   * contains this item. If this graph does not contain a node for the provided item, the empty
   * Optional is returned.
   */
  @Override
  Optional<? extends DirectedNode<E>> nodeOf(E item);

  @Override
  Optional<? extends DirectedNode<E>> nodeOfUnknownType(Object item);

  /**
   * Retrieves the {@link DirectedEdge} representation of the given items in the graph. In a
   * {@link DirectedGraph}, this means that the first parameter is the head / start of the edge, and
   * the second parameter is the tail / end of the edge. If this graph does not contain an edge for
   * the provided item, the empty Optional is returned.

   * @param from the starting node of the edge
   * @param to the ending node of the edge
   */
  Optional<? extends DirectedEdge<E>> edgeOf(E from, E to);

  Optional<? extends DirectedEdge<E>> edgeOfUnknownType(Object from, Object to);

  /** All nodes contained in the graph. */
  @Override
  Set<? extends DirectedNode<E>> nodes();

  /** All edges contained in the graph. */
  @Override
  Set<? extends DirectedEdge<E>> edges();

  /**
   * Compares if two {@link DirectedGraph}s are equal per this interface's definitions. This is a
   * more reliable method for comparing directed graphs than {@link Object#equals}.
   *
   * The following conditions are required for two graphs to be considered equal:
   *
   * <ol>
   *   <li>both graphs contain equal sets of nodes</li>
   *   <li>both graphs contain equal sets of edges</li>
   * </ol>
   */
  static boolean areEqual(DirectedGraph<?> a, DirectedGraph<?> b) {
    return a == b
        || a != null
        && b != null
        && Set.areEqual(a.contents(), b.contents())
        && Set.areEqual(
            a.edges().stream()
                .map(DirectedEdge::endpoints)
                .map(couplet -> couplet.map(DirectedNode::item))
                .collect(toSet()),
            b.edges().stream()
                .map(DirectedEdge::endpoints)
                .map(couplet -> couplet.map(DirectedNode::item))
                .collect(toSet()));
  }
}
