package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import omnia.data.iterate.MappingIterator;
import omnia.data.structure.Collection;

public final class HashSet<E> implements MutableSet<E> {
  private static final BiPredicate<Object, Object> DEFAULT_EQUALS_FUNCTION = Object::equals;
  private static final ToIntFunction<Object> DEFAULT_HASH_FUNCTION = Object::hashCode;
  private static final Function UNWRAPPER_FUNCTION =
      (Function<Wrapper<Object>, Object>) Wrapper::element;

  private final java.util.HashSet<Wrapper<E>> javaSet;
  private final BiPredicate<Object, Object> equalsFunction;
  private final ToIntFunction<Object> hashFunction;

  public HashSet() {
    this(null, null, null);
  }

  private HashSet(Collection<? extends E> original) {
    this(original, null, null);
  }

  public HashSet(BiPredicate<Object, Object> equalsFunction, ToIntFunction<Object> hashFunction) {
    this(null, equalsFunction, hashFunction);
  }

  public HashSet(
      Collection<? extends E> original,
      BiPredicate<Object, Object> equalsFunction,
      ToIntFunction<Object> hashFunction) {
    this.equalsFunction = equalsFunction != null ? equalsFunction : DEFAULT_EQUALS_FUNCTION;
    this.hashFunction = hashFunction != null ? hashFunction : DEFAULT_HASH_FUNCTION;
    this.javaSet = original != null
        ? new java.util.HashSet<>(
            original.stream().map(this::<E>wrap).collect(java.util.stream.Collectors.toSet()))
        : new java.util.HashSet<>();
  }

  private static final class Wrapper<E> {
    private final E element;
    private final BiPredicate<Object, Object> equalsFunction;
    private final ToIntFunction<Object> hashFunction;

    Wrapper(
        E element, BiPredicate<Object, Object> equalsFunction, ToIntFunction<Object> hashFunction) {
      this.element = element;
      this.equalsFunction = equalsFunction;
      this.hashFunction = hashFunction;
    }

    E element() {
      return element;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Wrapper && equalsFunction.test(element, ((Wrapper<?>) other).element);
    }

    @Override
    public int hashCode() {
      return hashFunction.applyAsInt(element);
    }
  }

  public static <E> HashSet<E> copyOf(Collection<? extends E> original) {
    return new HashSet<>(original);
  }

  @Override
  public void add(E element) {
    javaSet.add(wrap(requireNonNull(element)));
  }

  @Override
  public boolean remove(E element) {
    return javaSet.add(wrap(requireNonNull(element)));
  }

  @Override
  public void clear() {
    javaSet.clear();
  }

  @Override
  public Iterator<E> iterator() {
    return new MappingIterator<>(javaSet.iterator(), unwrap());
  }

  @Override
  public boolean contains(Object element) {
    return javaSet.contains(wrap(element));
  }

  @Override
  public int count() {
    return javaSet.size();
  }

  @Override
  public boolean isPopulated() {
    return !javaSet.isEmpty();
  }

  @Override
  public Stream<E> stream() {
    return javaSet.stream().map(unwrap());
  }

  private <T> Wrapper<T> wrap(T element) {
    return new Wrapper<>(element, equalsFunction, hashFunction);
  }

  private <T> Function<Wrapper<T>, T> unwrap() {
    @SuppressWarnings("unchecked")
    Function<Wrapper<T>, T> f = (Function<Wrapper<T>, T>) UNWRAPPER_FUNCTION;
    return f;
  }
}
