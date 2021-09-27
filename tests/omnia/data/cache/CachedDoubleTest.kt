package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class CachedDoubleTest {

  @Test
  fun cache_didReturnSimpleDoubleCache() {
    assertThat(CachedDouble.cache { 132.0 }).isInstanceOf(SimpleDoubleCacher::class.java)
  }
}