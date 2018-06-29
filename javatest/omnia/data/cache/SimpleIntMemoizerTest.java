package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.function.IntSupplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class SimpleIntMemoizerTest {

  @Test
  public void value_didReturnSuppliedResult() {
    int testValue = 132;
    MemoizedInt testSubject = new SimpleIntMemoizer(() -> testValue);

    assertEquals(testValue, testSubject.value());
  }

  @Test
  public void value_twice_didReturnSuppliedResult() {
    int testValue = 132;
    MemoizedInt testSubject = new SimpleIntMemoizer(() -> testValue);

    testSubject.value();

    assertEquals(testValue, testSubject.value());
  }

  @Test
  public void value_didInvokeSupplier() {
    IntSupplier supplier = setUpSupplier();
    MemoizedInt testSubject = new SimpleIntMemoizer(supplier);

    testSubject.value();

    verify(supplier).getAsInt();
  }

  @Test
  public void new_didNotInvokeSupplier() {
    IntSupplier supplier = setUpSupplier();
    new SimpleIntMemoizer(supplier);

    verify(supplier, never()).getAsInt();
  }

  @Test
  public void value_twice_onlyInvokedSupplierOnce() {
    IntSupplier supplier = setUpSupplier();
    MemoizedInt testSubject = new SimpleIntMemoizer(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).getAsInt();
  }

  @Test(expected = NullPointerException.class)
  public void new_withNullSupplier_didThrowException() {
    new SimpleIntMemoizer(null);
  }

  private static IntSupplier setUpSupplier() {
    IntSupplier supplier = mock(IntSupplier.class);
    when(supplier.getAsInt()).thenReturn(132);
    return supplier;
  }
}
