package omnia.data.cache

import com.google.common.truth.Truth
import java.util.function.Supplier
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class SimpleMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = Any()
    val testSubject: Memoized<Any> = SimpleMemoizer(Supplier { testValue })
    Truth.assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = Any()
    val testSubject: Memoized<Any> = SimpleMemoizer(Supplier { testValue })
    testSubject.value()
    Truth.assertThat(testSubject.value()).isSameInstanceAs(testValue)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: Memoized<Any> = SimpleMemoizer(supplier)
    testSubject.value()
    Mockito.verify(supplier).get()
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).get()
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: Memoized<Any> = SimpleMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).get()
  }

  internal interface ObjectSupplier : Supplier<Any>
  companion object {

    private fun setUpSupplier(): Supplier<Any> {
      val supplier: Supplier<Any> = Mockito.mock(ObjectSupplier::class.java)
      Mockito.`when`(supplier.get()).thenReturn(Any())
      return supplier
    }
  }
}