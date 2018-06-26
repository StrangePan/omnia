package omnia.data.cache;

import java.util.function.LongSupplier;

import static java.util.Objects.requireNonNull;

/**
 * A {@link MemoizedLong} implementation that uses a client-given {@link LongSupplier} to
 * lazily compute the memoized value. This class is not thread-safe.
 *
 * Once computed and memoized, the {@link LongSupplier} reference is forgotten and never invoked
 * ever again. The given {@link LongSupplier#getAsLong()} method is never invoked more than
 * once.
 */
final class SimpleLongMemoizer implements MemoizedLong {
    private LongSupplier supplier;
    private long value;

    SimpleLongMemoizer(LongSupplier supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public long value() {
      if (supplier != null) {
        value = supplier.getAsLong();
        supplier = null;
      }
      return value;
    }
}
