package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.tuple.Tuples.AtLeastSextuple
import omnia.data.structure.tuple.Tuples.AtMostSextuple

open class Sextuple<A, B, C, D, E, F>(
  override val first: A,
  override val second: B,
  override val third: C,
  override val fourth: D,
  override val fifth: E,
  override val sixth: F,
) : AtLeastSextuple<A, B, C, D, E, F>, AtMostSextuple {

  override val count get() = 6

  override fun equals(other: Any?): Boolean {
    return other is Sextuple<*, *, *, *, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
        && fourth == other.fourth
        && fifth == other.fifth
        && sixth == other.sixth
  }

  override fun hashCode(): Int {
    return hash(first, second, third, fourth, fifth, sixth)
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth},${fifth},${sixth}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Sextuple<R, B, C, D, E, F> {
    return Tuple.of(mapper(first), second, third, fourth, fifth, sixth)
  }

  override fun <R> mapSecond(mapper: (B) -> R): Sextuple<A, R, C, D, E, F> {
    return Tuple.of(first, mapper(second), third, fourth, fifth, sixth)
  }

  override fun <R> mapThird(mapper: (C) -> R): Sextuple<A, B, R, D, E, F> {
    return Tuple.of(first, second, mapper(third), fourth, fifth, sixth)
  }

  override fun <R> mapFourth(mapper: (D) -> R): Sextuple<A, B, C, R, E, F> {
    return Tuple.of(first, second, third, mapper(fourth), fifth, sixth)
  }

  override fun <R> mapFifth(mapper: (E) -> R): Sextuple<A, B, C, D, R, F> {
    return Tuple.of(first, second, third, fourth, mapper(fifth), sixth)
  }

  override fun <R> mapSixth(mapper: (F) -> R): Sextuple<A, B, C, D, E, R> {
    return Tuple.of(first, second, third, fourth, fifth, mapper(sixth))
  }

  override fun dropFirst(): Quintuple<B, C, D, E, F> {
    return Tuple.of(second, third, fourth, fifth, sixth)
  }

  override fun dropSecond(): Quintuple<A, C, D, E, F> {
    return Tuple.of(first, third, fourth, fifth, sixth)
  }

  override fun dropThird(): Quintuple<A, B, D, E, F> {
    return Tuple.of(first, second, fourth, fifth, sixth)
  }

  override fun dropFourth(): Quintuple<A, B, C, E, F> {
    return Tuple.of(first, second, third, fifth, sixth)
  }

  override fun dropFifth(): Quintuple<A, B, C, D, F> {
    return Tuple.of(first, second, third, fourth, sixth)
  }

  override fun dropSixth(): Quintuple<A, B, C, D, E> {
    return Tuple.of(first, second, third, fourth, fifth)
  }

  override fun <T> append(item: T): Septuple<A, B, C, D, E, F, T> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, item)
  }

  override fun <G, H> append(other: Couple<G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      other.first,
      other.second
    )
  }

  override fun <G, H, I> append(other: Triple<G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      other.first,
      other.second,
      other.third
    )
  }

  override fun <G, H, I, J> append(other: Quadruple<G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      other.first,
      other.second,
      other.third,
      other.fourth
    )
  }
}