package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

import org.mockito.Mockito
import org.mockito.kotlin.mock

class SimpleMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = Any()
    val testSubject: Memoized<Any> = SimpleMemoizer { testValue }
    assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = Any()
    val testSubject: Memoized<Any> = SimpleMemoizer { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: Memoized<Any> = SimpleMemoizer(supplier)
    testSubject.value()
    Mockito.verify(supplier).invoke()
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).invoke()
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: Memoized<Any> = SimpleMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).invoke()
  }

  companion object {

    private fun setUpSupplier(): () -> Any {
      val supplier: () -> Any = mock()
      Mockito.`when`(supplier()).thenReturn(Any())
      return supplier
    }
  }
}