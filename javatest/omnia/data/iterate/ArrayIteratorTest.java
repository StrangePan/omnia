package omnia.data.iterate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ArrayIteratorTest {

  @Test
  public void hasNext_whenEmptyArray_isFalse() {
    assertFalse(new ArrayIterator<>(new Object[0]).hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void next_whenEmptyArray_didThrowNoSuchElementException() {
    new ArrayIterator<>(new Object[0]).next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void remove_whenEmptyArray_didThrowUnsupportedOperationException() {
    new ArrayIterator<>(new Object[0]).remove();
  }

  @Test
  public void hasNext_whenAtStart_isTrue() {
    assertTrue(new ArrayIterator<>(setUpTestArray()).hasNext());
  }

  @Test
  public void hasNext_whenInMiddle_isTrue() {
    Iterator<?> testSubject = new ArrayIterator<>(setUpTestArray());

    testSubject.next();

    assertTrue(testSubject.hasNext());
  }

  @Test
  public void hasNext_whenAtEnd_isFalse() {
    Object[] testData = setUpTestArray();
    Iterator<?> testSubject = new ArrayIterator<>(testData);

    for (int i = 0; i < testData.length; i++) {
      testSubject.next();
    }

    assertFalse(testSubject.hasNext());
  }

  @Test
  public void next_whenAtStart_didReturnFirstItem() {
    Object[] testData = setUpTestArray();
    Iterator<?> testSubject = new ArrayIterator<>(testData);

    assertEquals(testData[0], testSubject.next());
  }

  @Test
  public void next_twice_didReturnSecondItem() {
    Object[] testData = setUpTestArray();
    Iterator<?> testSubject = new ArrayIterator<>(testData);

    testSubject.next();

    assertEquals(testData[1], testSubject.next());
  }

  @Test
  public void next_thrice_didReturnThirdItem() {
    Object[] testData = setUpTestArray();
    Iterator<?> testSubject = new ArrayIterator<>(testData);

    testSubject.next();
    testSubject.next();

    assertEquals(testData[2], testSubject.next());
  }

  @Test(expected = NoSuchElementException.class)
  public void next_whenAtEnd_didThrowException() {
    Object[] testData = setUpTestArray();
    Iterator<?> testSubject = new ArrayIterator<>(testData);

    for (int i = 0; i < testData.length; i++) {
      testSubject.next();
    }

    testSubject.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void remove_whenInMiddle_didThrowUnsupportedOperationException() {
    Iterator<?> testSubject = new ArrayIterator<>(setUpTestArray());

    testSubject.next();

    testSubject.remove();
  }

  private static String[] setUpTestArray() {
    return new String[] {
        "This was a triumph",
        "I'm making a note here",
        "Huge success",
    };
  }
}
