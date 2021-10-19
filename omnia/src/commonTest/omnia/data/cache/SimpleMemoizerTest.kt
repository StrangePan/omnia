package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isSameAs

class SimpleMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = Any()
    val testSubject: Memoized<Any> = SimpleMemoizer { testValue }
    assertThat(testSubject.value).isSameAs(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = Any()
    val testSubject: Memoized<Any> = SimpleMemoizer { testValue }
    testSubject.value
    assertThat(testSubject.value).isSameAs(testValue)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: Memoized<Any> = SimpleMemoizer(supplier)
    testSubject.value
    assertThat(supplier.invocations).isEqualTo(1)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleMemoizer(supplier)
    assertThat(supplier.invocations).isEqualTo(0)
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: Memoized<Any> = SimpleMemoizer(supplier)
    testSubject.value
    testSubject.value
    assertThat(supplier.invocations).isEqualTo(1)
  }

  companion object {
    private fun setUpSupplier() = MockSupplier(Any())
  }
}