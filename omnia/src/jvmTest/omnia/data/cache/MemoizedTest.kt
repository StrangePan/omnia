package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import omnia.data.cache.Memoized.Companion.memoize
import omnia.data.cache.SimpleMemoizer

class MemoizedTest {

  @Test
  fun memoize_didReturnSimpleMemoizer() {
    assertThat(memoize<Any> { Object() }).isInstanceOf(SimpleMemoizer::class.java)
  }
}