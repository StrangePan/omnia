package omnia.data.cache

import com.google.common.truth.Truth.assertThat

import kotlin.test.Test

import org.mockito.Mockito
import org.mockito.kotlin.mock

class SimpleIntMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = 132
    val testSubject: MemoizedInt = SimpleIntMemoizer { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = 132
    val testSubject: MemoizedInt = SimpleIntMemoizer { testValue }
    testSubject.value()
    assertThat(testSubject.value().toLong()).isEqualTo(testValue.toLong())
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedInt = SimpleIntMemoizer(supplier)
    testSubject.value()
    Mockito.verify(supplier).invoke()
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleIntMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedInt = SimpleIntMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).invoke()
  }

  companion object {

    private fun setUpSupplier(): () -> Int {
      val supplier = mock<() -> Int>()
      Mockito.`when`(supplier()).thenReturn(132)
      return supplier
    }
  }
}