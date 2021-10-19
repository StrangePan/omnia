package omnia.algorithm

import omnia.data.structure.List
import omnia.data.structure.mutable.MutableList
import omnia.data.structure.immutable.ImmutableList

object ListAlgorithms {

  fun <T : Any> sublistOf(other: List<T>, startingIndex: Int, endingIndex: Int): ImmutableList<T> {
    require(startingIndex in 0..endingIndex)
    require(endingIndex in startingIndex..other.count)
    val sublist: ImmutableList.Builder<T> = ImmutableList.builder()
    for (i in startingIndex until endingIndex) {
      sublist.add(other.itemAt(i))
    }
    return sublist.build()
  }

  fun <T : Any> reverse(other: List<T>): ImmutableList<T> {
    val sublist: ImmutableList.Builder<T> = ImmutableList.builder()
    for (i in other.count - 1 downTo 0) {
      sublist.add(other.itemAt(i))
    }
    return sublist.build()
  }

  /**
   * Converts a list to an array containing the contents of the list. Requires a seed array from
   * which to generate a new result array.
   *
   * @param list the items to copy into an array
   * @param template The seed array used to create a new array instance. Not mutated in this method.
   * Can have any length, including 0.
   */
  fun <T : Any> toArray(list: List<out T>, template: Array<T?>): Array<T?> {
    val copy = template.copyOf(list.count)
    list.forEachIndexed { index, item -> copy[index] = item }
    return copy
  }

  /**
   * Searches for a specific item in a sorted list of items.
   *
   * @param haystack the sorted list to search through
   * @param needle the needle to search for
   * @param comparator the [Comparator] to use when searching through the haystack
   * @param T the type of the item to search for
   * @return the index of the needle within the haystack or the empty optional if the needle was not
   * found
   */
  fun <T : Any> binarySearch(haystack: List<out T>, needle: T, comparator: Comparator<in T>): Int? {
    var min = 0
    var max = haystack.count
    while (min < max) {
      val mid = (min + max - 1) / 2
      val midValue = haystack.itemAt(mid)
      val comp = comparator.compare(midValue, needle)
      when {
        comp == 0 -> return mid
        comp < 0 -> min = mid + 1
        comp > 0 -> max = mid
      }
    }
    return null
  }

  /**
   * Searches for a specific item in a sorted list of items or inserts it in the appropriate spot.
   *
   * @param haystack the sorted list to search through
   * @param needle the needle to search for
   * @param comparator the [Comparator] to use when searching through the haystack
   * @param T the type of the item to search for
   * @return the index of the needle within the haystack
   */
  fun <T : Any> binarySearchOrInsert(
      haystack: MutableList<T>, needle: T, comparator: Comparator<in T>): Int {
    var min = 0
    var max = haystack.count - 1
    while (min <= max) {
      val mid = (min + max) / 2
      val midValue = haystack.itemAt(mid)
      val comp = comparator.compare(midValue, needle)
      when {
        comp == 0 -> return mid
        comp < 0 -> min = mid + 1
        comp > 0 -> max = mid - 1
      }
    }
    haystack.insertAt(min, needle)
    return min
  }
}