package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableMonuple<A> implements Monuple<A> {

  private final A first;

  static <A> ImmutableMonuple<A> create(A first) {
    return new ImmutableMonuple<>(first);
  }

  private ImmutableMonuple(A first) {
    this.first = requireNonNull(first);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableMonuple
        && Objects.equals(first(), ((ImmutableMonuple<?>) obj).first());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "}";
  }

  @Override
  public A first() {
    return first;
  }

  @Override
  public <R> Monuple<R> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()));
  }

  @Override
  public EmptyTuple dropFirst() {
    return Tuple.empty();
  }

  @Override
  public <T> Couple<A, T> append(T object) {
    return Tuple.of(first(), object);
  }

  @Override
  public <B> Couple<A, B> concat(Monuple<B> other) {
    return Tuple.of(first(), other.first());
  }

  @Override
  public <B, C> Triple<A, B, C> concat(Couple<B, C> other) {
    return Tuple.of(first(), other.first(), other.second());
  }

  @Override
  public <B, C, D> Quadruple<A, B, C, D> concat(Triple<B, C, D> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third());
  }

  @Override
  public <B, C, D, E> Quintuple<A, B, C, D, E> concat(Quadruple<B, C, D, E> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public <B, C, D, E, F> Sextuple<A, B, C, D, E, F> concat(Quintuple<B, C, D, E, F> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public <B, C, D, E, F, G> Septuple<A, B, C, D, E, F, G> concat(Sextuple<B, C, D, E, F, G> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public <B, C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> concat(Septuple<B, C, D, E, F, G, H> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }

  @Override
  public <B, C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Octuple<B, C, D, E, F, G, H, I> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth());
  }

  @Override
  public <B, C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Nonuple<B, C, D, E, F, G, H, I, J> other) {
    return Tuple.of(first(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth(), other.ninth());
  }
}
