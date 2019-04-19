package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoizedIntTest {

  @Test
  public void memoize_didReturnSimpleIntMemoizer() {
    assertThat(MemoizedInt.memoize(() -> 132)).isInstanceOf(SimpleIntMemoizer.class);
  }
}
