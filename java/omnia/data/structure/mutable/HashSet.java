package omnia.data.structure.mutable;

import omnia.data.structure.Collection;

public final class HashSet<E> extends MaskingSet<E> {

  public HashSet() {
    super(new java.util.HashSet<>());
  }

  private HashSet(Collection<? extends E> original) {
    super(original.stream().collect(java.util.stream.Collectors.toSet()));
  }

  public static <E> HashSet<E> copyOf(Collection<? extends E> original) {
    return new HashSet<>(original);
  }
}
