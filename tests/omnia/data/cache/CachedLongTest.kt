package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CachedLongTest {
  @Test
  fun cache_didReturnSimpleLongCache() {
    Truth.assertThat(CachedLong.cache { 132L }).isInstanceOf(SimpleLongCacher::class.java)
  }
}