package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableQuadruple<A, B, C, D>(
  private val first: A,
  private val second: B,
  private val third: C,
  private val fourth: D,
) : Quadruple<A, B, C, D> {

  override fun equals(other: Any?): Boolean {
    return other is ImmutableQuadruple<*, *, *, *>
        && first() == other.first()
        && second() == other.second()
        && third() == other.third()
        && fourth() == other.fourth()
  }

  override fun hashCode(): Int {
    return Objects.hash(first(), second(), third(), fourth())
  }

  override fun toString(): String {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "}"
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

  override fun <R> mapFirst(mapper: Function<in A, out R>): Quadruple<R, B, C, D> {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth())
  }

  override fun <R> mapSecond(mapper: Function<in B, out R>): Quadruple<A, R, C, D> {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth())
  }

  override fun <R> mapThird(mapper: Function<in C, out R>): Quadruple<A, B, R, D> {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth())
  }

  override fun <R> mapFourth(mapper: Function<in D, out R>): Quadruple<A, B, C, R> {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()))
  }

  override fun dropFirst(): Triple<B, C, D> {
    return Tuple.of(second(), third(), fourth())
  }

  override fun dropSecond(): Triple<A, C, D> {
    return Tuple.of(first(), third(), fourth())
  }

  override fun dropThird(): Triple<A, B, D> {
    return Tuple.of(first(), second(), fourth())
  }

  override fun dropFourth(): Triple<A, B, C> {
    return Tuple.of(first(), second(), third())
  }

  override fun <T> append(`object`: T): Quintuple<A, B, C, D, T> {
    return Tuple.of(first(), second(), third(), fourth(), `object`)
  }

  override fun <E, F> append(other: Couple<E, F>): Sextuple<A, B, C, D, E, F> {
    return Tuple.of(first(), second(), third(), fourth(), other.first(), other.second())
  }

  override fun <E, F, G> append(other: Triple<E, F, G>): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      other.first(),
      other.second(),
      other.third()
    )
  }

  override fun <E, F, G, H> append(other: Quadruple<E, F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth()
    )
  }

  override fun <E, F, G, H, I> append(other: Quintuple<E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth(),
      other.fifth()
    )
  }

  override fun <E, F, G, H, I, J> append(other: Sextuple<E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth(),
      other.fifth(),
      other.sixth()
    )
  }
}