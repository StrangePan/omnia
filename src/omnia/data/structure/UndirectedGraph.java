package omnia.data.structure;

import java.util.Optional;

public interface UndirectedGraph<E> extends Graph<E> {

  interface UndirectedNode<E> extends Graph.Node<E> {

    @Override
    Set<? extends UndirectedEdge<E>> edges();

    @Override
    Set<? extends UndirectedNode<E>> neighbors();
  }

  interface UndirectedEdge<E> extends Graph.Edge<E> {

    @Override
    UnorderedPair<? extends UndirectedNode<E>> endpoints();
  }

  @Override
  Optional<? extends UndirectedNode<E>> nodeOf(Object item);

  Optional<? extends UndirectedEdge<E>> edgeOf(E first, E second);

  @Override
  Set<? extends UndirectedNode<E>> nodes();

  @Override
  Set<? extends UndirectedEdge<E>> edges();
}
