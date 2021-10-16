package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo

class SimpleDoubleMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = 132.0
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer { testValue }
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = 132.0
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer { testValue }
    testSubject.value()
    assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer(supplier)
    testSubject.value()
    assertThat(supplier.invocations).isEqualTo(1)
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleDoubleMemoizer(supplier)
    assertThat(supplier.invocations).isEqualTo(0)
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedDouble = SimpleDoubleMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    assertThat(supplier.invocations).isEqualTo(1)
  }

  companion object {
    private fun setUpSupplier() = MockSupplier(132.0)
  }
}