package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CachedTest {

  @Test
  public void cache_didReturnSimpleCache() {
    assertTrue(Cached.cache(Object::new) instanceof SimpleCacher);
  }
}
