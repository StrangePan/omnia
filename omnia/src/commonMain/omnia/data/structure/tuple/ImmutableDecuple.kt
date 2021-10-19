package omnia.data.structure.tuple

import omnia.algorithm.HashAlgorithms.Companion.hash

internal open class ImmutableDecuple<A, B, C, D, E, F, G, H, I, J>(
    override val first: A,
    override val second: B,
    override val third: C,
    override val fourth: D,
    override val fifth: E,
    override val sixth: F,
    override val seventh: G,
    override val eighth: H,
    override val ninth: I,
    override val tenth: J,
) : Decuple<A, B, C, D, E, F, G, H, I, J> {

  override fun equals(other: Any?): Boolean {
    return (!(other !is ImmutableDecuple<*, *, *, *, *, *, *, *, *, *> || first != other.first || second != other.second || third != other.third || fourth != other.fourth || fifth != other.fifth || sixth != other.sixth || seventh != other.seventh || eighth != other.eighth || ninth != other.ninth || tenth != other.tenth))
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
      ninth,
      tenth
    )
  }

  override fun toString(): String {
    return "Tuple{${first},${second},${third},${fourth},${fifth},${sixth}," +
      "${seventh},${eighth},${ninth},${tenth}}"
  }

  override fun <R> mapFirst(mapper: (A) -> R): Decuple<R, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
        mapper(first),
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapSecond(mapper: (B) -> R): Decuple<A, R, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
        mapper(second),
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapThird(mapper: (C) -> R): Decuple<A, B, R, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
        mapper(third),
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapFourth(mapper: (D) -> R): Decuple<A, B, C, R, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
        mapper(fourth),
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapFifth(mapper: (E) -> R): Decuple<A, B, C, D, R, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
        mapper(fifth),
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapSixth(mapper: (F) -> R): Decuple<A, B, C, D, E, R, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
        mapper(sixth),
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapSeventh(mapper: (G) -> R): Decuple<A, B, C, D, E, F, R, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
        mapper(seventh),
      eighth,
      ninth,
      tenth
    )
  }

  override fun <R> mapEighth(mapper: (H) -> R): Decuple<A, B, C, D, E, F, G, R, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
        mapper(eighth),
      ninth,
      tenth
    )
  }

  override fun <R> mapNinth(mapper: (I) -> R): Decuple<A, B, C, D, E, F, G, H, R, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
        mapper(ninth),
      tenth
    )
  }

  override fun <R> mapTenth(mapper: (J) -> R): Decuple<A, B, C, D, E, F, G, H, I, R> {
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
        mapper(tenth)
    )
  }

  override fun dropFirst(): Nonuple<B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropSecond(): Nonuple<A, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropThird(): Nonuple<A, B, D, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropFourth(): Nonuple<A, B, C, E, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropFifth(): Nonuple<A, B, C, D, F, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropSixth(): Nonuple<A, B, C, D, E, G, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropSeventh(): Nonuple<A, B, C, D, E, F, H, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropEighth(): Nonuple<A, B, C, D, E, F, G, I, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      ninth,
      tenth
    )
  }

  override fun dropNinth(): Nonuple<A, B, C, D, E, F, G, H, J> {
    return Tuple.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      tenth
    )
  }

  override fun dropTenth(): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
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
}