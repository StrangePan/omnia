package omnia.algorithm;

import static omnia.data.stream.Collectors.toSet;

import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableSet;

public final class SetAlgorithms {
  private SetAlgorithms() {}

  public static <T> Set<T> unionOf(Set<? extends T> a, Set<? extends T> b) {
    return ImmutableSet.<T>builder().addAll(a).addAll(b).build();
  }

  public static <T> Set<T> intersectionOf(Set<? extends T> a, Set<? extends T> b) {
    Set<? extends T> min = (a.count() < b.count() ? a : b);
    Set<? extends T> max = (min != a ? a : b);
    return min.stream().filter(max::containsUnknownTyped).collect(toSet());
  }

  public static <T> Set<T> differenceBetween(Set<? extends T> a, Set<?> b) {
    return a.stream().filter(item -> !b.containsUnknownTyped(item)).collect(toSet());
  }

  public static boolean areDisjoint(Set<?> a, Set<?> b) {
    Set<?> min = (a.count() < b.count() ? a : b);
    Set<?> max = (min != a ? a : b);
    return min.stream().anyMatch(max::containsUnknownTyped);
  }
}
