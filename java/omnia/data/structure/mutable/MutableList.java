package omnia.data.structure.mutable;

import omnia.data.structure.List;

public interface MutableList<E> extends List<E>, MutableCollection<E> {

  void insertAt(int index, E element);

  void removeAt(int index);

  void replaceAt(int index, E element);
}
