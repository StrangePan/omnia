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
public class SimpleMemoizerTest {

  @Test
  public void value_didReturnSuppliedResult() {
    Object testValue = new Object();
    Memoized<Object> testSubject = new SimpleMemoizer<>(() -> testValue);

    assertThat(testSubject.value()).isSameInstanceAs(testValue);
  }

  @Test
  public void value_twice_didReturnSuppliedResult() {
    Object testValue = new Object();
    Memoized<Object> testSubject = new SimpleMemoizer<>(() -> testValue);

    testSubject.value();

    assertThat(testSubject.value()).isSameInstanceAs(testValue);
  }

  @Test
  public void value_didInvokeSupplier() {
    Supplier<Object> supplier = setUpSupplier();
    Memoized<Object> testSubject = new SimpleMemoizer<>(supplier);

    testSubject.value();

    verify(supplier).get();
  }

  @Test
  public void new_didNotInvokeSupplier() {
    Supplier<Object> supplier = setUpSupplier();
    new SimpleMemoizer<>(supplier);

    verify(supplier, never()).get();
  }

  @Test
  public void value_twice_onlyInvokedSupplierOnce() {
    Supplier<Object> supplier = setUpSupplier();
    Memoized<Object> testSubject = new SimpleMemoizer<>(supplier);

    testSubject.value();
    testSubject.value();

    verify(supplier, times(1)).get();
  }

  @Test
  public void new_withNullSupplier_didThrowException() {
    assertThrows(NullPointerException.class, () -> new SimpleMemoizer<>(null));
  }

  @Test
  public void value_whenSupplierReturnsNull_didThrowException() {
    Memoized<Object> testSubject = new SimpleMemoizer<>(() -> null);

    assertThrows(NullPointerException.class, testSubject::value);
  }

  private static Supplier<Object> setUpSupplier() {
    Supplier<Object> supplier = mock(ObjectSupplier.class);
    when(supplier.get()).thenReturn(new Object());
    return supplier;
  }

  interface ObjectSupplier extends Supplier<Object> {}
}
