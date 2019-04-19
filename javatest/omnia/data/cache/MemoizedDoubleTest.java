package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoizedDoubleTest {

  @Test
  public void memoize_didReturnSimpleDoubleMemoizer() {
    assertThat(MemoizedDouble.memoize(() -> 132.0)).isInstanceOf(SimpleDoubleMemoizer.class);
  }
}
