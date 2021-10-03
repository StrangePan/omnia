package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

import org.mockito.Mockito
import org.mockito.kotlin.mock

class SimpleDoubleCacherTest {

  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = 132.0
    val testSubject: CachedDouble = SimpleDoubleCacher { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = 132.0
    val testSubject: CachedDouble = SimpleDoubleCacher { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleDoubleCacher(supplier)
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun invalidate_didNotSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedDouble = SimpleDoubleCacher(supplier)
    testSubject.invalidate()
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedDouble = SimpleDoubleCacher(supplier)
    testSubject.value()
    Mockito.verify(supplier).invoke()
  }

  @Test
  fun value_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedDouble = SimpleDoubleCacher(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).invoke()
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedDouble = SimpleDoubleCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(2)).invoke()
  }

  companion object {

    private fun setUpMockSupplier(): () -> Double {
      val supplier = mock<() -> Double>()
      Mockito.`when`(supplier()).thenReturn(132.0)
      return supplier
    }
  }
}