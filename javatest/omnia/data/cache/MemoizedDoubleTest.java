package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoizedDoubleTest {

  @Test
  public void memoize_didReturnSimpleDoubleMemoizer() {
    assertTrue(MemoizedDouble.memoize(() -> 132.0) instanceof SimpleDoubleMemoizer);
  }
}
