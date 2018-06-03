package omnia.data.structure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Iterator;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnorderedPairTest {

  private static final String TEST_FIRST = "first";
  private static final String TEST_SECOND = "second";

  @Test(expected = NullPointerException.class)
  public void create_whenFirstNull_didThrowException() {
    UnorderedPair.of(null, TEST_SECOND);
  }

  @Test(expected = NullPointerException.class)
  public void create_whenSecondNull_didThrowException() {
    UnorderedPair.of(TEST_FIRST, null);
  }

  @Test
  public void first_didReturnFirst() {
    UnorderedPair<String> underTest = UnorderedPair.of(TEST_FIRST, TEST_SECOND);

    assertSame(TEST_FIRST, underTest.first());
  }

  @Test
  public void second_didReturnSecond() {
    UnorderedPair<String> underTest = UnorderedPair.of(TEST_FIRST, TEST_SECOND);

    assertSame(TEST_SECOND, underTest.second());
  }

  @Test
  public void count_is2() {
    assertEquals(2, UnorderedPair.of(TEST_FIRST, TEST_SECOND).count());
  }

  @Test
  public void containsFirst_isTrue() {
    assertTrue(UnorderedPair.of(TEST_FIRST, TEST_SECOND).contains(TEST_FIRST));
  }

  @Test
  public void containsSecond_isTrue() {
    assertTrue(UnorderedPair.of(TEST_FIRST, TEST_SECOND).contains(TEST_SECOND));
  }

  @Test
  public void containsUnknown_isFalse() {
    assertFalse(UnorderedPair.of(TEST_FIRST, TEST_SECOND).contains("unknown"));
  }

  @Test
  public void iterate_didIterateOverAll() {
    Iterator<String> underTest = UnorderedPair.of(TEST_FIRST, TEST_SECOND).iterator();

    assertSame(TEST_FIRST, underTest.next());
    assertSame(TEST_SECOND, underTest.next());
  }

  @Test
  public void stream_didStreamOverAll() {
    Stream<String> underTest = UnorderedPair.of(TEST_FIRST, TEST_SECOND).stream();

    Object[] contents = underTest.toArray();

    assertEquals(2, contents.length);
    assertSame(TEST_FIRST, contents[0]);
    assertSame(TEST_SECOND, contents[1]);
  }

  @Test
  public void equals_whenSameOrder_isEqual() {
    assertEquals(
        UnorderedPair.of(TEST_FIRST, TEST_SECOND), UnorderedPair.of(TEST_FIRST, TEST_SECOND));
  }

  @Test
  public void equals_whenDifferentOrder_isEqual() {
    assertEquals(
        UnorderedPair.of(TEST_FIRST, TEST_SECOND), UnorderedPair.of(TEST_SECOND, TEST_FIRST));
  }

  @Test
  public void equals_whenHalfOverlap_isNotEqual() {
    assertNotEquals(
        UnorderedPair.of(TEST_FIRST, TEST_SECOND), UnorderedPair.of(TEST_FIRST, TEST_FIRST));
  }

  @Test
  public void equals_whenNoOverlap_isNotEqual() {
    assertNotEquals(
        UnorderedPair.of(TEST_FIRST, TEST_SECOND), UnorderedPair.of("unknown1", "unknown2"));
  }

  @Test
  public void equals_whenNull_isFalse() {
    assertNotEquals(UnorderedPair.of(TEST_FIRST, TEST_SECOND), null);
  }

  @Test
  public void hashCode_whenDifferentOrder_isEqual() {
    assertEquals(
        UnorderedPair.of(TEST_FIRST, TEST_SECOND).hashCode(),
        UnorderedPair.of(TEST_SECOND, TEST_FIRST).hashCode());
  }
}
