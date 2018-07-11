package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoizedIntTest {

  @Test
  public void memoize_didReturnSimpleIntMemoizer() {
    assertTrue(MemoizedInt.memoize(() -> 132) instanceof SimpleIntMemoizer);
  }
}
