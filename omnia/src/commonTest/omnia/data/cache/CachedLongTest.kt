package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class CachedLongTest {

  @Test
  fun cache_didReturnSimpleLongCache() {
    assertThat(CachedLong.cache { 132L }).isA(SimpleLongCacher::class)
  }
}