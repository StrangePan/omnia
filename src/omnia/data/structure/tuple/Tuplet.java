package omnia.data.structure.tuple;

import omnia.data.structure.List;

public interface Tuplet<T> extends Tuple, List<T> {

  static <T> Couplet<T> of(T first, T second) {
    return new ImmutableCouplet<>(first, second);
  }

  static <T> Triplet<T> of(T first, T second, T third) {
    return new ImmutableTriplet<>(first, second, third);
  }

  static <T> Quadruplet<T> of(T first, T second, T third, T fourth) {
    return new ImmutableQuadruplet<>(first, second, third, fourth);
  }

  static <T> Quintuplet<T> of(T first, T second, T third, T fourth, T fifth) {
    return new ImmutableQuintuplet<>(first, second, third, fourth, fifth);
  }

  static <T> Sextuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth) {
    return new ImmutableSextuplet<>(first, second, third, fourth, fifth, sixth);
  }

  static <T> Septuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh) {
    return new ImmutableSeptuplet<>(first, second, third, fourth, fifth, sixth, seventh);
  }

  static <T> Octuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth) {
    return new ImmutableOctuplet<>(first, second, third, fourth, fifth, sixth, seventh, eighth);
  }

  static <T> Nonuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth) {
    return new ImmutableNonuplet<>(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth);
  }

  static <T> Decuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth, T tenth) {
    return new ImmutableDecuplet<>(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth);
  }

  @SuppressWarnings("unchecked")
  static <T> Couplet<T> copyOf(Couple<? extends T, ? extends T> couplet) {
    return couplet instanceof ImmutableCouplet
        ? (ImmutableCouplet<T>) couplet
        : Tuplet.of(couplet.first(), couplet.second());
  }

  @SuppressWarnings("unchecked")
  static <T> Triplet<T> copyOf(Triple<? extends T, ? extends T, ? extends T> triple) {
    return triple instanceof ImmutableTriplet
        ? (ImmutableTriplet<T>) triple
        : Tuplet.of(triple.first(), triple.second(), triple.third());
  }

  @SuppressWarnings("unchecked")
  static <T> Quadruplet<T> copyOf(Quadruple<? extends T, ? extends T, ? extends T, ? extends T> quadruple) {
    return quadruple instanceof ImmutableQuadruplet
        ? (ImmutableQuadruplet<T>) quadruple
        : Tuplet.of(quadruple.first(), quadruple.second(), quadruple.third(), quadruple.fourth());
  }

  @SuppressWarnings("unchecked")
  static <T> Quintuplet<T> copyOf(Quintuple<? extends T, ? extends T, ? extends T, ? extends T, ? extends T> quintuple) {
    return quintuple instanceof ImmutableQuintuplet
        ? (ImmutableQuintuplet<T>) quintuple
        : Tuplet.of(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), quintuple.fifth());
  }

  @SuppressWarnings("unchecked")
  static <T> Sextuplet<T> copyOf(Sextuple<? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T> sextuple) {
    return sextuple instanceof ImmutableSextuplet
        ? (ImmutableSextuplet<T>) sextuple
        : Tuplet.of(sextuple.first(), sextuple.second(), sextuple.third(), sextuple.fourth(), sextuple.fifth(), sextuple.sixth());
  }

  @SuppressWarnings("unchecked")
  static <T> Septuplet<T> copyOf(Septuple<? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T> septuple) {
    return septuple instanceof ImmutableSeptuplet
        ? (ImmutableSeptuplet<T>) septuple
        : Tuplet.of(septuple.first(), septuple.second(), septuple.third(), septuple.fourth(), septuple.fifth(), septuple.sixth(), septuple.seventh());
  }

  @SuppressWarnings("unchecked")
  static <T> Octuplet<T> copyOf(Octuple<? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T> octuple) {
    return octuple instanceof ImmutableOctuplet
        ? (ImmutableOctuplet<T>) octuple
        : Tuplet.of(octuple.first(), octuple.second(), octuple.third(), octuple.fourth(), octuple.fifth(), octuple.sixth(), octuple.seventh(), octuple.eighth());
  }

  @SuppressWarnings("unchecked")
  static <T> Nonuplet<T> copyOf(Nonuple<? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T> nonuple) {
    return nonuple instanceof ImmutableNonuplet
        ? (ImmutableNonuplet<T>) nonuple
        : Tuplet.of(nonuple.first(), nonuple.second(), nonuple.third(), nonuple.fourth(), nonuple.fifth(), nonuple.sixth(), nonuple.seventh(), nonuple.eighth(), nonuple.ninth());
  }

  @SuppressWarnings("unchecked")
  static <T> Decuplet<T> copyOf(Decuple<? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T, ? extends T> decuple) {
    return decuple instanceof ImmutableDecuplet
        ? (ImmutableDecuplet<T>) decuple
        : Tuplet.of(decuple.first(), decuple.second(), decuple.third(), decuple.fourth(), decuple.fifth(), decuple.sixth(), decuple.seventh(), decuple.eighth(), decuple.ninth(), decuple.tenth());
  }
}
