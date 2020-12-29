package omnia.data.cache

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MemoizedIntTest {

  @Test
  fun memoize_didReturnSimpleIntMemoizer() {
    Truth.assertThat(MemoizedInt.memoize { 132 }).isInstanceOf(SimpleIntMemoizer::class.java)
  }
}