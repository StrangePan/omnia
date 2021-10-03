package omnia.data.structure.tuple

import java.util.Objects

internal open class ImmutableSextuple<A, B, C, D, E, F>(
  private val first: A,
  private val second: B,
  private val third: C,
  private val fourth: D,
  private val fifth: E,
  private val sixth: F,
) : Sextuple<A, B, C, D, E, F> {

  override fun equals(other: Any?): Boolean {
    return other is ImmutableSextuple<*, *, *, *, *, *>
        && first() == other.first()
        && second() == other.second()
        && third() == other.third()
        && fourth() == other.fourth()
        && fifth() == other.fifth()
        && sixth() == other.sixth()
  }

  override fun hashCode(): Int {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth())
  }

  override fun toString(): String {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "}"
  }

  override fun first(): A {
    return first
  }

  override fun second(): B {
    return second
  }

  override fun third(): C {
    return third
  }

  override fun fourth(): D {
    return fourth
  }

  override fun fifth(): E {
    return fifth
  }

  override fun sixth(): F {
    return sixth
  }

  override fun <R> mapFirst(mapper: (A) -> R): Sextuple<R, B, C, D, E, F> {
    return Tuple.of(mapper(first()), second(), third(), fourth(), fifth(), sixth())
  }

  override fun <R> mapSecond(mapper: (B) -> R): Sextuple<A, R, C, D, E, F> {
    return Tuple.of(first(), mapper(second()), third(), fourth(), fifth(), sixth())
  }

  override fun <R> mapThird(mapper: (C) -> R): Sextuple<A, B, R, D, E, F> {
    return Tuple.of(first(), second(), mapper(third()), fourth(), fifth(), sixth())
  }

  override fun <R> mapFourth(mapper: (D) -> R): Sextuple<A, B, C, R, E, F> {
    return Tuple.of(first(), second(), third(), mapper(fourth()), fifth(), sixth())
  }

  override fun <R> mapFifth(mapper: (E) -> R): Sextuple<A, B, C, D, R, F> {
    return Tuple.of(first(), second(), third(), fourth(), mapper(fifth()), sixth())
  }

  override fun <R> mapSixth(mapper: (F) -> R): Sextuple<A, B, C, D, E, R> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper(sixth()))
  }

  override fun dropFirst(): Quintuple<B, C, D, E, F> {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth())
  }

  override fun dropSecond(): Quintuple<A, C, D, E, F> {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth())
  }

  override fun dropThird(): Quintuple<A, B, D, E, F> {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth())
  }

  override fun dropFourth(): Quintuple<A, B, C, E, F> {
    return Tuple.of(first(), second(), third(), fifth(), sixth())
  }

  override fun dropFifth(): Quintuple<A, B, C, D, F> {
    return Tuple.of(first(), second(), third(), fourth(), sixth())
  }

  override fun dropSixth(): Quintuple<A, B, C, D, E> {
    return Tuple.of(first(), second(), third(), fourth(), fifth())
  }

  override fun <T> append(`object`: T): Septuple<A, B, C, D, E, F, T> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), `object`)
  }

  override fun <G, H> append(other: Couple<G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      other.first(),
      other.second()
    )
  }

  override fun <G, H, I> append(other: Triple<G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      other.first(),
      other.second(),
      other.third()
    )
  }

  override fun <G, H, I, J> append(other: Quadruple<G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth()
    )
  }
}