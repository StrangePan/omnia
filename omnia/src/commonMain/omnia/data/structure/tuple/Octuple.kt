package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.tuple.Tuples.AtLeastOctuple
import omnia.data.structure.tuple.Tuples.AtMostOctuple

open class Octuple<A, B, C, D, E, F, G, H>(
    override val first: A,
    override val second: B,
    override val third: C,
    override val fourth: D,
    override val fifth: E,
    override val sixth: F,
    override val seventh: G,
    override val eighth: H,
) : AtLeastOctuple<A, B, C, D, E, F, G, H>, AtMostOctuple {

  override val count get() = 8

  override fun equals(other: Any?): Boolean {
    return other is Octuple<*, *, *, *, *, *, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
        && fourth == other.fourth
        && fifth == other.fifth
        && sixth == other.sixth
        && seventh == other.seventh
        && eighth == other.eighth
  }

  override fun hashCode(): Int {
    return hash(first, second, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth},${fifth},${sixth},${seventh},${eighth}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Octuple<R, B, C, D, E, F, G, H> {
    return Tuple.of(
        mapper(first),
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth
    )
  }

  override fun <R> mapSecond(mapper: (B) -> R): Octuple<A, R, C, D, E, F, G, H> {
    return Tuple.of(
      first,
        mapper(second),
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth
    )
  }

  override fun <R> mapThird(mapper: (C) -> R): Octuple<A, B, R, D, E, F, G, H> {
    return Tuple.of(
      first,
      second,
        mapper(third),
      fourth,
      fifth,
      sixth,
      seventh,
      eighth
    )
  }

  override fun <R> mapFourth(mapper: (D) -> R): Octuple<A, B, C, R, E, F, G, H> {
    return Tuple.of(
      first,
      second,
      third,
        mapper(fourth),
      fifth,
      sixth,
      seventh,
      eighth
    )
  }

  override fun <R> mapFifth(mapper: (E) -> R): Octuple<A, B, C, D, R, F, G, H> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
        mapper(fifth),
      sixth,
      seventh,
      eighth
    )
  }

  override fun <R> mapSixth(mapper: (F) -> R): Octuple<A, B, C, D, E, R, G, H> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
        mapper(sixth),
      seventh,
      eighth
    )
  }

  override fun <R> mapSeventh(mapper: (G) -> R): Octuple<A, B, C, D, E, F, R, H> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
        mapper(seventh),
      eighth
    )
  }

  override fun <R> mapEighth(mapper: (H) -> R): Octuple<A, B, C, D, E, F, G, R> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
        mapper(eighth)
    )
  }

  override fun dropFirst(): Septuple<B, C, D, E, F, G, H> {
    return Tuple.of(second, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun dropSecond(): Septuple<A, C, D, E, F, G, H> {
    return Tuple.of(first, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun dropThird(): Septuple<A, B, D, E, F, G, H> {
    return Tuple.of(first, second, fourth, fifth, sixth, seventh, eighth)
  }

  override fun dropFourth(): Septuple<A, B, C, E, F, G, H> {
    return Tuple.of(first, second, third, fifth, sixth, seventh, eighth)
  }

  override fun dropFifth(): Septuple<A, B, C, D, F, G, H> {
    return Tuple.of(first, second, third, fourth, sixth, seventh, eighth)
  }

  override fun dropSixth(): Septuple<A, B, C, D, E, G, H> {
    return Tuple.of(first, second, third, fourth, fifth, seventh, eighth)
  }

  override fun dropSeventh(): Septuple<A, B, C, D, E, F, H> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, eighth)
  }

  override fun dropEighth(): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, seventh)
  }

  override fun <T> append(item: T): Nonuple<A, B, C, D, E, F, G, H, T> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
        item
    )
  }

  override fun <I, J> append(other: Couple<I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
        other.first,
        other.second
    )
  }
}