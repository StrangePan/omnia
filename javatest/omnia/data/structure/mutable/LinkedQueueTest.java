package omnia.data.structure.mutable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class LinkedQueueTest {

  @Test
  public void init_isEmpty() {
    Queue<?> testSubject = new LinkedQueue<>();

    assertFalse(testSubject.isPopulated());
  }

  @Test
  public void init_countIsZero() {
    Queue<?> testSubject = new LinkedQueue<>();

    assertEquals(0, testSubject.count());
  }

  @Test
  public void enqueue_one_isPopulated() {
    Queue<Object> testSubject = new LinkedQueue<>();

    testSubject.enqueue(new Object());

    assertTrue(testSubject.isPopulated());
  }

  @Test
  public void enqueue_one_countIsOne() {
    Queue<Object> testSubject = new LinkedQueue<>();

    testSubject.enqueue(new Object());

    assertEquals(1, testSubject.count());
  }

  @Test(expected = NullPointerException.class)
  public void enqueue_null_didThrowException() {
    Queue<Object> testSubject = new LinkedQueue<>();

    testSubject.enqueue(null);
  }

  @Test
  public void enqueue_thenDequeue_didReturnObject() {
    Queue<Object> testSubject = new LinkedQueue<>();
    Object item = new Object();
    testSubject.enqueue(item);

    Optional<Object> result = testSubject.nextAndRemove();

    assertTrue(result.isPresent());
    assertSame(item, result.get());
  }

  @Test
  public void enqueue_thenDequeue_isEmpty() {
    Queue<Object> testSubject = new LinkedQueue<>();
    Object item = new Object();
    testSubject.enqueue(item);

    testSubject.nextAndRemove();

    assertFalse(testSubject.isPopulated());
  }

  @Test
  public void enqueue_thenDequeue_countIsZero() {
    Queue<Object> testSubject = new LinkedQueue<>();
    Object item = new Object();
    testSubject.enqueue(item);

    testSubject.nextAndRemove();

    assertEquals(0, testSubject.count());
  }
}
