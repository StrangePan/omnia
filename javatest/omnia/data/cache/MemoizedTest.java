package omnia.data.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoizedTest {

  @Test
  public void memoize_didReturnSimpleMemoizer() {
    assertTrue(Memoized.memoize(Object::new) instanceof SimpleMemoizer);
  }
}
