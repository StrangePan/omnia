package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoizedLongTest {

  @Test
  public void memoize_didReturnSimpleLongMemoizer() {
    assertTrue(MemoizedLong.memoize(() -> 132L) instanceof SimpleLongMemoizer);
  }
}
