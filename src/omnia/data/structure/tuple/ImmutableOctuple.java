package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

final class ImmutableOctuple<A, B, C, D, E, F, G, H> implements Octuple<A, B, C, D, E, F, G, H> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;
  private final F sixth;
  private final G seventh;
  private final H eighth;

  static <A, B, C, D, E, F, G, H> ImmutableOctuple<A, B, C, D, E, F, G, H> create(A first, B second, C third, D fourth, E fifth, F sixth, G seventh, H eighth) {
    return new ImmutableOctuple<>(first, second, third, fourth, fifth, sixth, seventh, eighth);
  }

  private ImmutableOctuple(A first, B second, C third, D fourth, E fifth, F sixth, G seventh, H eighth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
    this.sixth = requireNonNull(sixth);
    this.seventh = requireNonNull(seventh);
    this.eighth = requireNonNull(eighth);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableOctuple
        && Objects.equals(first(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).fourth())
        && Objects.equals(fifth(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).fifth())
        && Objects.equals(sixth(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).sixth())
        && Objects.equals(seventh(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).seventh())
        && Objects.equals(eighth(), ((ImmutableOctuple<?, ?, ?, ?, ?, ?, ?, ?>) obj).eighth());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "," + eighth() + "}";
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
  public <R> Octuple<R, B, C, D, E, F, G, H> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public <R> Octuple<A, R, C, D, E, F, G, H> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public <R> Octuple<A, B, R, D, E, F, G, H> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public <R> Octuple<A, B, C, R, E, F, G, H> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public <R> Octuple<A, B, C, D, R, F, G, H> mapFifth(Function<? super E, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh(), eighth());
  }

  @Override
  public <R> Octuple<A, B, C, D, E, R, G, H> mapSixth(Function<? super F, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh(), eighth());
  }

  @Override
  public <R> Octuple<A, B, C, D, E, F, R, H> mapSeventh(Function<? super G, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()), eighth());
  }

  @Override
  public <R> Octuple<A, B, C, D, E, F, G, R> mapEighth(Function<? super H, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), mapper.apply(eighth()));
  }

  @Override
  public Septuple<B, C, D, E, F, G, H> dropFirst() {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuple<A, C, D, E, F, G, H> dropSecond() {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuple<A, B, D, E, F, G, H> dropThird() {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuple<A, B, C, E, F, G, H> dropFourth() {
    return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuple<A, B, C, D, F, G, H> dropFifth() {
    return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuple<A, B, C, D, E, G, H> dropSixth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth());
  }

  @Override
  public Septuple<A, B, C, D, E, F, H> dropSeventh() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth());
  }

  @Override
  public Septuple<A, B, C, D, E, F, G> dropEighth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public <T> Nonuple<A, B, C, D, E, F, G, H, T> append(T object) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), object);
  }

  @Override
  public <I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Monuple<I> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), other.first());
  }

  @Override
  public <I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Couple<I, J> other) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), other.first(), other.second());
  }
}
