package omnia.data.structure.mutable;

import omnia.data.structure.Set;

public interface MutableSet<E> extends Set<E>, MutableCollection<E> {

  static <E> MutableSet<E> masking(java.util.Set<E> javaSet) {
    return new MaskingSet<>(javaSet);
  }
}
