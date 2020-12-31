package omnia.data.cache

import com.google.common.truth.Truth
import java.util.function.Supplier
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class SimpleCacherTest {

  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = Any()
    val testSubject: Cached<Any> = SimpleCacher { testValue }
    Truth.assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = Any()
    val testSubject: Cached<Any> = SimpleCacher { testValue }
    testSubject.value()
    Truth.assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleCacher(supplier)
    Mockito.verify(supplier, Mockito.never()).get()
  }

  @Test
  fun invalidate_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.invalidate()
    Mockito.verify(supplier, Mockito.never()).get()
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.value()
    Mockito.verify(supplier).get()
  }

  @Test
  fun value_twice_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).get()
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(2)).get()
  }

  internal interface ObjectSupplier : Supplier<Any>

  companion object {

    private fun setUpMockSupplier(): Supplier<Any> {
      val supplier: Supplier<Any> = Mockito.mock(ObjectSupplier::class.java)
      Mockito.`when`(supplier.get()).thenReturn(Any())
      return supplier
    }
  }
}