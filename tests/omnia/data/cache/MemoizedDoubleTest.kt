package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class MemoizedDoubleTest {

  @Test
  fun memoize_didReturnSimpleDoubleMemoizer() {
    assertThat(MemoizedDouble.memoize { 132.0 })
      .isInstanceOf(SimpleDoubleMemoizer::class.java)
  }
}