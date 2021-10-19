package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isSameAs

class SimpleCacherTest {

  @Test
  fun value_didReturnSuppliedValue() {
    val testValue = Any()
    val testSubject: Cached<Any> = SimpleCacher { testValue }
    assertThat(testSubject.value).isSameAs(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedValue() {
    val testValue = Any()
    val testSubject: Cached<Any> = SimpleCacher { testValue }
    testSubject.value
    assertThat(testSubject.value).isSameAs(testValue)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    SimpleCacher(supplier)
    assertThat(supplier.invocations).isEqualTo(0)
  }

  @Test
  fun invalidate_didNotInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject = SimpleCacher(supplier)
    testSubject.invalidate()
    assertThat(supplier.invocations).isEqualTo(0)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject = SimpleCacher(supplier)
    testSubject.value
    assertThat(supplier.invocations).isEqualTo(1)
  }

  @Test
  fun value_twice_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject = SimpleCacher(supplier)
    testSubject.value
    testSubject.value
    assertThat(supplier.invocations).isEqualTo(1)
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject = SimpleCacher(supplier)
    testSubject.value
    testSubject.invalidate()
    testSubject.value
    assertThat(supplier.invocations).isEqualTo(2)
  }

  companion object {
    private fun setUpMockSupplier() = MockSupplier(Any())
  }
}