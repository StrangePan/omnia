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
public class SimpleDoubleMemoizerTest {

  @Test
  public void value_didReturnSuppliedResult() {
    double testValue = 132.0;
    MemoizedDouble testSubject = new SimpleDoubleMemoizer(() -> testValue);

    assertThat(testSubject.value()).isEqualTo(testValue);
  }

  @Test
  public void value_twice_didReturnSuppliedResult() {
    double testValue = 132.0;
    MemoizedDouble testSubject = new SimpleDoubleMemoizer(() -> testValue);

    testSubject.value();

    assertThat(testSubject.value()).isEqualTo(testValue);
  }

  @Test
  public void value_didInvokeSupplier() {
    DoubleSupplier supplier = setUpSupplier();
    MemoizedDouble testSubject = new SimpleDoubleMemoizer(supplier);

    testSubject.value();

    verify(supplier).getAsDouble();
  }

  @Test
  public void new_didNotInvokeSupplier() {
    DoubleSupplier supplier = setUpSupplier();
    new SimpleDoubleMemoizer(supplier);

    verify(supplier, never()).getAsDouble();
  }

  @Test
  public void value_twice_onlyInvokedSupplierOnce() {
    DoubleSupplier supplier = setUpSupplier();
    MemoizedDouble testSubject = new SimpleDoubleMemoizer(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).getAsDouble();
  }

  @Test
  public void new_withNullSupplier_didThrowException() {
    assertThrows(NullPointerException.class, () -> new SimpleDoubleMemoizer(null));
  }

  private static DoubleSupplier setUpSupplier() {
    DoubleSupplier supplier = mock(DoubleSupplier.class);
    when(supplier.getAsDouble()).thenReturn(132.0);
    return supplier;
  }
}
