package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleSupplier;

/**
 * A {@link MemoizedDouble} implementation that uses a client-given {@link DoubleSupplier} to
 * lazily compute the memoized value. This class is not thread-safe.
 *
 * Once computed and memoized, the {@link DoubleSupplier} reference is forgotten and never invoked
 * ever again. The given {@link DoubleSupplier#getAsDouble()} method is never invoked more than
 * once.
 */
final class SimpleDoubleMemoizer implements MemoizedDouble {
    private DoubleSupplier supplier;
    private double value;

    SimpleDoubleMemoizer(DoubleSupplier supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public double value() {
      if (supplier != null) {
        value = supplier.getAsDouble();
        supplier = null;
      }
      return value;
    }
}
