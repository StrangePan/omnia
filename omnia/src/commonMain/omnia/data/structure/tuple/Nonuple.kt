package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.tuple.Tuples.AtLeastNonuple
import omnia.data.structure.tuple.Tuples.AtMostNonuple

open class Nonuple<A, B, C, D, E, F, G, H, I>(
    override val first: A,
    override val second: B,
    override val third: C,
    override val fourth: D,
    override val fifth: E,
    override val sixth: F,
    override val seventh: G,
    override val eighth: H,
    override val ninth: I,
): AtLeastNonuple<A, B, C, D, E, F, G, H, I>, AtMostNonuple {

  override val count get() = 9

  override fun equals(other: Any?): Boolean {
    return other is Nonuple<*, *, *, *, *, *, *, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
        && fourth == other.fourth
        && fifth == other.fifth
        && sixth == other.sixth
        && seventh == other.seventh
        && eighth == other.eighth
        && ninth == other.ninth
  }

  override fun hashCode(): Int {
    return hash(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
    )
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth},${fifth},${sixth}," +
      "${seventh},${eighth},${ninth}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Nonuple<R, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      mapper(first),
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
    )
  }

  override fun <R> mapSecond(mapper: (B) -> R): Nonuple<A, R, C, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      mapper(second),
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
    )
  }

  override fun <R> mapThird(mapper: (C) -> R): Nonuple<A, B, R, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      mapper(third),
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
    )
  }

  override fun <R> mapFourth(mapper: (D) -> R): Nonuple<A, B, C, R, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      mapper(fourth),
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
    )
  }

  override fun <R> mapFifth(mapper: (E) -> R): Nonuple<A, B, C, D, R, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      mapper(fifth),
      sixth,
      seventh,
      eighth,
      ninth
    )
  }

  override fun <R> mapSixth(mapper: (F) -> R): Nonuple<A, B, C, D, E, R, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      mapper(sixth),
      seventh,
      eighth,
      ninth
    )
  }

  override fun <R> mapSeventh(mapper: (G) -> R): Nonuple<A, B, C, D, E, F, R, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      mapper(seventh),
      eighth,
      ninth
    )
  }

  override fun <R> mapEighth(mapper: (H) -> R): Nonuple<A, B, C, D, E, F, G, R, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      mapper(eighth),
      ninth
    )
  }

  override fun <R> mapNinth(mapper: (I) -> R): Nonuple<A, B, C, D, E, F, G, H, R> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      mapper(ninth)
    )
  }

  override fun dropFirst(): Octuple<B, C, D, E, F, G, H, I> {
    return Tuple.of(second, third, fourth, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropSecond(): Octuple<A, C, D, E, F, G, H, I> {
    return Tuple.of(first, third, fourth, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropThird(): Octuple<A, B, D, E, F, G, H, I> {
    return Tuple.of(first, second, fourth, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropFourth(): Octuple<A, B, C, E, F, G, H, I> {
    return Tuple.of(first, second, third, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropFifth(): Octuple<A, B, C, D, F, G, H, I> {
    return Tuple.of(first, second, third, fourth, sixth, seventh, eighth, ninth)
  }

  override fun dropSixth(): Octuple<A, B, C, D, E, G, H, I> {
    return Tuple.of(first, second, third, fourth, fifth, seventh, eighth, ninth)
  }

  override fun dropSeventh(): Octuple<A, B, C, D, E, F, H, I> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, eighth, ninth)
  }

  override fun dropEighth(): Octuple<A, B, C, D, E, F, G, I> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, seventh, ninth)
  }

  override fun dropNinth(): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun <T> append(item: T): Decuple<A, B, C, D, E, F, G, H, I, T> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      item
    )
  }
}