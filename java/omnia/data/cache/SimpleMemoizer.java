package omnia.data.cache;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Memoized} implementation that uses a client-given {@link Supplier} to lazily compute
 * the memoized value.
 *
 * Once computed and memoized, the {@link Supplier} reference is forgotten and never invoked ever
 * again.
 *
 * This class is thread safe. The given {@link Supplier#get()} method is never invoked more than
 * once. Once computed, a reference to the computed object is indefinitely retained.
 *
 * @param <T> the value type to be memoized
 */
final class SimpleMemoizer<T> implements Memoized<T> {
    private Supplier<T> supplier;
    private T value;

    SimpleMemoizer(Supplier<T> supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public T value() {
      if (supplier != null) {
        synchronized (this) {
          if (supplier != null) {
            value = supplier.get();
            supplier = null;
          }
        }
      }
      return value;
    }
}
