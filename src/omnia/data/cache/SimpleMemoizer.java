package omnia.data.cache;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

/**
 * A {@link Memoized} implementation that uses a client-given {@link Supplier} to lazily compute
 * the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the {@link Supplier} reference is forgotten and never invoked ever
 * again. The given {@link Supplier#get()} method is never invoked more than once. Once computed, a
 * reference to the computed object is indefinitely retained. {@link Supplier#get()} is not allowed
 * to return {@code null}. A null value will result in {@link NullPointerException} being thrown.
 *
 * @param <T> the type empty value to be memoized
 */
final class SimpleMemoizer<T> implements Memoized<T> {
    private volatile Supplier<? extends T> supplier;
    private volatile T value;

    SimpleMemoizer(Supplier<? extends T> supplier) {
      this.supplier = requireNonNull(supplier);
    }

    @Override
    public T value() {
      T localValue = value;
      if (localValue == null) {
        synchronized (this) {
          localValue = value;
          if (localValue == null) {
            value = localValue = requireNonNull(supplier.get(), "memoized value cannot be null");
            supplier = null;
          }
        }
      }
      return localValue;
    }
}
