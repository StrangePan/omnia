package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WeakCacheTest {
  private final WeakCache<String, String> testSubject = new WeakCache<>();

  @Test
  public void contains_whenNotContained_returnsFalse() {
    assertThat(testSubject.contains("unknown")).isFalse();
  }

  @Test
  public void contains_whenContained_returnsTrue() {
    String value = "value";

    testSubject.getOrCache("key", () -> value);

    assertThat(testSubject.contains("key")).isTrue();
  }

  @Test
  public void getOrCache_whenNotContained_returnsSuppliedInstance() {
    String value = "value";

    assertThat(testSubject.getOrCache("key", () -> value)).isSameInstanceAs(value);
  }

  @Test
  public void getOrCache_whenContained_returnsCachedInstance() {
    @SuppressWarnings("RedundantStringConstructorCall") // we're testing reference behaviors
    String cachedValue = new String("cached");
    @SuppressWarnings("RedundantStringConstructorCall") // we're testing reference behaviors
    String suppliedValue = new String("supplied");

    testSubject.getOrCache("key", () -> cachedValue);

    assertThat(testSubject.getOrCache("key", () -> suppliedValue)).isSameInstanceAs(cachedValue);
  }

  @Test
  public void get_whenNotCached_isNotPresent() {
    assertThat(testSubject.get("key")).isEmpty();
  }

  @Test
  public void get_whenCached_isPresent() {
    String value = "value";

    testSubject.getOrCache("key", () -> value);

    assertThat(testSubject.get("key")).isPresent();
    assertThat(testSubject.get("key")).hasValue(value);
  }
}
