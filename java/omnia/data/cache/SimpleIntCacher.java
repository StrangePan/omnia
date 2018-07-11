package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.IntSupplier;

/**
 * A {@link CachedInt} implementation that uses the given {@link IntSupplier} to provide the value
 * to be cached. This class is not thread-safe.
 *
 * The given {@link IntSupplier#getAsInt()} method is never invoked if the value is cached.
 */
final class SimpleIntCacher implements CachedInt {
  private final IntSupplier supplier;
  private boolean isValid;
  private int value;

  SimpleIntCacher(IntSupplier supplier) {
    this.supplier = requireNonNull(supplier);
  }

  @Override
  public int value() {
    if (!isValid) {
      value = supplier.getAsInt();
      isValid = true;
    }
    return value;
  }

  @Override
  public void invalidate() {
    isValid = false;
  }
}
