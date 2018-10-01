package omnia.data.structure;

import java.util.Optional;
import omnia.contract.Container;
import omnia.contract.Countable;

public interface Graph<E> extends Container<E>, Countable {

  interface Node<E> {

    E element();

    Set<? extends Edge<E>> edges();

    Set<? extends Node<E>> neighbors();
  }

  interface Edge<E> {

    Collection<? extends Node<E>> endpoints();
  }

  Optional<? extends Node<E>> nodeOf(E element);

  Set<E> contents();

  Set<? extends Node<E>> nodes();

  Set<? extends Edge<E>> edges();
}