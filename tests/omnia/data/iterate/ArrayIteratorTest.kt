package omnia.data.iterate

import com.google.common.truth.Truth.assertThat
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.NoSuchElementException

@RunWith(JUnit4::class)
class ArrayIteratorTest {
  @Test
  fun hasNext_whenEmptyArray_isFalse() {
    Assert.assertFalse(ArrayIterator(arrayOfNulls<Any>(0)).hasNext())
  }

  @Test(expected = NoSuchElementException::class)
  fun next_whenEmptyArray_didThrowNoSuchElementException() {
    ArrayIterator(arrayOfNulls<Any>(0)).next()
  }

  @Test
  fun hasNext_whenAtStart_isTrue() {
    Assert.assertTrue(ArrayIterator(setUpTestArray()).hasNext())
  }

  @Test
  fun hasNext_whenInMiddle_isTrue() {
    val testSubject: Iterator<*> = ArrayIterator(setUpTestArray())
    testSubject.next()
    Assert.assertTrue(testSubject.hasNext())
  }

  @Test
  fun hasNext_whenAtEnd_isFalse() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    for (i in testData.indices) {
      testSubject.next()
    }
    Assert.assertFalse(testSubject.hasNext())
  }

  @Test
  fun next_whenAtStart_didReturnFirstItem() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    Assert.assertEquals(testData[0], testSubject.next())
  }

  @Test
  fun next_twice_didReturnSecondItem() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    testSubject.next()
    Assert.assertEquals(testData[1], testSubject.next())
  }

  @Test
  fun next_thrice_didReturnThirdItem() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    testSubject.next()
    testSubject.next()
    Assert.assertEquals(testData[2], testSubject.next())
  }

  @Test(expected = NoSuchElementException::class)
  fun next_whenAtEnd_didThrowException() {
    val testData: Array<out Any> = setUpTestArray()
    val testSubject: Iterator<*> = ArrayIterator(testData)
    for (i in testData.indices) {
      testSubject.next()
    }
    testSubject.next()
  }

  fun iterator_isNotMutable() {
    assertThat(ArrayIterator(setUpTestArray())).isNotInstanceOf(MutableIterator::class.java)
  }

  companion object {
    private fun setUpTestArray(): Array<String> {
      return arrayOf(
          "This was a triumph",
          "I'm making a note here",
          "Huge success")
    }
  }
}