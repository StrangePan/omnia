package omnia.data.cache;

import java.util.function.IntSupplier;

import static java.util.Objects.requireNonNull;

/**
 * A {@link MemoizedInt} implementation that uses a client-given {@link IntSupplier} to lazily
 * compute the memoized value. This class is not thread-safe.
 *
 * Once computed and memoized, the {@link IntSupplier} reference is forgotten and never invoked ever
 * again. The given {@link IntSupplier#getAsInt()} method is never invoked more than once. Once
 * computed, a reference to the computed object is indefinitely retained. {@link
 * IntSupplier#getAsInt()} is not allowed to return {@code null}. A null value will result in {@link
 * NullPointerException} being thrown.
 */
final class SimpleIntMemoizer implements MemoizedInt {
    private IntSupplier supplier;
    private int value;

    SimpleIntMemoizer(IntSupplier supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public int value() {
      if (supplier != null) {
        value = supplier.getAsInt();
        supplier = null;
      }
      return value;
    }
}
