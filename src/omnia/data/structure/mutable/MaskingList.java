package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.stream.Stream;
import omnia.data.structure.Collection;

class MaskingList<E> implements MutableList<E> {

  private final java.util.List<E> javaList;

  MaskingList(java.util.List<E> javaList) {
    this.javaList = javaList;
  }

  @Override
  public void insertAt(int index, E element) {
    javaList.add(index, element);
  }

  @Override
  public E removeAt(int index) {
    return javaList.remove(index);
  }

  @Override
  public E replaceAt(int index, E element) {
    return javaList.set(index, element);
  }

  @Override
  public E itemAt(int index) {
    return javaList.get(index);
  }

  @Override
  public OptionalInt indexOf(Object element) {
    int index = javaList.indexOf(element);
    return index < 0 ? OptionalInt.empty() : OptionalInt.of(index);
  }

  @Override
  public void add(E element) {
    javaList.add(element);
  }

  @Override
  public void addAll(Collection<? extends E> elements) {
    javaList.addAll(elements.stream().collect(java.util.stream.Collectors.toList()));
  }

  @Override
  public boolean removeUnknownTyped(Object element) {
    return javaList.remove(element);
  }

  @Override
  public void clear() {
    javaList.clear();
  }

  @Override
  public Iterator<E> iterator() {
    return javaList.iterator();
  }

  @Override
  public boolean containsUnknownTyped(Object element) {
    return javaList.contains(element);
  }

  @Override
  public boolean isPopulated() {
    return !javaList.isEmpty();
  }

  @Override
  public int count() {
    return javaList.size();
  }

  @Override
  public Stream<E> stream() {
    return javaList.stream();
  }
}
