package omnia.data.structure.mutable;

import java.util.Optional;
import omnia.data.structure.Collection;
import omnia.data.structure.Graph;

public interface MutableGraph<E> extends Graph<E>, MutableCollection<E> {

  interface Node<E> extends Graph.Node<E> {

    void replaceValue(E element);

    void remove();

    @Override
    MutableSet<? extends Edge<E>> edges();

    @Override
    MutableSet<? extends Node<E>> neighbors();
  }

  interface Edge<E> extends Graph.Edge<E> {

    void remove();

    @Override
    Collection<? extends Node<E>> endpoints();
  }

  void replace(E original, E replacement);

  void addEdge(E first, E second);

  boolean removeEdge(E first, E second);

  @Override
  Optional<? extends Node<E>> nodeOf(E element);

  @Override
  MutableSet<E> contents();

  @Override
  MutableSet<? extends Node<E>> nodes();

  @Override
  MutableSet<? extends Edge<E>> edges();
}
