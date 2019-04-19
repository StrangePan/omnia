package omnia.data.cache;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleCacherTest {

  @Test
  public void value_didReturnSuppliedValue() {
    Object testValue = new Object();
    Cached<Object> testSubject = new SimpleCacher<>(() -> testValue);

    assertThat(testSubject.value()).isSameInstanceAs(testValue);
  }

  @Test
  public void value_twice_didReturnSuppliedValue() {
    Object testValue = new Object();
    Cached<Object> testSubject = new SimpleCacher<>(() -> testValue);

    testSubject.value();

    assertThat(testSubject.value()).isSameInstanceAs(testValue);
  }

  @Test
  public void new_didNotInvokeSupplier() {
    Supplier<Object> supplier = setUpMockSupplier();
    new SimpleCacher<>(supplier);

    verify(supplier, never()).get();
  }

  @Test
  public void invalidate_didNotInvokeSupplier() {
    Supplier<Object> supplier = setUpMockSupplier();
    Cached<Object> testSubject = new SimpleCacher<>(supplier);

    testSubject.invalidate();

    verify(supplier, never()).get();
  }

  @Test
  public void value_didInvokeSupplier() {
    Supplier<Object> supplier = setUpMockSupplier();
    Cached<Object> testSubject = new SimpleCacher<>(supplier);

    testSubject.value();

    verify(supplier).get();
  }

  @Test
  public void value_twice_didInvokeSupplierOnce() {
    Supplier<Object> supplier = setUpMockSupplier();
    Cached<Object> testSubject = new SimpleCacher<>(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).get();
  }

  @Test
  public void value_thenInvalidate_thenValue_didInvokeSupplierTwice() {
    Supplier<Object> supplier = setUpMockSupplier();
    Cached<Object> testSubject = new SimpleCacher<>(supplier);

    testSubject.value();
    testSubject.invalidate();
    testSubject.value();

    verify(supplier, times(2)).get();
  }

  @Test
  public void new_withNullSupplier_didThrowException() {
    assertThrows(NullPointerException.class, () -> new SimpleCacher<>(null));
  }

  @Test
  public void value_whenSupplierReturnsNull_didThrowException() {
    Cached<Object> testSubject = new SimpleCacher<>(() -> null);

    assertThrows(NullPointerException.class, testSubject::value);
  }

  private static Supplier<Object> setUpMockSupplier() {
    Supplier<Object> supplier = mock(ObjectSupplier.class);
    when(supplier.get()).thenReturn(new Object());
    return supplier;
  }

  interface ObjectSupplier extends Supplier<Object> {}
}
