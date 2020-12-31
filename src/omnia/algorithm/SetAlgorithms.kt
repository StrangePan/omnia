package omnia.algorithm

import omnia.data.stream.Collectors
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet

object SetAlgorithms {

  /**
   * Computes the mathematical union of two sets. Returns a new set containing all of the items
   * present in set [a] and all of the items present in set [b].
   *
   *
   * This operation is commutative; the order of parameters will not affect the result.
   *
   * @return a new set representing the union of sets [a] and [b]
   */
  @JvmStatic
  fun <T> unionOf(a: Set<out T>, b: Set<out T>): Set<T> {
    return ImmutableSet.builder<T>().addAll(a).addAll(b).build()
  }

  /**
   * Computes the mathematical intersection of two sets. Returns a new set containing all of the
   * items present in set [a] and all of the items present in set [b] *except* for
   * items present in *both* sets.
   *
   *
   * This operation is commutative; the order of parameters will not affect the result.
   *
   * @return a new set representing the intersection of sets [a] and [b]
   */
  @JvmStatic
  fun <T> intersectionOf(a: Set<out T>, b: Set<out T>): Set<T> {
    val min = if (a.count() < b.count()) a else b
    val max = if (min !== a) a else b
    return min.stream().filter { element -> max.containsUnknownTyped(element) }
      .collect(Collectors.toSet())
  }

  /**
   * Returns a new set that contains all of the items present in set [a] except for the items
   * *also* present in set [b]. The result is guaranteed to never contain any item from
   * set [b].
   *
   *
   * This operation **is not commutative**; the order of parameters **will** affect the
   * result.
   *
   * @param a the set of items to possibly include in the final result
   * @param b the items not to be included in the final result
   * @param T the type of items in set [a]
   * @return a new set containing the items from set [a] except for the items contained in
   * set [b]
   */
  @JvmStatic
  fun <T> differenceBetween(a: Set<out T>, b: Set<*>): Set<T> {
    return a.stream().filter { item: T -> !b.containsUnknownTyped(item) }
      .collect(Collectors.toSet())
  }

  /**
   * Computes whether or not two sets are mathematically disjoint. Two sets are disjoint if they
   * share no common items. In other words, In other words, this function returns false if any
   * item in set [a] is also present in set [b]. Empty sets are considered disjoint,
   * despite also being considered equal.
   *
   *
   * This operation is commutative; the order of parameters will not affect the result.
   *
   * @return true if the sets are disjoint, false if any item is present in both.
   */
  @JvmStatic
  fun areDisjoint(a: Set<*>, b: Set<*>): Boolean {
    val min = if (a.count() < b.count()) a else b
    val max = if (min !== a) a else b
    return min.stream().anyMatch { element -> max.containsUnknownTyped(element) }
  }
}