package omnia.data.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.IntSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleIntCacherTest {

  @Test
  public void value_didReturnSuppliedValue() {
    int testValue = 132;
    CachedInt testSubject = new SimpleIntCacher(() -> testValue);

    assertEquals(testValue, testSubject.value());
  }

  @Test
  public void value_twice_didReturnSuppliedValue() {
    int testValue = 132;
    CachedInt testSubject = new SimpleIntCacher(() -> testValue);

    testSubject.value();

    assertEquals(testValue, testSubject.value());
  }

  @Test
  public void new_didNotInvokeSupplier() {
    IntSupplier supplier = setUpMockSupplier();
    new SimpleIntCacher(supplier);

    verify(supplier, never()).getAsInt();
  }

  @Test
  public void invalidate_didNotSupplier() {
    IntSupplier supplier = setUpMockSupplier();
    CachedInt testSubject = new SimpleIntCacher(supplier);

    testSubject.invalidate();

    verify(supplier, never()).getAsInt();
  }

  @Test
  public void value_didInvokeSupplier() {
    IntSupplier supplier = setUpMockSupplier();
    CachedInt testSubject = new SimpleIntCacher(supplier);

    testSubject.value();

    verify(supplier).getAsInt();
  }

  @Test
  public void value_didInvokeSupplierOnce() {
    IntSupplier supplier = setUpMockSupplier();
    CachedInt testSubject = new SimpleIntCacher(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).getAsInt();
  }

  @Test
  public void value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    IntSupplier supplier = setUpMockSupplier();
    CachedInt testSubject = new SimpleIntCacher(supplier);

    testSubject.value();
    testSubject.invalidate();
    testSubject.value();

    verify(supplier, times(2)).getAsInt();
  }

  @Test(expected = NullPointerException.class)
  public void new_withNullSupplier_didThrowException() {
    new SimpleIntCacher(null);
  }

  private static IntSupplier setUpMockSupplier() {
    IntSupplier supplier = mock(IntSupplier.class);
    when(supplier.getAsInt()).thenReturn(132);
    return supplier;
  }
}
