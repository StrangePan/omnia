package omnia.data.cache

import kotlin.test.Test
import omnia.data.cache.Cached.Companion.cache
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class CachedTest {

  @Test
  fun cache_didReturnSimpleCache() {
    assertThat(cache { Any() }).isA(SimpleCacher::class)
  }
}