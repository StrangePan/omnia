package omnia.data.structure.tuple;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

class ImmutableDecuple<A, B, C, D, E, F, G, H, I, J> implements Decuple<A, B, C, D, E, F, G, H, I, J> {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;
  private final E fifth;
  private final F sixth;
  private final G seventh;
  private final H eighth;
  private final I ninth;
  private final J tenth;

  ImmutableDecuple(A first, B second, C third, D fourth, E fifth, F sixth, G seventh, H eighth, I ninth, J tenth) {
    this.first = requireNonNull(first);
    this.second = requireNonNull(second);
    this.third = requireNonNull(third);
    this.fourth = requireNonNull(fourth);
    this.fifth = requireNonNull(fifth);
    this.sixth = requireNonNull(sixth);
    this.seventh = requireNonNull(seventh);
    this.eighth = requireNonNull(eighth);
    this.ninth = requireNonNull(ninth);
    this.tenth = requireNonNull(tenth);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableDecuple
        && Objects.equals(first(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).first())
        && Objects.equals(second(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).second())
        && Objects.equals(third(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).third())
        && Objects.equals(fourth(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).fourth())
        && Objects.equals(fifth(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).fifth())
        && Objects.equals(sixth(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).sixth())
        && Objects.equals(seventh(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).seventh())
        && Objects.equals(eighth(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).eighth())
        && Objects.equals(ninth(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).ninth())
        && Objects.equals(tenth(), ((ImmutableDecuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) obj).tenth());
  }

  @Override
  public int hashCode() {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public String toString() {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "," + eighth() + "," + ninth() + "," + tenth() + "}";
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
  public J tenth() {
    return tenth;
  }

  @Override
  public <R> Decuple<R, B, C, D, E, F, G, H, I, J> mapFirst(Function<? super A, ? extends R> mapper) {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, R, C, D, E, F, G, H, I, J> mapSecond(Function<? super B, ? extends R> mapper) {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, R, D, E, F, G, H, I, J> mapThird(Function<? super C, ? extends R> mapper) {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, R, E, F, G, H, I, J> mapFourth(Function<? super D, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, D, R, F, G, H, I, J> mapFifth(Function<? super E, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, D, E, R, G, H, I, J> mapSixth(Function<? super F, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, D, E, F, R, H, I, J> mapSeventh(Function<? super G, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()), eighth(), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, D, E, F, G, R, I, J> mapEighth(Function<? super H, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), mapper.apply(eighth()), ninth(), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, D, E, F, G, H, R, J> mapNinth(Function<? super I, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), mapper.apply(ninth()), tenth());
  }

  @Override
  public <R> Decuple<A, B, C, D, E, F, G, H, I, R> mapTenth(Function<? super J, ? extends R> mapper) {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), mapper.apply(tenth()));
  }

  @Override
  public Nonuple<B, C, D, E, F, G, H, I, J> dropFirst() {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, C, D, E, F, G, H, I, J> dropSecond() {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, D, E, F, G, H, I, J> dropThird() {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, E, F, G, H, I, J> dropFourth() {
    return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, D, F, G, H, I, J> dropFifth() {
    return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, D, E, G, H, I, J> dropSixth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, D, E, F, H, I, J> dropSeventh() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, D, E, F, G, I, J> dropEighth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), ninth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, D, E, F, G, H, J> dropNinth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), tenth());
  }

  @Override
  public Nonuple<A, B, C, D, E, F, G, H, I> dropTenth() {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }
}
