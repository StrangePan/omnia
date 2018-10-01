package omnia.data.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WeakCacheTest {

  private WeakCache<String, String> testSubject;

  @Before
  public void setUpTestSubject() {
    testSubject = new WeakCache<>();
  }

  @Test
  public void contains_whenNotContained_returnsFalse() {
    assertFalse(testSubject.contains("unknown"));
  }

  @Test
  public void contains_whenContained_returnsTrue() {
    String value = "value";

    testSubject.getOrCache("key", () -> value);

    assertTrue(testSubject.contains("key"));
  }

  @Test
  public void getOrCache_whenNotContained_returnsSuppliedInstance() {
    String value = "value";

    assertSame(value, testSubject.getOrCache("key", () -> value));
  }

  @Test
  public void getOrCache_whenContained_returnsCachedInstance() {
    @SuppressWarnings("RedundantStringConstructorCall") // we're testing reference behaviors
    String cachedValue = new String("cached");
    @SuppressWarnings("RedundantStringConstructorCall") // we're testing reference behaviors
    String suppliedValue = new String("supplied");

    testSubject.getOrCache("key", () -> cachedValue);

    assertSame(cachedValue, testSubject.getOrCache("key", () -> suppliedValue));
  }

  @Test
  public void get_whenNotCached_isNotPresent() {
    assertFalse(testSubject.get("key").isPresent());
  }

  @Test
  public void get_whenCached_isPresent() {
    String value = "value";

    testSubject.getOrCache("key", () -> value);

    assertTrue(value, testSubject.get("key").isPresent());
    assertSame(value, testSubject.get("key").get());
  }
}
