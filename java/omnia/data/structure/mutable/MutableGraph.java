package omnia.data.structure.mutable;

import java.util.Optional;
import omnia.data.structure.Collection;
import omnia.data.structure.Graph;

public interface MutableGraph<E> extends Graph<E>, MutableCollection<E> {

  interface MutableNode<E> extends Node<E> {

    void replaceValue(E element);

    void remove();

    @Override
    MutableSet<? extends MutableEdge<E>> edges();

    @Override
    MutableSet<? extends MutableNode<E>> neighbors();
  }

  interface MutableEdge<E> extends Edge<E> {

    void remove();

    @Override
    Collection<? extends MutableNode<E>> endpoints();
  }

  void replace(E original, E replacement);

  void addEdge(E first, E second);

  boolean removeEdge(E first, E second);

  @Override
  Optional<? extends MutableNode<E>> nodeOf(E element);

  @Override
  MutableSet<E> contents();

  @Override
  MutableSet<? extends MutableNode<E>> nodes();

  @Override
  MutableSet<? extends MutableEdge<E>> edges();
}
