package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableNonuple<A, B, C, D, E, F, G, H, I> implements Nonuple<A, B, C, D, E, F, G, H, I> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;
  private final F sixth;
  private final G seventh;
  private final H eighth;
  private final I ninth;

  static <A, B, C, D, E, F, G, H, I> ImmutableNonuple<A, B, C, D, E, F, G, H, I> create(A first, B second, C third, D fourth, E fifth, F sixth, G seventh, H eighth, I ninth) {
    return new ImmutableNonuple<>(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth);
  }

  private ImmutableNonuple(A first, B second, C third, D fourth, E fifth, F sixth, G seventh, H eighth, I ninth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
    this.sixth = requireNonNull(sixth);
    this.seventh = requireNonNull(seventh);
    this.eighth = requireNonNull(eighth);
    this.ninth = requireNonNull(ninth);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableNonuple
        && Objects.equals(first(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).fourth())
        && Objects.equals(fifth(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).fifth())
        && Objects.equals(sixth(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).sixth())
        && Objects.equals(seventh(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).seventh())
        && Objects.equals(eighth(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).eighth())
        && Objects.equals(ninth(), ((ImmutableNonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).ninth());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "," + eighth() + "," + ninth() + "}";
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
  public H eighth() {
    return eighth;
  }

  @Override
  public I ninth() {
    return ninth;
  }

  @Override
  public <R> Nonuple<R, B, C, D, E, F, G, H, I> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, R, C, D, E, F, G, H, I> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, B, R, D, E, F, G, H, I> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, B, C, R, E, F, G, H, I> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, B, C, D, R, F, G, H, I> mapFifth(Function<? super E, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, B, C, D, E, R, G, H, I> mapSixth(Function<? super F, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh(), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, B, C, D, E, F, R, H, I> mapSeventh(Function<? super G, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()), eighth(), ninth());
  }

  @Override
  public <R> Nonuple<A, B, C, D, E, F, G, R, I> mapEighth(Function<? super H, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), mapper.apply(eighth()), ninth());
  }

  @Override
  public <R> Nonuple<A, B, C, D, E, F, G, H, R> mapNinth(Function<? super I, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), mapper.apply(ninth()));
  }

  @Override
  public Octuple<B, C, D, E, F, G, H, I> dropFirst() {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuple<A, C, D, E, F, G, H, I> dropSecond() {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuple<A, B, D, E, F, G, H, I> dropThird() {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuple<A, B, C, E, F, G, H, I> dropFourth() {
    return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuple<A, B, C, D, F, G, H, I> dropFifth() {
    return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuple<A, B, C, D, E, G, H, I> dropSixth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuple<A, B, C, D, E, F, H, I> dropSeventh() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth(), ninth());
  }

  @Override
  public Octuple<A, B, C, D, E, F, G, I> dropEighth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), ninth());
  }

  @Override
  public Octuple<A, B, C, D, E, F, G, H> dropNinth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public <T> Decuple<T, A, B, C, D, E, F, G, H, I> prepend(T object) {
    return Tuple.of(object, first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public <T> Decuple<A, B, C, D, E, F, G, H, I, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), object);
  }

  @Override
  public <J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Monuple<J> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), other.first());
  }
}
