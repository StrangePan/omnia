package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.function.DoubleSupplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class SimpleDoubleCacherTest {

  @Test
  public void value_didReturnSuppliedValue() {
    double testValue = 132.0;
    CachedDouble testSubject = new SimpleDoubleCacher(() -> testValue);

    assertEquals(testValue, testSubject.value(),  /* delta= */ 0);
  }

  @Test
  public void value_twice_didReturnSuppliedValue() {
    double testValue = 132.0;
    CachedDouble testSubject = new SimpleDoubleCacher(() -> testValue);

    testSubject.value();

    assertEquals(testValue, testSubject.value(), /* delta= */ 0);
  }

  @Test
  public void new_didNotInvokeSupplier() {
    DoubleSupplier supplier = setUpMockSupplier();
    new SimpleDoubleCacher(supplier);

    verify(supplier, never()).getAsDouble();
  }

  @Test
  public void invalidate_didNotSupplier() {
    DoubleSupplier supplier = setUpMockSupplier();
    CachedDouble testSubject = new SimpleDoubleCacher(supplier);

    testSubject.invalidate();

    verify(supplier, never()).getAsDouble();
  }

  @Test
  public void value_didInvokeSupplier() {
    DoubleSupplier supplier = setUpMockSupplier();
    CachedDouble testSubject = new SimpleDoubleCacher(supplier);

    testSubject.value();

    verify(supplier).getAsDouble();
  }

  @Test
  public void value_didInvokeSupplierOnce() {
    DoubleSupplier supplier = setUpMockSupplier();
    CachedDouble testSubject = new SimpleDoubleCacher(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).getAsDouble();
  }

  @Test
  public void value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    DoubleSupplier supplier = setUpMockSupplier();
    CachedDouble testSubject = new SimpleDoubleCacher(supplier);

    testSubject.value();
    testSubject.invalidate();
    testSubject.value();

    verify(supplier, times(2)).getAsDouble();
  }

  @Test(expected = NullPointerException.class)
  public void new_withNullSupplier_didThrowException() {
    new SimpleDoubleCacher(null);
  }

  private static DoubleSupplier setUpMockSupplier() {
    DoubleSupplier supplier = mock(DoubleSupplier.class);
    when(supplier.getAsDouble()).thenReturn(132.0);
    return supplier;
  }
}
