package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.tuple.Tuples.AtLeastTriple
import omnia.data.structure.tuple.Tuples.AtMostTriple

open class Triple<A, B, C>(
    override val first: A,
    override val second: B,
    override val third: C,
) : AtLeastTriple<A, B, C>, AtMostTriple {

  override val count get() = 3

  override fun equals(other: Any?): Boolean {
    return other is Triple<*, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
  }

  override fun hashCode(): Int {
    return hash(first, second, third)
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Triple<R, B, C> {
    return Tuple.of(mapper(first), second, third)
  }

  override fun <R> mapSecond(mapper: (B) -> R): Triple<A, R, C> {
    return Tuple.of(first, mapper(second), third)
  }

  override fun <R> mapThird(mapper: (C) -> R): Triple<A, B, R> {
    return Tuple.of(first, second, mapper(third))
  }

  override fun dropFirst(): Couple<B, C> {
    return Tuple.of(second, third)
  }

  override fun dropSecond(): Couple<A, C> {
    return Tuple.of(first, third)
  }

  override fun dropThird(): Couple<A, B> {
    return Tuple.of(first, second)
  }

  override fun <T> append(item: T): Quadruple<A, B, C, T> {
    return Tuple.of(first, second, third, item)
  }

  override fun <D, E> append(other: Couple<D, E>): Quintuple<A, B, C, D, E> {
    return Tuple.of(first, second, third, other.first, other.second)
  }

  override fun <D, E, F> append(other: Triple<D, E, F>): Sextuple<A, B, C, D, E, F> {
    return Tuple.of(first, second, third, other.first, other.second, other.third)
  }

  override fun <D, E, F, G> append(other: Quadruple<D, E, F, G>): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(
      first,
      second,
      third,
        other.first,
        other.second,
        other.third,
        other.fourth
    )
  }

  override fun <D, E, F, G, H> append(other: Quintuple<D, E, F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first,
      second,
      third,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth
    )
  }

  override fun <D, E, F, G, H, I> append(other: Sextuple<D, E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth,
        other.sixth
    )
  }

  override fun <D, E, F, G, H, I, J> append(other: Septuple<D, E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth,
        other.sixth,
        other.seventh
    )
  }
}