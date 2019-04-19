package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CachedIntTest {

  @Test
  public void cache_didReturnSimpleIntCache() {
    assertThat(CachedInt.cache(() -> 132)).isInstanceOf(SimpleIntCacher.class);
  }
}
