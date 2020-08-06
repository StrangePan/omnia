package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableQuintuple<A, B, C, D, E> implements Quintuple<A, B, C, D, E> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;

  static <A, B, C, D, E> ImmutableQuintuple<A, B, C, D, E> create(A first, B second, C third, D fourth, E fifth) {
    return new ImmutableQuintuple<>(first, second, third, fourth, fifth);
  }

  private ImmutableQuintuple(A first, B second, C third, D fourth, E fifth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableQuintuple
        && Objects.equals(first(), ((ImmutableQuintuple<?, ?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableQuintuple<?, ?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableQuintuple<?, ?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableQuintuple<?, ?, ?, ?, ?>) obj).fourth())
        && Objects.equals(fifth(), ((ImmutableQuintuple<?, ?, ?, ?, ?>) obj).fifth());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth(), fifth());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "}";
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
  public E fifth() {
    return fifth;
  }

  @Override
  public <R> Quintuple<R, B, C, D, E> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth());
  }

  @Override
  public <R> Quintuple<A, R, C, D, E> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth());
  }

  @Override
  public <R> Quintuple<A, B, R, D, E> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth());
  }

  @Override
  public <R> Quintuple<A, B, C, R, E> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth());
  }

  @Override
  public <R> Quintuple<A, B, C, D, R> mapFifth(Function<? super E, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()));
  }

  @Override
  public Quadruple<B, C, D, E> dropFirst() {
    return Tuple.of(second(), third(), fourth(), fifth());
  }

  @Override
  public Quadruple<A, C, D, E> dropSecond() {
    return Tuple.of(first(), third(), fourth(), fifth());
  }

  @Override
  public Quadruple<A, B, D, E> dropThird() {
    return Tuple.of(first(), second(), fourth(), fifth());
  }

  @Override
  public Quadruple<A, B, C, E> dropFourth() {
    return Tuple.of(first(), second(), third(), fifth());
  }

  @Override
  public Quadruple<A, B, C, D> dropFifth() {
    return Tuple.of(first(), second(), third(), fourth());
  }

  @Override
  public <T> Sextuple<A, B, C, D, E, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), object);
  }

  @Override
  public <F> Sextuple<A, B, C, D, E, F> concat(Monuple<F> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), other.first());
  }

  @Override
  public <F, G> Septuple<A, B, C, D, E, F, G> concat(Couple<F, G> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second());
  }

  @Override
  public <F, G, H> Octuple<A, B, C, D, E, F, G, H> concat(Triple<F, G, H> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second(), other.third());
  }

  @Override
  public <F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Quadruple<F, G, H, I> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public <F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Quintuple<F, G, H, I, J> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }
}
