package omnia.data.cache

import com.google.common.truth.Truth
import omnia.data.cache.Memoized.Companion.memoize
import omnia.data.cache.SimpleMemoizer
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MemoizedTest {
  @Test
  fun memoize_didReturnSimpleMemoizer() {
    Truth.assertThat(memoize<Any> { Object() }).isInstanceOf(SimpleMemoizer::class.java)
  }
}