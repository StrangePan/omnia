package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class MemoizedTest {

  @Test
  public void memoize_didReturnSimpleMemoizer() {
    assertTrue(Memoized.memoize(Object::new) instanceof SimpleMemoizer);
  }
}
