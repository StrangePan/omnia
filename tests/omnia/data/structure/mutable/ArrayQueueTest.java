package omnia.data.structure.mutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ArrayQueueTest {

  @Test
  public void init_isEmpty() {
    Queue<?> testSubject = new ArrayQueue<>();

    assertFalse(testSubject.isPopulated());
  }

  @Test
  public void init_countIsZero() {
    Queue<?> testSubject = new ArrayQueue<>();

    assertEquals(0, testSubject.count());
  }

  @Test(expected = IllegalArgumentException.class)
  public void init_withCapacityZero_didThrowException() {
    new ArrayQueue(/* capacity= */ 0);
  }

  @Test
  public void enqueue_one_isPopulated() {
    Queue<Object> testSubject = new ArrayQueue<>();

    testSubject.enqueue(new Object());

    assertTrue(testSubject.isPopulated());
  }

  @Test
  public void enqueue_one_countIsOne() {
    Queue<Object> testSubject = new ArrayQueue<>();

    testSubject.enqueue(new Object());

    assertEquals(1, testSubject.count());
  }

  @Test(expected = NullPointerException.class)
  public void enqueue_null_didThrowException() {
    Queue<Object> testSubject = new ArrayQueue<>();

    testSubject.enqueue(null);
  }

  @Test
  public void enqueue_thenDequeue_didReturnObject() {
    Queue<Object> testSubject = new ArrayQueue<>();
    Object item = new Object();
    testSubject.enqueue(item);

    Optional<Object> result = testSubject.dequeue();

    assertTrue(result.isPresent());
    assertSame(item, result.get());
  }

  @Test
  public void enqueue_thenDequeue_isEmpty() {
    Queue<Object> testSubject = new ArrayQueue<>();
    Object item = new Object();
    testSubject.enqueue(item);

    testSubject.dequeue();

    assertFalse(testSubject.isPopulated());
  }

  @Test
  public void enqueue_thenDequeue_countIsZero() {
    Queue<Object> testSubject = new ArrayQueue<>();
    Object item = new Object();
    testSubject.enqueue(item);

    testSubject.dequeue();

    assertEquals(0, testSubject.count());
  }

  @Test
  public void dequeue_whenEmpty_didReturnEmpty() {
    assertFalse(new ArrayQueue<>().dequeue().isPresent());
  }

  @Test
  public void enqueue_thenDequeueTwice_didReturnEmpty() {
    Queue<Object> testSubject = new ArrayQueue<>();
    testSubject.enqueue(new Object());

    testSubject.dequeue();

    assertFalse(testSubject.dequeue().isPresent());
  }

  @Test
  public void enqueue_fiveHundredTimes_isExpectedSize() {
    List<Integer> data = buildData(500);
    Queue<Integer> testSubject = new ArrayQueue<>();

    for (Integer datum : data) {
      testSubject.enqueue(datum);
    }

    assertEquals(500, testSubject.count());
  }

  @Test
  public void enqueue_fiveHundredTimes_thenDequeueFiveHundredTimes_didReturnExpectedItems() {
    List<Integer> data = buildData(500);
    Queue<Integer> testSubject = new ArrayQueue<>();

    for (Integer datum : data) {
      testSubject.enqueue(datum);
    }

    for (int i = 0; i < 500; i++) {
      assertEquals(data.itemAt(i), testSubject.dequeue().get());
    }
  }

  @Test
  public void enqueue_fiveHundredTimes_thenDequeueFiveHundredTimes_isEmpty() {
    List<Integer> data = buildData(500);
    Queue<Integer> testSubject = new ArrayQueue<>();

    for (Integer datum : data) {
      testSubject.enqueue(datum);
    }

    for (int i = 0; i < 500; i++) {
      testSubject.dequeue().get();
    }
    assertEquals(0, testSubject.count());
    assertFalse(testSubject.isPopulated());
  }

  @Test
  public void enqueue_thenDequeue_fiveHundredTimes_didReturnExpectedItems() {
    List<Integer> data = buildData(500);
    Queue<Integer> testSubject = new ArrayQueue<>();

    for (int i = 0; i < 500; i++) {
      Integer item = data.itemAt(i);
      testSubject.enqueue(item);
      assertEquals(item, testSubject.dequeue().get());
    }
  }

  @Test
  public void enqueue_thenDequeue_fiveHundredTimes_isEmpty() {
    List<Integer> data = buildData(500);
    Queue<Integer> testSubject = new ArrayQueue<>();

    for (int i = 0; i < 500; i++) {
      Integer item = data.itemAt(i);
      testSubject.enqueue(item);
      testSubject.dequeue();
    }

    assertEquals(0, testSubject.count());
    assertFalse(testSubject.isPopulated());
  }


  @Test
  public void peek_whenEmpty_isEmpty() {
    assertFalse(new ArrayQueue<>().peek().isPresent());
  }

  @Test
  public void peek_withOne_didReturnObject() {
    Queue<Integer> testSubject = new ArrayQueue<>();
    testSubject.enqueue(132);

    Optional<Integer> datum = testSubject.peek();

    assertTrue(datum.isPresent());
    assertEquals(Integer.valueOf(132), datum.get());
  }

  @Test
  public void peek_withOne_didNotDequeue() {
    Queue<Object> testSubject = new ArrayQueue<>();
    testSubject.enqueue(new Object());

    testSubject.peek();

    assertEquals(1, testSubject.count());
    assertTrue(testSubject.isPopulated());
  }

  @Test
  public void peekRepeatedly_withContent_didNotDequeue() {
    Queue<Object> testSubject = new ArrayQueue<>();
    for (int i = 0; i < 10; i++) {
      testSubject.enqueue(new Object());
    }

    for (int i = 0; i < 200; i++) {
      testSubject.peek();
    }

    assertEquals(10, testSubject.count());
    assertTrue(testSubject.isPopulated());
  }

  @Test
  public void peekRepeatedly_withContent_didReturnFirst() {
    Queue<Object> testSubject = new ArrayQueue<>();
    // First item (which we'll compare with)
    Object expected = new Object();
    testSubject.enqueue(expected);
    // Additional arbitrary objects
    for (int i = 0; i < 10; i++) {
      testSubject.enqueue(new Object());
    }

    for (int i = 0; i < 200; i++) {
      Optional<Object> result = testSubject.peek();
      assertTrue(result.isPresent());
      assertEquals(expected, result.get());
    }
  }

  private static List<Integer> buildData(int count) {
    if (count < 0) {
      throw new IllegalArgumentException(String.format("count must be positive (was %d)", count));
    }
    ImmutableList.Builder<Integer> data = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      data.add(i);
    }
    return data.build();
  }
}
