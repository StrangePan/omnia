package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

class ImmutableSextuple<A, B, C, D, E, F> implements Sextuple<A, B, C, D, E, F> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;
  private final F sixth;

  ImmutableSextuple(A first, B second, C third, D fourth, E fifth, F sixth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
    this.sixth = requireNonNull(sixth);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableSextuple
        && Objects.equals(first(), ((ImmutableSextuple<?, ?, ?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableSextuple<?, ?, ?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableSextuple<?, ?, ?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableSextuple<?, ?, ?, ?, ?, ?>) obj).fourth())
        && Objects.equals(fifth(), ((ImmutableSextuple<?, ?, ?, ?, ?, ?>) obj).fifth())
        && Objects.equals(sixth(), ((ImmutableSextuple<?, ?, ?, ?, ?, ?>) obj).sixth());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "}";
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
  public <T> Septuple<A, B, C, D, E, F, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), object);
  }

  @Override
  public <G, H> Octuple<A, B, C, D, E, F, G, H> concat(Couple<G, H> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second());
  }

  @Override
  public <G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Triple<G, H, I> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third());
  }

  @Override
  public <G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Quadruple<G, H, I, J> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third(), other.fourth());
  }
}
