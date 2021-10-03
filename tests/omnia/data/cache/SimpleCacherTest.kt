package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

import org.mockito.Mockito

class SimpleCacherTest {

  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = Any()
    val testSubject: Cached<Any> = SimpleCacher { testValue }
    assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = Any()
    val testSubject: Cached<Any> = SimpleCacher { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleCacher(supplier)
    Mockito.verify(supplier, Mockito.never())()
  }

  @Test
  fun invalidate_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.invalidate()
    Mockito.verify(supplier, Mockito.never())()
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.value()
    Mockito.verify(supplier)()
  }

  @Test
  fun value_twice_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1))()
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: Cached<Any> = SimpleCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(2))()
  }

  internal interface ObjectSupplier : () -> Any

  companion object {

    private fun setUpMockSupplier(): () -> Any {
      val supplier: () -> Any = Mockito.mock(ObjectSupplier::class.java)
      Mockito.`when`(supplier()).thenReturn(Any())
      return supplier
    }
  }
}