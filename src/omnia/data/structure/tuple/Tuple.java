package omnia.data.structure.tuple;

import java.util.Objects;
import omnia.contract.Countable;
import omnia.data.structure.immutable.ImmutableList;

public interface Tuple extends Countable {

  static ImmutableList<?> toList(Tuple tuple) {
    Objects.requireNonNull(tuple);
    if (tuple instanceof EmptyTuple) {
      return ImmutableList.empty();
    }
    if (tuple instanceof Monuple) {
      return toList((Monuple<?>) tuple);
    }
    if (tuple instanceof Couple) {
      return toList((Couple<?, ?>) tuple);
    }
    if (tuple instanceof Triple) {
      return toList((Triple<?, ?, ?>) tuple);
    }
    if (tuple instanceof Quadruple) {
      return toList((Quadruple<?, ?, ?, ?>) tuple);
    }
    if (tuple instanceof Quintuple) {
      return toList((Quintuple<?, ?, ?, ?, ?>) tuple);
    }
    if (tuple instanceof Sextuple) {
      return toList((Sextuple<?, ?, ?, ?, ?, ?>) tuple);
    }
    if (tuple instanceof Septuple) {
      return toList((Septuple<?, ?, ?, ?, ?, ?, ?>) tuple);
    }
    if (tuple instanceof Octuple) {
      return toList((Octuple<?, ?, ?, ?, ?, ?, ?, ?>) tuple);
    }
    if (tuple instanceof Nonuple) {
      return toList((Nonuple<?, ?, ?, ?, ?, ?, ?, ?, ?>) tuple);
    }
    if (tuple instanceof Decuple) {
      return toList((Decuple<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) tuple);
    }
    throw new IllegalArgumentException("unrecognized tuple " + tuple.getClass());
  }

  static <T> ImmutableList<T> toList(Monuple<? extends T> monuple) {
    return ImmutableList.of(monuple.first());
  }

  static <T> ImmutableList<T> toList(Couple<? extends T, ? extends T> couple) {
    return ImmutableList.of(couple.first(), couple.second());
  }

  static <T> ImmutableList<T> toList(
      Triple<
          ? extends T,
          ? extends T,
          ? extends T> triple) {
    return ImmutableList.of(
        triple.first(),
        triple.second(),
        triple.third());
  }

  static <T> ImmutableList<T> toList(
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

  static <T> ImmutableList<T> toList(
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

  static <T> ImmutableList<T> toList(
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

  static <T> ImmutableList<T> toList(
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

  static <T> ImmutableList<T> toList(
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

  static <T> ImmutableList<T> toList(
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

  static <T> ImmutableList<T> toList(
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
