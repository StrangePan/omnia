package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CachedDoubleTest {

  @Test
  public void cache_didReturnSimpleDoubleCache() {
    assertThat(CachedDouble.cache(() -> 132.0)).isInstanceOf(SimpleDoubleCacher.class);
  }
}
