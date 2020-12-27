package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
public class SimpleLongCacherTest {

  @Test
  public void value_didReturnSuppliedValue() {
    long testValue = 132L;
    CachedLong testSubject = new SimpleLongCacher(() -> testValue);

    assertThat(testSubject.value()).isEqualTo(testValue);
  }

  @Test
  public void value_twice_didReturnSuppliedValue() {
    long testValue = 132L;
    CachedLong testSubject = new SimpleLongCacher(() -> testValue);

    testSubject.value();

    assertThat(testSubject.value()).isEqualTo(testValue);
  }

  @Test
  public void new_didNotInvokeSupplier() {
    LongSupplier supplier = setUpMockSupplier();
    new SimpleLongCacher(supplier);

    verify(supplier, never()).getAsLong();
  }

  @Test
  public void invalidate_didNotSupplier() {
    LongSupplier supplier = setUpMockSupplier();
    CachedLong testSubject = new SimpleLongCacher(supplier);

    testSubject.invalidate();

    verify(supplier, never()).getAsLong();
  }

  @Test
  public void value_didInvokeSupplier() {
    LongSupplier supplier = setUpMockSupplier();
    CachedLong testSubject = new SimpleLongCacher(supplier);

    testSubject.value();

    verify(supplier).getAsLong();
  }

  @Test
  public void value_didInvokeSupplierOnce() {
    LongSupplier supplier = setUpMockSupplier();
    CachedLong testSubject = new SimpleLongCacher(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).getAsLong();
  }

  @Test
  public void value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    LongSupplier supplier = setUpMockSupplier();
    CachedLong testSubject = new SimpleLongCacher(supplier);

    testSubject.value();
    testSubject.invalidate();
    testSubject.value();

    verify(supplier, times(2)).getAsLong();
  }

  @Test
  public void new_withNullSupplier_didThrowException() {
    assertThrows(NullPointerException.class, () -> new SimpleLongCacher(null));
  }

  private static LongSupplier setUpMockSupplier() {
    LongSupplier supplier = mock(LongSupplier.class);
    when(supplier.getAsLong()).thenReturn(132L);
    return supplier;
  }
}
