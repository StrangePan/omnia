package omnia.data.iterate

import com.google.common.truth.Truth
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.NoSuchElementException
import java.util.function.Predicate

@RunWith(JUnit4::class)
class FilterIteratorTest {
  @Test
  fun iterate_whenPredicateMatchesAll_iteratesOverAll() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { true }
    Truth.assertThat(underTest.next()).isEqualTo(0)
    Truth.assertThat(underTest.next()).isEqualTo(1)
    Truth.assertThat(underTest.next()).isEqualTo(2)
    Truth.assertThat(underTest.next()).isEqualTo(3)
    Truth.assertThat(underTest.next()).isEqualTo(4)
    Truth.assertThat(underTest.next()).isEqualTo(5)
    Truth.assertThat(underTest.next()).isEqualTo(6)
    Truth.assertThat(underTest.next()).isEqualTo(7)
    Truth.assertThat(underTest.next()).isEqualTo(8)
    Truth.assertThat(underTest.next()).isEqualTo(9)
    Truth.assertThat(underTest.next()).isEqualTo(10)
    Assertions.assertThrows(NoSuchElementException::class.java) { underTest.next() }
  }

  @Test
  fun iterate_whenPredicateMatchesEvens_iteratesOverEvens() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator(), isEven)
    Truth.assertThat(underTest.next()).isEqualTo(0)
    Truth.assertThat(underTest.next()).isEqualTo(2)
    Truth.assertThat(underTest.next()).isEqualTo(4)
    Truth.assertThat(underTest.next()).isEqualTo(6)
    Truth.assertThat(underTest.next()).isEqualTo(8)
    Truth.assertThat(underTest.next()).isEqualTo(10)
    Assertions.assertThrows(NoSuchElementException::class.java) { underTest.next() }
  }

  @Test
  fun iterate_whenPredicateMatchesOdds_iteratesOverOdds() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator(), isOdd)
    Truth.assertThat(underTest.next()).isEqualTo(1)
    Truth.assertThat(underTest.next()).isEqualTo(3)
    Truth.assertThat(underTest.next()).isEqualTo(5)
    Truth.assertThat(underTest.next()).isEqualTo(7)
    Truth.assertThat(underTest.next()).isEqualTo(9)
    Assertions.assertThrows(NoSuchElementException::class.java) { underTest.next() }
  }

  @Test
  fun iterate_whenPredicateMatchesNone_iteratesOverNone() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { false }
    Assertions.assertThrows(NoSuchElementException::class.java) { underTest.next() }
  }

  @Test
  fun hasNext_whenPredicateMatchesAll_isTrue() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { true }
    Truth.assertThat(underTest.hasNext()).isTrue()
  }

  @Test
  fun hasNext_whenPredicateMatchesNone_isFalse() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { false }
    Truth.assertThat(underTest.hasNext()).isFalse()
  }

  @Test
  fun hasNext_whenPredicateMatchesFirstHalf_whenHalfwayIterated_isFalse() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { number -> number < 5 }
    underTest.next() // 0
    underTest.next() // 1
    underTest.next() // 2
    underTest.next() // 3
    underTest.next() // 4
    Truth.assertThat(underTest.hasNext()).isFalse()
  }

  @Test
  fun hasNext_whenPredicateMatchesFirstHalf_whenHasValuesLeft_isTrue() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { number -> number < 5 }
    underTest.next() // 0
    underTest.next() // 1
    underTest.next() // 2
    underTest.next() // 3
    Truth.assertThat(underTest.hasNext()).isTrue()
  }

  companion object {
    private val TEST_DATA: List<Int> = ImmutableList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    private val isEven: Predicate<Int>
      get() = Predicate { number -> number % 2 == 0 }
    private val isOdd: Predicate<Int>
      get() = Predicate { number -> number % 2 == 1 }
  }
}