package omnia.data.structure.immutable;

import org.junit.Test;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImmutableSetTest {

  private static final String TEST_ITEM_1 = "first";
  private static final String TEST_ITEM_2 = "second";
  private static final String TEST_ITEM_3 = "third";

  @Test
  public void count_whenEmpty_didReturnCount() {
    assertEquals(0, ImmutableSet.builder().build().count());
  }

  @Test
  public void count_didReturnCount() {
    assertEquals(3, createTestSubject().count());
  }

  @Test
  public void count_withDuplicates_didReturnCount_withoutDuplicates() {
    assertEquals(2, createTestSubjectWithDuplicates().count());
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
    Iterator<String> iterator = createTestSubjectWithDuplicates().iterator();

    int count = 0;
    boolean found1 = false;
    boolean found2 = false;

    while (iterator.hasNext()) {
      String item = iterator.next();
      found1 |= item == TEST_ITEM_1;
      found2 |= item == TEST_ITEM_2;
      count++;
    }

    assertEquals(2, count);
    assertTrue(found1);
    assertTrue(found2);
  }

  @Test
  public void stream_didStreamAll() {
    Stream<String> stream = createTestSubjectWithDuplicates().stream();

    Object[] contents = stream.toArray();

    boolean found1 = false;
    boolean found2 = false;
    for (Object item : contents) {
      found1 |= item == TEST_ITEM_1;
      found2 |= item == TEST_ITEM_2;
    }

    assertEquals(2, contents.length);
    assertTrue(found1);
    assertTrue(found2);
  }

  private static ImmutableSet<String> createTestSubject() {
    return ImmutableSet.<String>builder()
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_2)
        .add(TEST_ITEM_3)
        .build();
  }

  private static ImmutableSet<String> createTestSubjectWithDuplicates() {
    return ImmutableSet.<String>builder()
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_2)
        .add(TEST_ITEM_2)
        .build();
  }
}
