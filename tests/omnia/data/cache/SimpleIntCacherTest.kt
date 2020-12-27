package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.function.IntSupplier

@RunWith(JUnit4::class)
class SimpleIntCacherTest {
  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = 132
    val testSubject: CachedInt = SimpleIntCacher { testValue }
    Truth.assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = 132
    val testSubject: CachedInt = SimpleIntCacher { testValue }
    testSubject.value()
    Truth.assertThat(testSubject.value()).isEqualTo(testValue)
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