package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class MemoizedLongTest {

  @Test
  fun memoize_didReturnSimpleLongMemoizer() {
    assertThat(MemoizedLong.memoize { 132L }).isA(SimpleLongMemoizer::class)
  }
}