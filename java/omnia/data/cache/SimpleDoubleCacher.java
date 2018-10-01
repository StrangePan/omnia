package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleSupplier;

/**
 * A {@link CachedDouble} implementation that uses the given {@link DoubleSupplier} to provide the
 * value to be cached. This class is not thread-safe.
 *
 * The given {@link DoubleSupplier#getAsDouble()} method is never invoked if the value is cached.
 */
final class SimpleDoubleCacher implements CachedDouble {
  private final DoubleSupplier supplier;
  private boolean isValid;
  private double value;

  SimpleDoubleCacher(DoubleSupplier supplier) {
    this.supplier = requireNonNull(supplier);
  }

  @Override
  public double value() {
    if (!isValid) {
      value = supplier.getAsDouble();
      isValid = true;
    }
    return value;
  }

  @Override
  public void invalidate() {
    isValid = false;
  }
}
