package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CachedDoubleTest {
  @Test
  fun cache_didReturnSimpleDoubleCache() {
    Truth.assertThat(CachedDouble.cache { 132.0 }).isInstanceOf(SimpleDoubleCacher::class.java)
  }
}