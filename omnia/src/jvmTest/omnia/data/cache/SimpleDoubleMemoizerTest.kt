package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

import org.mockito.Mockito
import org.mockito.kotlin.mock

class SimpleDoubleMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = 132.0
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = 132.0
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer(supplier)
    testSubject.value()
    Mockito.verify(supplier).invoke()
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleDoubleMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).invoke()
  }

  companion object {

    private fun setUpSupplier(): () -> Double {
      val supplier = mock<() -> Double>()
      Mockito.`when`(supplier()).thenReturn(132.0)
      return supplier
    }
  }
}