package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CachedDoubleTest {

  @Test
  public void cache_didReturnSimpleDoubleCache() {
    assertTrue(CachedDouble.cache(() -> 132.0) instanceof SimpleDoubleCacher);
  }
}
