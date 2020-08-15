package omnia.algorithm;

import static omnia.data.stream.Collectors.toSet;

import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableSet;

public final class SetAlgorithms {
  private SetAlgorithms() {}

  /**
   * Computes the mathematical union of two sets. Returns a new set containing all of the items
   * present in set {@code a} and all of the items present in set {@code b}.
   *
   * <p>This operation is communative; the order of parameters will not affect the result.
   *
   * @return a new set representing the union of sets {@code a} and {@code b}
   */
  public static <T> Set<T> unionOf(Set<? extends T> a, Set<? extends T> b) {
    return ImmutableSet.<T>builder().addAll(a).addAll(b).build();
  }

  /**
   * Computes the mathematical intersection of two sets. Returns a new set containing all of the
   * items present in set {@code a} and all of the items present in set {@code b} <i>except</i> for
   * items present in <i>both</i> sets.
   *
   * <p>This operation is communative; the order of parameters will not affect the result.
   *
   * @return a new set representing the intersection of sets {@code a} and {@code b}
   */
  public static <T> Set<T> intersectionOf(Set<? extends T> a, Set<? extends T> b) {
    Set<? extends T> min = (a.count() < b.count() ? a : b);
    Set<? extends T> max = (min != a ? a : b);
    return min.stream().filter(max::containsUnknownTyped).collect(toSet());
  }

  /**
   * Returns a new set that contains all of the items present in set {@code a} except for the items
   * <i>also</i> present in set {@code b}. The result is guaranteed to never contain any item from
   * set {@code b}.
   *
   * <p>This operation <b>is not communative</b>; the order of parameters <b>will</b> affect the
   * result.</p>
   *
   * @param a the set of items to possibly include in the final result
   * @param b the items not to be included in the final result
   * @param <T> the type of items in set {@code a}
   * @return a new set containing the items from set {@code a} except for the items contained in
   *     set {@code b}
   */
  public static <T> Set<T> differenceBetween(Set<? extends T> a, Set<?> b) {
    return a.stream().filter(item -> !b.containsUnknownTyped(item)).collect(toSet());
  }

  /**
   * Computes whether or not two sets are mathematically disjoint. Two sets are disjoint if they
   * share no common items. In other words, In other words, this function returns false if any
   * item in set {@code a} is also present in set {@code b}. Empty sets are considered disjoint,
   * despite also being considered equal.
   *
   * <p>This operation is communative; the order of parameters will not affect the result.
   *
   * @return true if the sets are disjoint, false if any item is present in both.
   */
  public static boolean areDisjoint(Set<?> a, Set<?> b) {
    Set<?> min = (a.count() < b.count() ? a : b);
    Set<?> max = (min != a ? a : b);
    return min.stream().anyMatch(max::containsUnknownTyped);
  }
}
