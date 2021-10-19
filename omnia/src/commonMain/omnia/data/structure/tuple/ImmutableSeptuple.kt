package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash

internal open class ImmutableSeptuple<A, B, C, D, E, F, G>(
  override val first: A,
  override val second: B,
  override val third: C,
  override val fourth: D,
  override val fifth: E,
  override val sixth: F,
  override val seventh: G,
) : Septuple<A, B, C, D, E, F, G> {

  override fun equals(other: Any?): Boolean {
    return (other is ImmutableSeptuple<*, *, *, *, *, *, *>
        && first == other.first
        && second == other.second
        && third == other.third
        && fourth == other.fourth
        && fifth == other.fifth
        && sixth == other.sixth
        && seventh == other.seventh)
  }

  override fun hashCode(): Int {
    return hash(first, second, third, fourth, fifth, sixth, seventh)
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth},${fifth},${sixth},${seventh}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Septuple<R, B, C, D, E, F, G> {
    return Tuple.of(mapper(first), second, third, fourth, fifth, sixth, seventh)
  }

  override fun <R> mapSecond(mapper: (B) -> R): Septuple<A, R, C, D, E, F, G> {
    return Tuple.of(first, mapper(second), third, fourth, fifth, sixth, seventh)
  }

  override fun <R> mapThird(mapper: (C) -> R): Septuple<A, B, R, D, E, F, G> {
    return Tuple.of(first, second, mapper(third), fourth, fifth, sixth, seventh)
  }

  override fun <R> mapFourth(mapper: (D) -> R): Septuple<A, B, C, R, E, F, G> {
    return Tuple.of(first, second, third, mapper(fourth), fifth, sixth, seventh)
  }

  override fun <R> mapFifth(mapper: (E) -> R): Septuple<A, B, C, D, R, F, G> {
    return Tuple.of(first, second, third, fourth, mapper(fifth), sixth, seventh)
  }

  override fun <R> mapSixth(mapper: (F) -> R): Septuple<A, B, C, D, E, R, G> {
    return Tuple.of(first, second, third, fourth, fifth, mapper(sixth), seventh)
  }

  override fun <R> mapSeventh(mapper: (G) -> R): Septuple<A, B, C, D, E, F, R> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, mapper(seventh))
  }

  override fun dropFirst(): Sextuple<B, C, D, E, F, G> {
    return Tuple.of(second, third, fourth, fifth, sixth, seventh)
  }

  override fun dropSecond(): Sextuple<A, C, D, E, F, G> {
    return Tuple.of(first, third, fourth, fifth, sixth, seventh)
  }

  override fun dropThird(): Sextuple<A, B, D, E, F, G> {
    return Tuple.of(first, second, fourth, fifth, sixth, seventh)
  }

  override fun dropFourth(): Sextuple<A, B, C, E, F, G> {
    return Tuple.of(first, second, third, fifth, sixth, seventh)
  }

  override fun dropFifth(): Sextuple<A, B, C, D, F, G> {
    return Tuple.of(first, second, third, fourth, sixth, seventh)
  }

  override fun dropSixth(): Sextuple<A, B, C, D, E, G> {
    return Tuple.of(first, second, third, fourth, fifth, seventh)
  }

  override fun dropSeventh(): Sextuple<A, B, C, D, E, F> {
    return Tuple.of(first, second, third, fourth, fifth, sixth)
  }

  override fun <T> append(`object`: T): Octuple<A, B, C, D, E, F, G, T> {
    return Tuple.of(first, second, third, fourth, fifth, sixth, seventh, `object`)
  }

  override fun <H, I> append(other: Couple<H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      other.first,
      other.second
    )
  }

  override fun <H, I, J> append(other: Triple<H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      other.first,
      other.second,
      other.third
    )
  }
}