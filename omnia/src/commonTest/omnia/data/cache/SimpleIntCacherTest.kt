package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo

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
    assertThat(supplier.invocations).isEqualTo(0)
  }

  @Test
  fun invalidate_didNotSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.invalidate()
    assertThat(supplier.invocations).isEqualTo(0)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.value()
    assertThat(supplier.invocations).isEqualTo(1)
  }

  @Test
  fun value_didInvokeSupplierOnce() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.value()
    testSubject.value()
    assertThat(supplier.invocations).isEqualTo(1)
  }

  @Test
  fun value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    val supplier = setUpMockSupplier()
    val testSubject: CachedInt = SimpleIntCacher(supplier)
    testSubject.value()
    testSubject.invalidate()
    testSubject.value()
    assertThat(supplier.invocations).isEqualTo(2)
  }

  companion object {
    private fun setUpMockSupplier() = MockSupplier(132)
  }
}