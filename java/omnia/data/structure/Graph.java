package omnia.data.structure;

import omnia.contract.Container;
import omnia.contract.Countable;

import java.util.Optional;

public interface Graph<E> extends Container<E>, Countable {

  interface Node<E> {

    E element();

    Collection<? extends Edge<E>> edges();

    Set<? extends Node<E>> neighbors();
  }

  interface Edge<E> {

    Collection<? extends Node<E>> endpoints();
  }

  Optional<? extends Node<E>> nodeOf(E element);

  Set<? extends Node<E>> nodes();

  Collection<? extends Edge<E>> edges();
}