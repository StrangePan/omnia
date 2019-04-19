package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CachedTest {

  @Test
  public void cache_didReturnSimpleCache() {
    assertThat(Cached.cache(Object::new)).isInstanceOf(SimpleCacher.class);
  }
}
