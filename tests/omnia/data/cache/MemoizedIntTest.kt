package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class MemoizedIntTest {

  @Test
  fun memoize_didReturnSimpleIntMemoizer() {
    assertThat(MemoizedInt.memoize { 132 }).isInstanceOf(SimpleIntMemoizer::class.java)
  }
}