package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableCouple<A, B> implements Couple<A, B> {

  private final A first;
  private final B second;

  static <A, B> ImmutableCouple<A, B> create(A first, B second) {
    return new ImmutableCouple<>(first, second);
  }

  private ImmutableCouple(A first, B second) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableCouple
        && Objects.equals(first(), ((ImmutableCouple<?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableCouple<?, ?>) obj).second());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "}";
  }

  @Override
  public A first() {
    return first;
  }

  @Override
  public B second() {
    return second;
  }

  @Override
  public <R> Couple<R, B> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second());
  }

  @Override
  public <R> Couple<A, R> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()));
  }

  @Override
  public <T> Triple<A, B, T> append(T object) {
    return Tuple.of(first(), second(), object);
  }

  @Override
  public <C, D> Quadruple<A, B, C, D> concat(Couple<C, D> other) {
    return Tuple.of(first(), second(), other.first(), other.second());
  }

  @Override
  public <C, D, E> Quintuple<A, B, C, D, E> concat(Triple<C, D, E> other) {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third());
  }

  @Override
  public <C, D, E, F> Sextuple<A, B, C, D, E, F> concat(Quadruple<C, D, E, F> other) {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public <C, D, E, F, G> Septuple<A, B, C, D, E, F, G> concat(Quintuple<C, D, E, F, G> other) {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public <C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> concat(Sextuple<C, D, E, F, G, H> other) {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public <C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Septuple<C, D, E, F, G, H, I> other) {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }

  @Override
  public <C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Octuple<C, D, E, F, G, H, I, J> other) {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth());
  }
}
