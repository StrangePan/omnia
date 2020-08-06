package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableQuadruple<A, B, C, D> implements Quadruple<A, B, C, D> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;

  static <A, B, C, D> ImmutableQuadruple<A, B, C, D> create(A first, B second, C third, D fourth) {
    return new ImmutableQuadruple<>(first, second, third, fourth);
  }

  private ImmutableQuadruple(A first, B second, C third, D fourth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableQuadruple
        && Objects.equals(first(), ((ImmutableQuadruple<?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableQuadruple<?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableQuadruple<?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableQuadruple<?, ?, ?, ?>) obj).fourth());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "}";
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
  public C third() {
    return third;
  }

  @Override
  public D fourth() {
    return fourth;
  }

  @Override
  public <R> Quadruple<R, B, C, D> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth());
  }

  @Override
  public <R> Quadruple<A, R, C, D> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth());
  }

  @Override
  public <R> Quadruple<A, B, R, D> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth());
  }

  @Override
  public <R> Quadruple<A, B, C, R> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()));
  }

  @Override
  public Triple<B, C, D> dropFirst() {
    return Tuple.of(second(), third(), fourth());
  }

  @Override
  public Triple<A, C, D> dropSecond() {
    return Tuple.of(first(), third(), fourth());
  }

  @Override
  public Triple<A, B, D> dropThird() {
    return Tuple.of(first(), second(), fourth());
  }

  @Override
  public Triple<A, B, C> dropFourth() {
    return Tuple.of(first(), second(), third());
  }

  @Override
  public <T> Quintuple<T, A, B, C, D> prepend(T object) {
    return Tuple.of(object, first(), second(), third(), fourth());
  }

  @Override
  public <T> Quintuple<A, B, C, D, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), object);
  }

  @Override
  public <E> Quintuple<A, B, C, D, E> appendAll(Monuple<E> other) {
    return Tuple.of(first(), second(), third(), fourth(), other.first());
  }

  @Override
  public <E, F> Sextuple<A, B, C, D, E, F> appendAll(Couple<E, F> other) {
    return Tuple.of(first(), second(), third(), fourth(), other.first(), other.second());
  }

  @Override
  public <E, F, G> Septuple<A, B, C, D, E, F, G> appendAll(Triple<E, F, G> other) {
    return Tuple.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third());
  }

  @Override
  public <E, F, G, H> Octuple<A, B, C, D, E, F, G, H> appendAll(Quadruple<E, F, G, H> other) {
    return Tuple.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public <E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Quintuple<E, F, G, H, I> other) {
    return Tuple.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public <E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Sextuple<E, F, G, H, I, J> other) {
    return Tuple.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }
}
