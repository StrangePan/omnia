package omnia.data.structure.immutable;

import java.util.LinkedList;

abstract class AbstractBuilder<E, B extends AbstractBuilder<E, B, R>, R> {
  final java.util.List<E> elements = new LinkedList<>();

  public B add(E element) {
    elements.add(element);
    return getSelf();
  }

  public abstract R build();

  protected abstract B getSelf();
}
