package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

import org.mockito.Mockito
import org.mockito.kotlin.mock

class SimpleLongCacherTest {

  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = 132L
    val testSubject: CachedLong = SimpleLongCacher { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = 132L
    val testSubject: CachedLong = SimpleLongCacher { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleLongCacher(supplier)
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun invalidate_didNotSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.invalidate()
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.value()
    Mockito.verify(supplier).invoke()
  }

  @Test
  fun value_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).invoke()
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(2)).invoke()
  }

  companion object {

    private fun setUpMockSupplier(): () -> Long {
      val supplier = mock<() -> Long>()
      Mockito.`when`(supplier()).thenReturn(132L)
      return supplier
    }
  }
}