package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import java.util.function.LongSupplier
import kotlin.test.Test

import org.mockito.Mockito

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
    Mockito.verify(supplier).asLong
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleLongMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).asLong
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedLong = SimpleLongMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).asLong
  }

  companion object {

    private fun setUpSupplier(): LongSupplier {
      val supplier = Mockito.mock(LongSupplier::class.java)
      Mockito.`when`(supplier.asLong).thenReturn(132L)
      return supplier
    }
  }
}