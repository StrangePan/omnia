package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class CachedLongTest {

  @Test
  fun cache_didReturnSimpleLongCache() {
    assertThat(CachedLong.cache { 132L }).isInstanceOf(SimpleLongCacher::class.java)
  }
}