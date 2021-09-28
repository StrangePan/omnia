package omnia.data.cache

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class WeakCacheTest {

  private val testSubject = WeakCache<String, String>()

  @Test
  fun contains_whenNotContained_returnsFalse() {
    assertThat(testSubject.contains("unknown")).isFalse()
  }

  @Test
  fun contains_whenContained_returnsTrue() {
    val value = "value"
    testSubject.getOrCache("key") { value }
    assertThat(testSubject.contains("key")).isTrue()
  }

  @Test
  fun getOrCache_whenNotContained_returnsSuppliedInstance() {
    val value = "value"
    assertThat(testSubject.getOrCache("key") { value }).isSameInstanceAs(value)
  }

  @Test
  fun getOrCache_whenContained_returnsCachedInstance() {
    val cachedValue = "cached"
    val suppliedValue = "supplied"
    testSubject.getOrCache("key") { cachedValue }
    assertThat(testSubject.getOrCache("key") { suppliedValue }).isSameInstanceAs(cachedValue)
  }

  @Test
  fun get_whenNotCached_isNotPresent() {
    assertThat(testSubject["key"]).isNull()
  }

  @Test
  fun get_whenCached_isPresent() {
    val value = "value"
    testSubject.getOrCache("key") { value }
    assertThat(testSubject["key"]).isEqualTo(value)
  }
}