package omnia.data.cache;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Memoized} implementation that uses a client-given {@link Supplier} to lazily compute
 * the memoized value. This class is not thread-safe.
 *
 * Once computed and memoized, the {@link Supplier} reference is forgotten and never invoked ever
 * again. The given {@link Supplier#get()} method is never invoked more than once. Once computed, a
 * reference to the computed object is indefinitely retained. {@link Supplier#get()} is not allowed
 * to return {@code null}. A null value will result in {@link NullPointerException} being thrown.
 *
 * @param <T> the type of value to be memoized
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
        value = requireNonNull(supplier.get());
        supplier = null;
      }
      return value;
    }
}
