package omnia.data.structure.mutable;

import java.util.Optional;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.UnorderedPair;

public interface MutableUndirectedGraph<E> extends MutableGraph<E>, UndirectedGraph<E> {

  interface Node<E> extends MutableGraph.Node<E>, UndirectedGraph.Node<E> {

    @Override
    MutableSet<? extends Edge<E>> edges();

    @Override
    MutableSet<? extends Node<E>> neighbors();
  }

  interface Edge<E> extends MutableGraph.Edge<E>, UndirectedGraph.Edge<E> {

    @Override
    UnorderedPair<? extends Node<E>> endpoints();
  }

  @Override
  Optional<? extends Node<E>> nodeOf(E element);

  @Override
  MutableSet<? extends Node<E>> nodes();

  @Override
  MutableSet<? extends Edge<E>> edges();
}
