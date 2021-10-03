package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

import org.mockito.Mockito
import org.mockito.kotlin.mock

class SimpleLongMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = 132L
    val testSubject: MemoizedLong = SimpleLongMemoizer { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = 132L
    val testSubject: MemoizedLong = SimpleLongMemoizer { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedLong = SimpleLongMemoizer(supplier)
    testSubject.value()
    Mockito.verify(supplier).invoke()
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleLongMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedLong = SimpleLongMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).invoke()
  }

  companion object {

    private fun setUpSupplier(): () -> Long {
      val supplier = mock<() -> Long>()
      Mockito.`when`(supplier()).thenReturn(132L)
      return supplier
    }
  }
}