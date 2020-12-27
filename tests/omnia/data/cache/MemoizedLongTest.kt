package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MemoizedLongTest {
  @Test
  fun memoize_didReturnSimpleLongMemoizer() {
    Truth.assertThat(MemoizedLong.memoize { 132L }).isInstanceOf(SimpleLongMemoizer::class.java)
  }
}