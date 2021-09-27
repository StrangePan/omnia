package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class MemoizedLongTest {

  @Test
  fun memoize_didReturnSimpleLongMemoizer() {
    assertThat(MemoizedLong.memoize { 132L }).isInstanceOf(SimpleLongMemoizer::class.java)
  }
}