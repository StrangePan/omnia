package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

final class ImmutableSextuple<A, B, C, D, E, F> implements Sextuple<A, B, C, D, E, F> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;
  private final F sixth;

  static <A, B, C, D, E, F> ImmutableSextuple<A, B, C, D, E, F> create(A first, B second, C third, D fourth, E fifth, F sixth) {
    return new ImmutableSextuple<>(first, second, third, fourth, fifth, sixth);
  }

  private ImmutableSextuple(A first, B second, C third, D fourth, E fifth, F sixth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
    this.sixth = requireNonNull(sixth);
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
  public F sixth() {
    return sixth;
  }

  @Override
  public <R> Sextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public <R> Sextuple<A, R, C, D, E, F> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth());
  }

  @Override
  public <R> Sextuple<A, B, R, D, E, F> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth());
  }

  @Override
  public <R> Sextuple<A, B, C, R, E, F> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth());
  }

  @Override
  public <R> Sextuple<A, B, C, D, R, F> mapFifth(Function<? super E, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth());
  }

  @Override
  public <R> Sextuple<A, B, C, D, E, R> mapSixth(Function<? super F, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()));
  }

  @Override
  public Quintuple<B, C, D, E, F> dropFirst() {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public Quintuple<A, C, D, E, F> dropSecond() {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public Quintuple<A, B, D, E, F> dropThird() {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth());
  }

  @Override
  public Quintuple<A, B, C, E, F> dropFourth() {
    return Tuple.of(first(), second(), third(), fifth(), sixth());
  }

  @Override
  public Quintuple<A, B, C, D, F> dropFifth() {
    return Tuple.of(first(), second(), third(), fourth(), sixth());
  }

  @Override
  public Quintuple<A, B, C, D, E> dropSixth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth());
  }

  @Override
  public <T> Septuple<T, A, B, C, D, E, F> prepend(T object) {
    return Tuple.of(object, first(), second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public <T> Septuple<A, B, C, D, E, F, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), object);
  }

  @Override
  public <G> Septuple<A, B, C, D, E, F, G> appendAll(Monuple<G> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first());
  }

  @Override
  public <G, H> Octuple<A, B, C, D, E, F, G, H> appendAll(Couple<G, H> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second());
  }

  @Override
  public <G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Triple<G, H, I> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third());
  }

  @Override
  public <G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Quadruple<G, H, I, J> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third(), other.fourth());
  }
}
