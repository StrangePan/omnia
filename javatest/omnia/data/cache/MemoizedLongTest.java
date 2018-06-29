package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class MemoizedLongTest {

  @Test
  public void memoize_didReturnSimpleLongMemoizer() {
    assertTrue(MemoizedLong.memoize(() -> 132L) instanceof SimpleLongMemoizer);
  }
}
