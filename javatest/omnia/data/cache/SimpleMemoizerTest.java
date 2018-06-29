package omnia.data.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.function.Supplier;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class SimpleMemoizerTest {

  @Test
  public void value_didReturnSuppliedResult() {
    Object testValue = new Object();
    Memoized<Object> testSubject = new SimpleMemoizer<>(() -> testValue);

    assertSame(testValue, testSubject.value());
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
  public void value_twice_didReturnSuppliedResult() {
    Object testValue = new Object();
    Memoized<Object> testSubject = new SimpleMemoizer<>(() -> testValue);

    testSubject.value();

    assertSame(testValue, testSubject.value());
  }

  @Test(expected = NullPointerException.class)
  public void new_withNullSupplier_didThrowException() {
    new SimpleMemoizer<>(null);
  }

  @Test(expected = NullPointerException.class)
  public void value_whenSupplierReturnsNull_didThrowException() {
    Memoized<Object> testSubject = new SimpleMemoizer<>(() -> null);

    testSubject.value();
  }

  private static Supplier<Object> setUpSupplier() {
    Supplier<Object> supplier = mock(ObjectSupplier.class);
    when(supplier.get()).thenReturn(new Object());
    return supplier;
  }

  interface ObjectSupplier extends Supplier<Object> {}
}
