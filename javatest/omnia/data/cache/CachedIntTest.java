package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CachedIntTest {

  @Test
  public void cache_didReturnSimpleIntCache() {
    assertTrue(CachedInt.cache(() -> 132) instanceof SimpleIntCacher);
  }
}
