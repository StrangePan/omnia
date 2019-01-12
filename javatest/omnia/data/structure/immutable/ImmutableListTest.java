package omnia.data.structure.immutable;

import omnia.data.structure.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ImmutableListTest {

  private static final String TEST_ITEM_1 = "first";
  private static final String TEST_ITEM_2 = "second";
  private static final String TEST_ITEM_3 = "third";

  @Test
  public void getAt_whenFirst_didReturnFirst() {
    List<String> underTest = createTestSubject();

    assertSame(TEST_ITEM_1, underTest.itemAt(0));
  }

  @Test
  public void getAt_whenMiddle_didReturnMiddle() {
    List<String> underTest = createTestSubject();

    assertSame(TEST_ITEM_2, underTest.itemAt(1));
  }

  @Test
  public void getAt_whenLast_didReturnLast() {
    List<String> underTest = createTestSubject();

    assertSame(TEST_ITEM_3, underTest.itemAt(2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getAt_whenEmpty_didThrowException() {
    ImmutableList.builder().build().itemAt(0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getAt_negative_didThrowException() {
    createTestSubject().itemAt(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getAt_tooHigh_didThrowException() {
    createTestSubject().itemAt(20);
  }

  @Test
  public void count_whenEmpty_didReturnCount() {
    assertEquals(0, ImmutableList.builder().build().count());
  }

  @Test
  public void count_didReturnCount() {
    assertEquals(3, createTestSubject().count());
  }

  @Test
  public void contains_whenFirst_didReturnTrue() {
    assertTrue(createTestSubject().contains(TEST_ITEM_1));
  }

  @Test
  public void contains_whenMiddle_didReturnTrue() {
    assertTrue(createTestSubject().contains(TEST_ITEM_2));
  }

  @Test
  public void contains_whenLast_didReturnTrue() {
    assertTrue(createTestSubject().contains(TEST_ITEM_3));
  }

  @Test
  public void contains_whenUnknown_didReturnFalse() {
    assertFalse(createTestSubject().contains("unknown"));
  }

  @Test
  public void iterate_didIterateOverAll() {
    Iterator<String> iterator = createTestSubject().iterator();

    assertSame(TEST_ITEM_1, iterator.next());
    assertSame(TEST_ITEM_2, iterator.next());
    assertSame(TEST_ITEM_3, iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void stream_didStreamAll() {
    Stream<String> stream = createTestSubject().stream();

    Object[] contents = stream.toArray();

    assertEquals(3, contents.length);
    assertSame(TEST_ITEM_1, contents[0]);
    assertSame(TEST_ITEM_2, contents[1]);
    assertSame(TEST_ITEM_3, contents[2]);
  }

  @Test
  public void equals_whenEmpty_didEqual() {
    assertEquals(ImmutableList.builder().build(), ImmutableList.builder().build());
  }

  @Test
  public void equals_whenEqual_areEqual() {
    assertEquals(createTestSubject(), createTestSubject());
  }

  @Test
  public void equals_whenUnequal_areNotEqual() {
    assertNotEquals(ImmutableList.<String>builder().add("hi there").build(), createTestSubject());
  }

  @Test
  public void hashCode_whenEqual_areEqual() {
    assertEquals(createTestSubject().hashCode(), createTestSubject().hashCode());
  }

  private static ImmutableList<String> createTestSubject() {
    return ImmutableList.<String>builder()
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_2)
        .add(TEST_ITEM_3)
        .build();
  }
}
