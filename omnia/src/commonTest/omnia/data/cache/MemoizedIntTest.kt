package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class MemoizedIntTest {

  @Test
  fun memoize_didReturnSimpleIntMemoizer() {
    assertThat(MemoizedInt.memoize { 132 }).isA(SimpleIntMemoizer::class)
  }
}