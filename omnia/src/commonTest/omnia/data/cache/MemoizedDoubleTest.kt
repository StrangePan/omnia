package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class MemoizedDoubleTest {

  @Test
  fun memoize_didReturnSimpleDoubleMemoizer() {
    assertThat(MemoizedDouble.memoize { 132.0 }).isA(SimpleDoubleMemoizer::class)
  }
}