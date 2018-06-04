package omnia.data.structure.immutable;

import omnia.data.structure.Collection;

import java.util.LinkedList;

abstract class AbstractBuilder<E, B extends AbstractBuilder<E, B, R>, R> {
  final java.util.List<E> elements = new LinkedList<>();

  public B add(E element) {
    elements.add(element);
    return getSelf();
  }

  public B addAll(Collection<E> elements) {
    for (E element : elements) {
      this.elements.add(element);
    }
    return getSelf();
  }

  public abstract R build();

  protected abstract B getSelf();
}
