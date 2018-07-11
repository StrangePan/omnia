package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CachedLongTest {

  @Test
  public void cache_didReturnSimpleLongCache() {
    assertTrue(CachedLong.cache(() -> 132L) instanceof SimpleLongCacher);
  }
}
