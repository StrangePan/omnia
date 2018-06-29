package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class MemoizedIntTest {

  @Test
  public void memoize_didReturnSimpleIntMemoizer() {
    assertTrue(MemoizedInt.memoize(() -> 132) instanceof SimpleIntMemoizer);
  }
}
