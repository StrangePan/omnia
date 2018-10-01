package omnia.data.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.LongSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleLongMemoizerTest {

  @Test
  public void value_didReturnSuppliedResult() {
    long testValue = 132L;
    MemoizedLong testSubject = new SimpleLongMemoizer(() -> testValue);

    assertEquals(testValue, testSubject.value());
  }

  @Test
  public void value_twice_didReturnSuppliedResult() {
    long testValue = 132L;
    MemoizedLong testSubject = new SimpleLongMemoizer(() -> testValue);

    testSubject.value();

    assertEquals(testValue, testSubject.value());
  }

  @Test
  public void value_didInvokeSupplier() {
    LongSupplier supplier = setUpSupplier();
    MemoizedLong testSubject = new SimpleLongMemoizer(supplier);

    testSubject.value();

    verify(supplier).getAsLong();
  }

  @Test
  public void new_didNotInvokeSupplier() {
    LongSupplier supplier = setUpSupplier();
    new SimpleLongMemoizer(supplier);

    verify(supplier, never()).getAsLong();
  }

  @Test
  public void value_twice_onlyInvokedSupplierOnce() {
    LongSupplier supplier = setUpSupplier();
    MemoizedLong testSubject = new SimpleLongMemoizer(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).getAsLong();
  }

  @Test(expected = NullPointerException.class)
  public void new_withNullSupplier_didThrowException() {
    new SimpleLongMemoizer(null);
  }

  private static LongSupplier setUpSupplier() {
    LongSupplier supplier = mock(LongSupplier.class);
    when(supplier.getAsLong()).thenReturn(132L);
    return supplier;
  }
}
