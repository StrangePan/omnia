package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.LongSupplier;

/**
 * A {@link CachedLong} implementation that uses the given {@link LongSupplier} to provide the
 * value to be cached. This class is not thread-safe.
 *
 * The given {@link LongSupplier#getAsLong()} method is never invoked if the value is cached.
 */
final class SimpleLongCacher implements CachedLong {
  private final LongSupplier supplier;
  private boolean isValid;
  private long value;

  SimpleLongCacher(LongSupplier supplier) {
    this.supplier = requireNonNull(supplier);
  }

  @Override
  public long value() {
    if (!isValid) {
      value = supplier.getAsLong();
      isValid = true;
    }
    return value;
  }

  @Override
  public void invalidate() {
    isValid = false;
  }
}
