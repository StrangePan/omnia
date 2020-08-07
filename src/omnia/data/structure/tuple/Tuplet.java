package omnia.data.structure.tuple;

import omnia.data.structure.immutable.ImmutableList;

public interface Tuplet {

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

  static <T> ImmutableList<T> toList(Couplet<? extends T> couplet) {
    return ImmutableList.of(couplet.first(), couplet.second());
  }

  static <T> ImmutableList<T> toList(Triplet<? extends T> triplet) {
    return ImmutableList.of(triplet.first(), triplet.second(), triplet.third());
  }

  static <T> ImmutableList<T> toList(Quadruplet<? extends T> quadruplet) {
    return ImmutableList.of(quadruplet.first(), quadruplet.second(), quadruplet.third(), quadruplet.fourth());
  }

  static <T> ImmutableList<T> toList(Quintuplet<? extends T> quintuplet) {
    return ImmutableList.of(quintuplet.first(), quintuplet.second(), quintuplet.third(), quintuplet.fourth(), quintuplet.fifth());
  }

  static <T> ImmutableList<T> toList(Sextuplet<? extends T> sextuplet) {
    return ImmutableList.of(sextuplet.first(), sextuplet.second(), sextuplet.third(), sextuplet.fourth(), sextuplet.fifth(), sextuplet.sixth());
  }

  static <T> ImmutableList<T> toList(Septuplet<? extends T> septuplet) {
    return ImmutableList.of(septuplet.first(), septuplet.second(), septuplet.third(), septuplet.fourth(), septuplet.fifth(), septuplet.sixth(), septuplet.seventh());
  }

  static <T> ImmutableList<T> toList(Octuplet<? extends T> octuplet) {
    return ImmutableList.of(octuplet.first(), octuplet.second(), octuplet.third(), octuplet.fourth(), octuplet.fifth(), octuplet.sixth(), octuplet.seventh(), octuplet.eighth());
  }

  static <T> ImmutableList<T> toList(Nonuplet<? extends T> nonuplet) {
    return ImmutableList.of(nonuplet.first(), nonuplet.second(), nonuplet.third(), nonuplet.fourth(), nonuplet.fifth(), nonuplet.sixth(), nonuplet.seventh(), nonuplet.eighth(), nonuplet.ninth());
  }

  static <T> ImmutableList<T> toList(Decuplet<? extends T> decuplet) {
    return ImmutableList.of(decuplet.first(), decuplet.second(), decuplet.third(), decuplet.fourth(), decuplet.fifth(), decuplet.sixth(), decuplet.seventh(), decuplet.eighth(), decuplet.ninth(), decuplet.tenth());
  }
}
