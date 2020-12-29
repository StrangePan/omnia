package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CachedIntTest {

  @Test
  fun cache_didReturnSimpleIntCache() {
    Truth.assertThat(CachedInt.cache { 132 }).isInstanceOf(SimpleIntCacher::class.java)
  }
}