package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.function.LongSupplier

@RunWith(JUnit4::class)
class SimpleLongCacherTest {
  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = 132L
    val testSubject: CachedLong = SimpleLongCacher { testValue }
    Truth.assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = 132L
    val testSubject: CachedLong = SimpleLongCacher { testValue }
    testSubject.value()
    Truth.assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleLongCacher(supplier)
    Mockito.verify(supplier, Mockito.never()).asLong
  }

  @Test
  fun invalidate_didNotSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.invalidate()
    Mockito.verify(supplier, Mockito.never()).asLong
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.value()
    Mockito.verify(supplier).asLong
  }

  @Test
  fun value_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).asLong
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedLong = SimpleLongCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(2)).asLong
  }

  companion object {
    private fun setUpMockSupplier(): LongSupplier {
      val supplier = Mockito.mock(LongSupplier::class.java)
      Mockito.`when`(supplier.asLong).thenReturn(132L)
      return supplier
    }
  }
}