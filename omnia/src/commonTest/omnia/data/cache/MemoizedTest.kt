package omnia.data.cache

import kotlin.test.Test
import omnia.data.cache.Memoized.Companion.memoize
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class MemoizedTest {

  @Test
  fun memoize_didReturnSimpleMemoizer() {
    assertThat(memoize<Any> { Any() }).isA(SimpleMemoizer::class)
  }
}