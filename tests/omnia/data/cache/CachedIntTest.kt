package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class CachedIntTest {

  @Test
  fun cache_didReturnSimpleIntCache() {
    assertThat(CachedInt.cache { 132 }).isInstanceOf(SimpleIntCacher::class.java)
  }
}