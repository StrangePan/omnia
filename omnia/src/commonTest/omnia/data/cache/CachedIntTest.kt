package omnia.data.cache

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isA

class CachedIntTest {

  @Test
  fun cache_didReturnSimpleIntCache() {
    assertThat(CachedInt.cache { 132 }).isA(SimpleIntCacher::class)
  }
}