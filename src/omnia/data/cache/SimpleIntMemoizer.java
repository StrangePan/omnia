package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.IntSupplier;

/**
 * A {@link MemoizedInt} implementation that uses a client-given {@link IntSupplier} to lazily
 * compute the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the {@link IntSupplier} reference is forgotten and never invoked ever
 * again. The given {@link IntSupplier#getAsInt()} method is never invoked more than once.
 */
final class SimpleIntMemoizer implements MemoizedInt {
    private volatile IntSupplier supplier;
    private volatile int value;

    SimpleIntMemoizer(IntSupplier supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public int value() {
      if (supplier != null) {
        synchronized (this) {
          if (supplier != null) {
            value = supplier.getAsInt();
            supplier = null;
          }
        }
      }
      return value;
    }
}
