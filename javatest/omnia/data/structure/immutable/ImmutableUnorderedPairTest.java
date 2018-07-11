package omnia.data.structure.immutable;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ImmutableUnorderedPairTest {

  private static final String TEST_FIRST = "first";
  private static final String TEST_SECOND = "second";

  @Test(expected = NullPointerException.class)
  public void create_whenFirstNull_didThrowException() {
    ImmutableUnorderedPair.of(null, TEST_SECOND);
  }

  @Test(expected = NullPointerException.class)
  public void create_whenSecondNull_didThrowException() {
    ImmutableUnorderedPair.of(TEST_FIRST, null);
  }

  @Test
  public void first_didReturnFirst() {
    ImmutableUnorderedPair<String> underTest = ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND);

    assertSame(TEST_FIRST, underTest.first());
  }

  @Test
  public void second_didReturnSecond() {
    ImmutableUnorderedPair<String> underTest = ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND);

    assertSame(TEST_SECOND, underTest.second());
  }

  @Test
  public void count_is2() {
    assertEquals(2, ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).count());
  }

  @Test
  public void containsFirst_isTrue() {
    assertTrue(ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).contains(TEST_FIRST));
  }

  @Test
  public void containsSecond_isTrue() {
    assertTrue(ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).contains(TEST_SECOND));
  }

  @Test
  public void containsUnknown_isFalse() {
    assertFalse(ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).contains("unknown"));
  }

  @Test
  public void iterate_didIterateOverAll() {
    Iterator<String> underTest = ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).iterator();

    assertSame(TEST_FIRST, underTest.next());
    assertSame(TEST_SECOND, underTest.next());
  }

  @Test
  public void stream_didStreamOverAll() {
    Stream<String> underTest = ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).stream();

    Object[] contents = underTest.toArray();

    assertEquals(2, contents.length);
    assertSame(TEST_FIRST, contents[0]);
    assertSame(TEST_SECOND, contents[1]);
  }

  @Test
  public void equals_whenSameOrder_isEqual() {
    assertEquals(
        ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND), ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND));
  }

  @Test
  public void equals_whenDifferentOrder_isEqual() {
    assertEquals(
        ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND), ImmutableUnorderedPair.of(TEST_SECOND, TEST_FIRST));
  }

  @Test
  public void equals_whenHalfOverlap_isNotEqual() {
    assertNotEquals(
        ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND), ImmutableUnorderedPair.of(TEST_FIRST, TEST_FIRST));
  }

  @Test
  public void equals_whenNoOverlap_isNotEqual() {
    assertNotEquals(
        ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND), ImmutableUnorderedPair.of("unknown1", "unknown2"));
  }

  @Test
  public void equals_whenNull_isFalse() {
    assertNotEquals(ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND), null);
  }

  @Test
  public void hashCode_whenDifferentOrder_isEqual() {
    assertEquals(
        ImmutableUnorderedPair.of(TEST_FIRST, TEST_SECOND).hashCode(),
        ImmutableUnorderedPair.of(TEST_SECOND, TEST_FIRST).hashCode());
  }
}
