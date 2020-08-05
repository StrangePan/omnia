package omnia.data.structure.tuple;

import omnia.contract.Container;
import omnia.contract.Countable;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;

public interface Tuple extends Container, Countable {

  static <T> List<T> toList(Monuple<? extends T> monuple) {
    return ImmutableList.of(monuple.first());
  }

  static <T> List<T> toList(Couple<? extends T, ? extends T> couple) {
    return ImmutableList.of(couple.first(), couple.second());
  }

  static <T> List<T> toList(
      Triple<
          ? extends T,
          ? extends T,
          ? extends T> triple) {
    return ImmutableList.of(
        triple.first(),
        triple.second(),
        triple.third());
  }

  static <T> List<T> toList(
      Quadruple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> quadruple) {
    return ImmutableList.of(
        quadruple.first(),
        quadruple.second(),
        quadruple.third(),
        quadruple.fourth());
  }

  static <T> List<T> toList(
      Quintuple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> quintuple) {
    return ImmutableList.of(
        quintuple.first(),
        quintuple.second(),
        quintuple.third(),
        quintuple.fourth(),
        quintuple.fifth());
  }

  static <T> List<T> toList(
      Sextuple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> sextuple) {
    return ImmutableList.of(
        sextuple.first(),
        sextuple.second(),
        sextuple.third(),
        sextuple.fourth(),
        sextuple.fifth(),
        sextuple.sixth());
  }

  static <T> List<T> toList(
      Septuple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> septuple) {
    return ImmutableList.of(
        septuple.first(),
        septuple.second(),
        septuple.third(),
        septuple.fourth(),
        septuple.fifth(),
        septuple.sixth(),
        septuple.seventh());
  }

  static <T> List<T> toList(
      Octuple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> octuple) {
    return ImmutableList.of(
        octuple.first(),
        octuple.second(),
        octuple.third(),
        octuple.fourth(),
        octuple.fifth(),
        octuple.sixth(),
        octuple.seventh(),
        octuple.eighth());
  }

  static <T> List<T> toList(
      Nonuple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> nonuple) {
    return ImmutableList.of(
        nonuple.first(),
        nonuple.second(),
        nonuple.third(),
        nonuple.fourth(),
        nonuple.fifth(),
        nonuple.sixth(),
        nonuple.seventh(),
        nonuple.eighth(),
        nonuple.ninth());
  }

  static <T> List<T> toList(
      Decuple<
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T,
          ? extends T> decuple) {
    return ImmutableList.of(
        decuple.first(),
        decuple.second(),
        decuple.third(),
        decuple.fourth(),
        decuple.fifth(),
        decuple.sixth(),
        decuple.seventh(),
        decuple.eighth(),
        decuple.ninth(),
        decuple.tenth());
  }
}
