package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import java.util.function.IntSupplier
import kotlin.test.Test

import org.mockito.Mockito

class SimpleIntCacherTest {

  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = 132
    val testSubject: CachedInt = SimpleIntCacher { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = 132
    val testSubject: CachedInt = SimpleIntCacher { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleIntCacher(supplier)
    Mockito.verify(supplier, Mockito.never()).asInt
  }

  @Test
  fun invalidate_didNotSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.invalidate()
    Mockito.verify(supplier, Mockito.never()).asInt
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.value()
    Mockito.verify(supplier).asInt
  }

  @Test
  fun value_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).asInt
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(2)).asInt
  }

  companion object {

    private fun setUpMockSupplier(): IntSupplier {
      val supplier = Mockito.mock(IntSupplier::class.java)
      Mockito.`when`(supplier.asInt).thenReturn(132)
      return supplier
    }
  }
}