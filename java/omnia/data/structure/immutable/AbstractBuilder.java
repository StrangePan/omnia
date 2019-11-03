package omnia.data.structure.immutable;

import omnia.data.structure.mutable.LinkedList;
import omnia.data.structure.mutable.MutableList;

abstract class AbstractBuilder<E, B extends AbstractBuilder<E, B, R>, R> {
  final MutableList<E> elements = new LinkedList<>();

  public B add(E element) {
    elements.add(element);
    return getSelf();
  }

  public B addAll(Iterable<? extends E> elements) {
    for (E element : elements) {
      this.elements.add(element);
    }
    return getSelf();
  }

  @SafeVarargs
  public final B addAll(E... elements) {
    for (E element : elements) {
      this.elements.add(element);
    }
    return getSelf();
  }

  public abstract R build();

  protected abstract B getSelf();
}
