package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.LongSupplier;

/**
 * A {@link MemoizedLong} implementation that uses a client-given {@link LongSupplier} to
 * lazily compute the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the {@link LongSupplier} reference is forgotten and never invoked
 * ever again. The given {@link LongSupplier#getAsLong()} method is never invoked more than
 * once.
 */
final class SimpleLongMemoizer implements MemoizedLong {
    private volatile LongSupplier supplier;
    private volatile long value;

    SimpleLongMemoizer(LongSupplier supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public long value() {
      if (supplier != null) {
        synchronized (this) {
          if (supplier != null) {
            value = supplier.getAsLong();
            supplier = null;
          }
        }
      }
      return value;
    }
}