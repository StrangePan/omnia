package omnia.data.structure.tuple;

final class ImmutableEmptyTuple implements EmptyTuple {

  static ImmutableEmptyTuple create() {
    return new ImmutableEmptyTuple();
  }

  private ImmutableEmptyTuple() {}

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ImmutableEmptyTuple;
  }

  @Override
  public int hashCode() {
    return ImmutableEmptyTuple.class.hashCode();
  }

  @Override
  public String toString() {
    return "Tuple{}";
  }

  @Override
  public <T> Monuple<T> append(T object) {
    return Tuple.of(object);
  }

  @Override
  public <A> Monuple<A> concat(Monuple<A> other) {
    return Tuple.of(other.first());
  }

  @Override
  public <A, B> Couple<A, B> concat(Couple<A, B> other) {
    return Tuple.of(other.first(), other.second());
  }

  @Override
  public <A, B, C> Triple<A, B, C> concat(Triple<A, B, C> other) {
    return Tuple.of(other.first(), other.second(), other.third());
  }

  @Override
  public <A, B, C, D> Quadruple<A, B, C, D> concat(Quadruple<A, B, C, D> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public <A, B, C, D, E> Quintuple<A, B, C, D, E> concat(Quintuple<A, B, C, D, E> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public <A, B, C, D, E, F> Sextuple<A, B, C, D, E, F> concat(Sextuple<A, B, C, D, E, F> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public <A, B, C, D, E, F, G> Septuple<A, B, C, D, E, F, G> concat(Septuple<A, B, C, D, E, F, G> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }

  @Override
  public <A, B, C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> concat(Octuple<A, B, C, D, E, F, G, H> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth());
  }

  @Override
  public <A, B, C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Nonuple<A, B, C, D, E, F, G, H, I> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth(), other.ninth());
  }

  @Override
  public <A, B, C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Decuple<A, B, C, D, E, F, G, H, I, J> other) {
    return Tuple.of(other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth(), other.ninth(), other.tenth());
  }
}
