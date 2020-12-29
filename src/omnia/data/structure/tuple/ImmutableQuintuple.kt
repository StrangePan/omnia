package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableQuintuple<A, B, C, D, E>(
  private val first: A,
  private val second: B,
  private val third: C,
  private val fourth: D,
  private val fifth: E,
) : Quintuple<A, B, C, D, E> {

  override fun equals(other: Any?): Boolean {
    return other is ImmutableQuintuple<*, *, *, *, *>
        && first() == other.first()
        && second() == other.second()
        && third() == other.third()
        && fourth() == other.fourth()
        && fifth() == other.fifth()
  }

  override fun hashCode(): Int {
    return Objects.hash(first(), second(), third(), fourth(), fifth())
  }

  override fun toString(): String {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "}"
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

  override fun <R> mapFirst(mapper: Function<in A, out R>): Quintuple<R, B, C, D, E> {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth())
  }

  override fun <R> mapSecond(mapper: Function<in B, out R>): Quintuple<A, R, C, D, E> {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth())
  }

  override fun <R> mapThird(mapper: Function<in C, out R>): Quintuple<A, B, R, D, E> {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth())
  }

  override fun <R> mapFourth(mapper: Function<in D, out R>): Quintuple<A, B, C, R, E> {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth())
  }

  override fun <R> mapFifth(mapper: Function<in E, out R>): Quintuple<A, B, C, D, R> {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()))
  }

  override fun dropFirst(): Quadruple<B, C, D, E> {
    return Tuple.of(second(), third(), fourth(), fifth())
  }

  override fun dropSecond(): Quadruple<A, C, D, E> {
    return Tuple.of(first(), third(), fourth(), fifth())
  }

  override fun dropThird(): Quadruple<A, B, D, E> {
    return Tuple.of(first(), second(), fourth(), fifth())
  }

  override fun dropFourth(): Quadruple<A, B, C, E> {
    return Tuple.of(first(), second(), third(), fifth())
  }

  override fun dropFifth(): Quadruple<A, B, C, D> {
    return Tuple.of(first(), second(), third(), fourth())
  }

  override fun <T> append(`object`: T): Sextuple<A, B, C, D, E, T> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), `object`)
  }

  override fun <F, G> append(other: Couple<F, G>): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second())
  }

  override fun <F, G, H> append(other: Triple<F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      other.first(),
      other.second(),
      other.third()
    )
  }

  override fun <F, G, H, I> append(other: Quadruple<F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth()
    )
  }

  override fun <F, G, H, I, J> append(other: Quintuple<F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth(),
      other.fifth()
    )
  }
}