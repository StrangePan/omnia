package omnia.data.structure.mutable;

import omnia.data.structure.Graph;

public interface MutableGraph<E> extends Graph<E> {

  void addNode(E item);

  void replaceNode(E original, E replacement);

  boolean removeNode(Object item);
}
