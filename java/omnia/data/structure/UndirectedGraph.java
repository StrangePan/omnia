package omnia.data.structure;

import java.util.Optional;

public interface UndirectedGraph<E> extends Graph<E> {

  interface Node<E> extends Graph.Node<E> {

    @Override
    Set<? extends Edge<E>> edges();

    @Override
    Set<? extends Node<E>> neighbors();
  }

  interface Edge<E> extends Graph.Edge<E> {

    @Override
    UnorderedPair<? extends Node<E>> endpoints();
  }

  @Override
  Optional<? extends Node<E>> nodeOf(E element);

  @Override
  Set<? extends Node<E>> nodes();

  @Override
  Set<? extends Edge<E>> edges();
}
