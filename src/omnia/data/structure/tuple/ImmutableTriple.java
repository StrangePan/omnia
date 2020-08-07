package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableTriple<A, B, C> implements Triple<A, B, C> {

  private final A first;
  private final B second;
  private final C third;

  static <A, B, C> ImmutableTriple<A, B, C> create(A first, B second, C third) {
    return new ImmutableTriple<>(first, second, third);
  }

  private ImmutableTriple(A first, B second, C third) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableTriple
        && Objects.equals(first(), ((ImmutableTriple<?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableTriple<?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableTriple<?, ?, ?>) obj).third());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "}";
  }

  @Override
  public A first() {
    return requireNonNull(first);
  }

  @Override
  public B second() {
    return requireNonNull(second);
  }

  @Override
  public C third() {
    return requireNonNull(third);
  }

  @Override
  public <R> Triple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third());
  }

  @Override
  public <R> Triple<A, R, C> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third());
  }

  @Override
  public <R> Triple<A, B, R> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()));
  }

  @Override
  public Couple<B, C> dropFirst() {
    return Tuple.of(second(), third());
  }

  @Override
  public Couple<A, C> dropSecond() {
    return Tuple.of(first(), third());
  }

  @Override
  public Couple<A, B> dropThird() {
    return Tuple.of(first(), second());
  }

  @Override
  public <T> Quadruple<A, B, C, T> append(T object) {
    return Tuple.of(first(), second(), third(), object);
  }

  @Override
  public <D, E> Quintuple<A, B, C, D, E> concat(Couple<D, E> other) {
    return Tuple.of(first(), second(), third(), other.first(), other.second());
  }

  @Override
  public <D, E, F> Sextuple<A, B, C, D, E, F> concat(Triple<D, E, F> other) {
    return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third());
  }

  @Override
  public <D, E, F, G> Septuple<A, B, C, D, E, F, G> concat(Quadruple<D, E, F, G> other) {
    return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public <D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> concat(Quintuple<D, E, F, G, H> other) {
    return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public <D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Sextuple<D, E, F, G, H, I> other) {
    return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public <D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Septuple<D, E, F, G, H, I, J> other) {
    return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }
}
