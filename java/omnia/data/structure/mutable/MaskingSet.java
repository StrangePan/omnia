package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.stream.Stream;

class MaskingSet<E> implements MutableSet<E> {

  private final java.util.Set<E> javaSet;

  MaskingSet(java.util.Set<E> javaSet) {
    this.javaSet = javaSet;
  }

  @Override
  public void add(E element) {
    javaSet.add(element);
  }

  @Override
  public boolean remove(E element) {
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
