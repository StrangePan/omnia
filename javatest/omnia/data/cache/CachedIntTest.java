package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CachedIntTest {

  @Test
  public void cache_didReturnSimpleIntCache() {
    assertTrue(CachedInt.cache(() -> 132) instanceof SimpleIntCacher);
  }
}
