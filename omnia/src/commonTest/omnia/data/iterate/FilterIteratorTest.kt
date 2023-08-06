package omnia.data.iterate

import kotlin.test.Test
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.assertThatCode
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isTrue

class FilterIteratorTest {

  @Test
  fun iterate_whenPredicateMatchesAll_iteratesOverAll() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { true }
    assertThat(underTest.next()).isEqualTo(0)
    assertThat(underTest.next()).isEqualTo(1)
    assertThat(underTest.next()).isEqualTo(2)
    assertThat(underTest.next()).isEqualTo(3)
    assertThat(underTest.next()).isEqualTo(4)
    assertThat(underTest.next()).isEqualTo(5)
    assertThat(underTest.next()).isEqualTo(6)
    assertThat(underTest.next()).isEqualTo(7)
    assertThat(underTest.next()).isEqualTo(8)
    assertThat(underTest.next()).isEqualTo(9)
    assertThat(underTest.next()).isEqualTo(10)
    assertThatCode { underTest.next() }.failsWith(NoSuchElementException::class)
  }

  @Test
  fun iterate_whenPredicateMatchesEvens_iteratesOverEvens() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator(), isEven)
    assertThat(underTest.next()).isEqualTo(0)
    assertThat(underTest.next()).isEqualTo(2)
    assertThat(underTest.next()).isEqualTo(4)
    assertThat(underTest.next()).isEqualTo(6)
    assertThat(underTest.next()).isEqualTo(8)
    assertThat(underTest.next()).isEqualTo(10)
    assertThatCode { underTest.next() }.failsWith(NoSuchElementException::class)
  }

  @Test
  fun iterate_whenPredicateMatchesOdds_iteratesOverOdds() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator(), isOdd)
    assertThat(underTest.next()).isEqualTo(1)
    assertThat(underTest.next()).isEqualTo(3)
    assertThat(underTest.next()).isEqualTo(5)
    assertThat(underTest.next()).isEqualTo(7)
    assertThat(underTest.next()).isEqualTo(9)
    assertThatCode { underTest.next() }.failsWith(NoSuchElementException::class)
  }

  @Test
  fun iterate_whenPredicateMatchesNone_iteratesOverNone() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { false }
    assertThatCode { underTest.next() }.failsWith(NoSuchElementException::class)
  }

  @Test
  fun hasNext_whenPredicateMatchesAll_isTrue() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { true }
    assertThat(underTest.hasNext()).isTrue()
  }

  @Test
  fun hasNext_whenPredicateMatchesNone_isFalse() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { false }
    assertThat(underTest.hasNext()).isFalse()
  }

  @Test
  fun hasNext_whenPredicateMatchesFirstHalf_whenHalfwayIterated_isFalse() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { number -> number < 5 }
    underTest.next() // 0
    underTest.next() // 1
    underTest.next() // 2
    underTest.next() // 3
    underTest.next() // 4
    assertThat(underTest.hasNext()).isFalse()
  }

  @Test
  fun hasNext_whenPredicateMatchesFirstHalf_whenHasValuesLeft_isTrue() {
    val underTest: Iterator<Int> = FilterIterator(TEST_DATA.iterator()) { number -> number < 5 }
    underTest.next() // 0
    underTest.next() // 1
    underTest.next() // 2
    underTest.next() // 3
    assertThat(underTest.hasNext()).isTrue()
  }

  companion object {
    private val TEST_DATA: List<Int> = ImmutableList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    private val isEven: (Int) -> Boolean = { it % 2 == 0 }
    private val isOdd: (Int) -> Boolean = { it % 2 == 1 }
  }
}
