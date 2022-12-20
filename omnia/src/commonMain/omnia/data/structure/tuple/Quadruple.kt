package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.tuple.Tuples.AtLeastQuadruple
import omnia.data.structure.tuple.Tuples.AtMostQuadruple

open class Quadruple<A, B, C, D>(
  override val first: A,
  override val second: B,
  override val third: C,
  override val fourth: D,
) : AtLeastQuadruple<A, B, C, D>, AtMostQuadruple {

  override val count get() = 4

  override fun equals(other: Any?): Boolean {
    return other is Quadruple<*, *, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
        && fourth == other.fourth
  }

  override fun hashCode(): Int {
    return hash(first, second, third, fourth)
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Quadruple<R, B, C, D> {
    return Tuple.of(mapper(first), second, third, fourth)
  }

  override fun <R> mapSecond(mapper: (B) -> R): Quadruple<A, R, C, D> {
    return Tuple.of(first, mapper(second), third, fourth)
  }

  override fun <R> mapThird(mapper: (C) -> R): Quadruple<A, B, R, D> {
    return Tuple.of(first, second, mapper(third), fourth)
  }

  override fun <R> mapFourth(mapper: (D) -> R): Quadruple<A, B, C, R> {
    return Tuple.of(first, second, third, mapper(fourth))
  }

  override fun dropFirst(): Triple<B, C, D> {
    return Tuple.of(second, third, fourth)
  }

  override fun dropSecond(): Triple<A, C, D> {
    return Tuple.of(first, third, fourth)
  }

  override fun dropThird(): Triple<A, B, D> {
    return Tuple.of(first, second, fourth)
  }

  override fun dropFourth(): Triple<A, B, C> {
    return Tuple.of(first, second, third)
  }

  override fun <T> append(item: T): Quintuple<A, B, C, D, T> {
    return Tuple.of(first, second, third, fourth, item)
  }

  override fun <E, F> append(other: Couple<E, F>): Sextuple<A, B, C, D, E, F> {
    return Tuple.of(first, second, third, fourth, other.first, other.second)
  }

  override fun <E, F, G> append(other: Triple<E, F, G>): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third
    )
  }

  override fun <E, F, G, H> append(other: Quadruple<E, F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third,
      other.fourth
    )
  }

  override fun <E, F, G, H, I> append(other: Quintuple<E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third,
      other.fourth,
      other.fifth
    )
  }

  override fun <E, F, G, H, I, J> append(other: Sextuple<E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third,
      other.fourth,
      other.fifth,
      other.sixth
    )
  }
}