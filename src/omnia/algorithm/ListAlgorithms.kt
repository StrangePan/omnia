package omnia.algorithm

import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import java.util.Comparator
import java.util.Objects
import java.util.OptionalInt

object ListAlgorithms {
    fun <T> sublistOf(other: List<T>, startingIndex: Int, endingIndex: Int): ImmutableList<T> {
        Objects.checkFromToIndex(startingIndex, endingIndex, other.count())
        val sublist: ImmutableList.Builder<T> = ImmutableList.builder()
        for (i in startingIndex until endingIndex) {
            sublist.add(other.itemAt(i))
        }
        return sublist.build()
    }

    fun <T> reverse(other: List<T>): ImmutableList<T> {
        Objects.requireNonNull(other)
        val sublist: ImmutableList.Builder<T> = ImmutableList.builder()
        for (i in other.count() - 1 downTo 0) {
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
    fun <T> toArray(list: List<out T>, template: Array<T>): Array<T> {
        return list.stream().toArray { length: Int -> template.copyOf(length) }
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
    fun <T> binarySearch(
            haystack: List<out T>, needle: T, comparator: Comparator<in T>): OptionalInt {
        var min = 0
        var max = haystack.count() - 1
        while (min <= max) {
            val mid = (min + max) / 2
            val midValue = haystack.itemAt(mid)
            val comp = comparator.compare(midValue, needle)
            if (comp == 0) {
                return OptionalInt.of(mid)
            }
            if (comp < 0) {
                min = mid + 1
            }
            if (comp > 0) {
                max = mid - 1
            }
        }
        return OptionalInt.empty()
    }
}