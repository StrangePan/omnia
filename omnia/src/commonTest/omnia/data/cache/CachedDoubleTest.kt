package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class CachedDoubleTest {

  @Test
  fun cache_didReturnSimpleDoubleCache() {
    assertThat(CachedDouble.cache { 132.0 }).isA(SimpleDoubleCacher::class)
  }
}