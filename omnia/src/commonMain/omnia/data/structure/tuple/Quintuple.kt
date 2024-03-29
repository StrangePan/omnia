package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.tuple.Tuples.AtLeastQuintuple
import omnia.data.structure.tuple.Tuples.AtMostQuintuple

open class Quintuple<A, B, C, D, E>(
  override val first: A,
  override val second: B,
  override val third: C,
  override val fourth: D,
  override val fifth: E,
) : AtLeastQuintuple<A, B, C, D, E>, AtMostQuintuple {

  override val count get() = 5

  override fun equals(other: Any?): Boolean {
    return other is Quintuple<*, *, *, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
        && fourth == other.fourth
        && fifth == other.fifth
  }

  override fun hashCode(): Int {
    return hash(first, second, third, fourth, fifth)
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth},${fifth}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Quintuple<R, B, C, D, E> {
    return Tuple.of(mapper(first), second, third, fourth, fifth)
  }

  override fun <R> mapSecond(mapper: (B) -> R): Quintuple<A, R, C, D, E> {
    return Tuple.of(first, mapper(second), third, fourth, fifth)
  }

  override fun <R> mapThird(mapper: (C) -> R): Quintuple<A, B, R, D, E> {
    return Tuple.of(first, second, mapper(third), fourth, fifth)
  }

  override fun <R> mapFourth(mapper: (D) -> R): Quintuple<A, B, C, R, E> {
    return Tuple.of(first, second, third, mapper(fourth), fifth)
  }

  override fun <R> mapFifth(mapper: (E) -> R): Quintuple<A, B, C, D, R> {
    return Tuple.of(first, second, third, fourth, mapper(fifth))
  }

  override fun dropFirst(): Quadruple<B, C, D, E> {
    return Tuple.of(second, third, fourth, fifth)
  }

  override fun dropSecond(): Quadruple<A, C, D, E> {
    return Tuple.of(first, third, fourth, fifth)
  }

  override fun dropThird(): Quadruple<A, B, D, E> {
    return Tuple.of(first, second, fourth, fifth)
  }

  override fun dropFourth(): Quadruple<A, B, C, E> {
    return Tuple.of(first, second, third, fifth)
  }

  override fun dropFifth(): Quadruple<A, B, C, D> {
    return Tuple.of(first, second, third, fourth)
  }

  override fun <T> append(item: T): Sextuple<A, B, C, D, E, T> {
    return Tuple.of(first, second, third, fourth, fifth, item)
  }

  override fun <F, G> append(other: Couple<F, G>): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(first, second, third, fourth, fifth, other.first, other.second)
  }

  override fun <F, G, H> append(other: Triple<F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      other.first,
      other.second,
      other.third
    )
  }

  override fun <F, G, H, I> append(other: Quadruple<F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      other.first,
      other.second,
      other.third,
      other.fourth
    )
  }

  override fun <F, G, H, I, J> append(other: Quintuple<F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      other.first,
      other.second,
      other.third,
      other.fourth,
      other.fifth
    )
  }
}