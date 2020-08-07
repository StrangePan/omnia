package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableSeptuple<A, B, C, D, E, F, G> implements Septuple<A, B, C, D, E, F, G> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;
  private final F sixth;
  private final G seventh;

  static <A, B, C, D, E, F, G> ImmutableSeptuple<A, B, C, D, E, F, G> create(A first, B second, C third, D fourth, E fifth, F sixth, G seventh) {
    return new ImmutableSeptuple<>(first, second, third, fourth, fifth, sixth, seventh);
  }

  private ImmutableSeptuple(A first, B second, C third, D fourth, E fifth, F sixth, G seventh) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
    this.sixth = requireNonNull(sixth);
    this.seventh = requireNonNull(seventh);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableSeptuple
        && Objects.equals(first(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).fourth())
        && Objects.equals(fifth(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).fifth())
        && Objects.equals(sixth(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).sixth())
        && Objects.equals(seventh(), ((ImmutableSeptuple<?, ?, ?, ?, ?, ?, ?>) obj).seventh());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "}";
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
  public G seventh() {
    return seventh;
  }

  @Override
  public <R> Septuple<R, B, C, D, E, F, G> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public <R> Septuple<A, R, C, D, E, F, G> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public <R> Septuple<A, B, R, D, E, F, G> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public <R> Septuple<A, B, C, R, E, F, G> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh());
  }

  @Override
  public <R> Septuple<A, B, C, D, R, F, G> mapFifth(Function<? super E, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh());
  }

  @Override
  public <R> Septuple<A, B, C, D, E, R, G> mapSixth(Function<? super F, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh());
  }

  @Override
  public <R> Septuple<A, B, C, D, E, F, R> mapSeventh(Function<? super G, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()));
  }

  @Override
  public Sextuple<B, C, D, E, F, G> dropFirst() {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuple<A, C, D, E, F, G> dropSecond() {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuple<A, B, D, E, F, G> dropThird() {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuple<A, B, C, E, F, G> dropFourth() {
    return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuple<A, B, C, D, F, G> dropFifth() {
    return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh());
  }

  @Override
  public Sextuple<A, B, C, D, E, G> dropSixth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh());
  }

  @Override
  public Sextuple<A, B, C, D, E, F> dropSeventh() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public <T> Octuple<A, B, C, D, E, F, G, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), object);
  }

  @Override
  public <H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Couple<H, I> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), other.first(), other.second());
  }

  @Override
  public <H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Triple<H, I, J> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), other.first(), other.second(), other.third());
  }
}
