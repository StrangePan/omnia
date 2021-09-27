package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import omnia.data.cache.Cached.Companion.cache

class CachedTest {

  @Test
  fun cache_didReturnSimpleCache() {
    assertThat(cache<Any> { Object() }).isInstanceOf(SimpleCacher::class.java)
  }
}