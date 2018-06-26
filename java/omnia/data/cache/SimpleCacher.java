package omnia.data.cache;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Cached} implementation that uses the given {@link Supplier} to provide the value to be
 * cached. This class is not thread-safe.
 *
 * The given {@link Supplier#get()} method is never invoked if the value is cached. {@link
 * Supplier#get()} is not allowed to return {@code null}. A null value will result in {@link
 * NullPointerException} being thrown.
 *
 * @param <T> the type of object to be cached
 */
final class SimpleCacher<T> implements Cached<T> {
  private final Supplier<T> supplier;
  private boolean isValid;
  private T value;

  SimpleCacher(Supplier<T> supplier) {
    this.supplier = requireNonNull(supplier);
  }

  @Override
  public T value() {
    if (!isValid) {
      value = requireNonNull(supplier.get());
      isValid = true;
    }
    return value;
  }

  @Override
  public void invalidate() {
    isValid = false;
    value = null;
  }
}
