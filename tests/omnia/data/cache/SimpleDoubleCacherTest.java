package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.DoubleSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleDoubleCacherTest {

  @Test
  public void value_didReturnSuppliedValue() {
    double testValue = 132.0;
    CachedDouble testSubject = new SimpleDoubleCacher(() -> testValue);

    assertThat(testSubject.value()).isEqualTo(testValue);
  }

  @Test
  public void value_twice_didReturnSuppliedValue() {
    double testValue = 132.0;
    CachedDouble testSubject = new SimpleDoubleCacher(() -> testValue);

    testSubject.value();

    assertThat(testSubject.value()).isEqualTo(testValue);
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

  @Test
  public void new_withNullSupplier_didThrowException() {
    assertThrows(NullPointerException.class, () -> new SimpleDoubleCacher(null));
  }

  private static DoubleSupplier setUpMockSupplier() {
    DoubleSupplier supplier = mock(DoubleSupplier.class);
    when(supplier.getAsDouble()).thenReturn(132.0);
    return supplier;
  }
}
