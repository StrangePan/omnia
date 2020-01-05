package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
public class SimpleIntMemoizerTest {

  @Test
  public void value_didReturnSuppliedResult() {
    int testValue = 132;
    MemoizedInt testSubject = new SimpleIntMemoizer(() -> testValue);

    assertThat(testSubject.value()).isEqualTo(testValue);
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

  @Test
  public void new_withNullSupplier_didThrowException() {
    assertThrows(NullPointerException.class, () -> new SimpleIntMemoizer(null));
  }

  private static IntSupplier setUpSupplier() {
    IntSupplier supplier = mock(IntSupplier.class);
    when(supplier.getAsInt()).thenReturn(132);
    return supplier;
  }
}
