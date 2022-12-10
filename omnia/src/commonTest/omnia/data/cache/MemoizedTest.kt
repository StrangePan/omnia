package omnia.data.cache

import kotlin.test.Test
import omnia.data.cache.Memoized.Companion.memoize
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA
import omnia.util.test.fluent.isEqualTo

class MemoizedTest {

  class ValueHolder {
    var value = 0
    fun incrementAndGet(): Int {
      return ++value
    }
  }

  @Test
  fun memoize_getValueTwice_onlyInvokesSupplierOnce() {
    val valueHolder = ValueHolder()
    val memoized = memoize(valueHolder::incrementAndGet)

    assertThat(memoized.value).isEqualTo(1)
    assertThat(memoized.value).isEqualTo(1)
    assertThat(memoized.value).isEqualTo(1)

    assertThat(valueHolder.value).isEqualTo(1)
  }
}