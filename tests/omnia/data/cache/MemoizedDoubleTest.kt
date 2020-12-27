package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MemoizedDoubleTest {
  @Test
  fun memoize_didReturnSimpleDoubleMemoizer() {
    Truth.assertThat(MemoizedDouble.memoize { 132.0 }).isInstanceOf(SimpleDoubleMemoizer::class.java)
  }
}