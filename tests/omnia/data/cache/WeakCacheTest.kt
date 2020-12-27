package omnia.data.cache

import com.google.common.truth.Truth
import com.google.common.truth.Truth8
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeakCacheTest {
  private val testSubject = WeakCache<String, String>()

  @Test
  fun contains_whenNotContained_returnsFalse() {
    Truth.assertThat(testSubject.contains("unknown")).isFalse()
  }

  @Test
  fun contains_whenContained_returnsTrue() {
    val value = "value"
    testSubject.getOrCache("key") { value }
    Truth.assertThat(testSubject.contains("key")).isTrue()
  }

  @Test
  fun getOrCache_whenNotContained_returnsSuppliedInstance() {
      val value = "value"
      Truth.assertThat(testSubject.getOrCache("key") { value }).isSameInstanceAs(value)
    }

  @Test
  fun getOrCache_whenContained_returnsCachedInstance() {
      val cachedValue = "cached"
      val suppliedValue = "supplied"
      testSubject.getOrCache("key") { cachedValue }
      Truth.assertThat(testSubject.getOrCache("key") { suppliedValue }).isSameInstanceAs(cachedValue)
    }

  @Test
  fun get_whenNotCached_isNotPresent() {
    Truth8.assertThat(testSubject["key"]).isEmpty()
  }

  @Test
  fun get_whenCached_isPresent() {
    val value = "value"
    testSubject.getOrCache("key") { value }
    Truth8.assertThat(testSubject["key"]).isPresent()
    Truth8.assertThat(testSubject["key"]).hasValue(value)
  }
}