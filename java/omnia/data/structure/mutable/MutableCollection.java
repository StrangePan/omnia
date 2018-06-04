package omnia.data.structure.mutable;

import omnia.data.structure.Collection;

public interface MutableCollection<E> extends Collection<E> {

  void add(E element);

  boolean remove(E element);

  void clear();
}
