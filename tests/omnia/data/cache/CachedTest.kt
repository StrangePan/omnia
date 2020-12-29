package omnia.data.cache

import com.google.common.truth.Truth
import omnia.data.cache.Cached.Companion.cache
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CachedTest {

  @Test
  fun cache_didReturnSimpleCache() {
    Truth.assertThat(cache<Any> { Object() }).isInstanceOf(SimpleCacher::class.java)
  }
}