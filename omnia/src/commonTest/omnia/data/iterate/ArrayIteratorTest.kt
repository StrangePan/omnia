package omnia.data.iterate

import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isTrue

class ArrayIteratorTest {

  @Test
  fun hasNext_whenEmptyArray_isFalse() {
    assertThat(ArrayIterator(arrayOfNulls<Any>(0)).hasNext()).isFalse()
  }

  @Test
  fun next_whenEmptyArray_didThrowNoSuchElementException() {
    assertFailsWith(NoSuchElementException::class) { ArrayIterator(arrayOfNulls<Any>(0)).next() }
  }

  @Test
  fun hasNext_whenAtStart_isTrue() {
    assertThat(ArrayIterator(setUpTestArray()).hasNext()).isTrue()
  }

  @Test
  fun hasNext_whenInMiddle_isTrue() {
    val testSubject: Iterator<*> = ArrayIterator(setUpTestArray())
    testSubject.next()
    assertThat(testSubject.hasNext()).isTrue()
  }

  @Test
  fun hasNext_whenAtEnd_isFalse() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    for (i in testData.indices) {
      testSubject.next()
    }
    assertThat(testSubject.hasNext()).isFalse()
  }

  @Test
  fun next_whenAtStart_didReturnFirstItem() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    assertThat(testSubject.next()).isEqualTo(testData[0])
  }

  @Test
  fun next_twice_didReturnSecondItem() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    testSubject.next()
    assertThat(testSubject.next()).isEqualTo(testData[1])
  }

  @Test
  fun next_thrice_didReturnThirdItem() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    testSubject.next()
    testSubject.next()
    assertThat(testSubject.next()).isEqualTo(testData[2])
  }

  @Test
  fun next_whenAtEnd_didThrowException() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    for (i in testData.indices) {
      testSubject.next()
    }
    assertFailsWith(NoSuchElementException::class) { testSubject.next() }
  }

  @Test
  fun iterator_isNotMutable() {
    assertThat(ArrayIterator(setUpTestArray())).isA(MutableIterator::class)
  }

  companion object {
    private fun setUpTestArray(): Array<String> {
      return arrayOf(
        "This was a triumph",
        "I'm making a note here",
        "Huge success"
      )
    }
  }
}