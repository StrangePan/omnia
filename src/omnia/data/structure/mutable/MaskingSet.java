package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.stream.Stream;

class MaskingSet<E, J extends java.util.Set<E>> implements MutableSet<E> {

  private final J javaSet;

  MaskingSet(J javaSet) {
    this.javaSet = requireNonNull(javaSet);
  }

  protected final J javaSet() {
    return javaSet;
  }

  @Override
  public void add(E element) {
    javaSet.add(element);
  }

  @Override
  public boolean remove(Object element) {
    return javaSet.remove(element);
  }

  @Override
  public void clear() {
    javaSet.clear();
  }

  @Override
  public Iterator<E> iterator() {
    return javaSet.iterator();
  }

  @Override
  public boolean contains(Object element) {
    return javaSet.contains(element);
  }

  @Override
  public boolean isPopulated() {
    return !javaSet.isEmpty();
  }

  @Override
  public int count() {
    return javaSet.size();
  }

  @Override
  public Stream<E> stream() {
    return javaSet.stream();
  }
}
