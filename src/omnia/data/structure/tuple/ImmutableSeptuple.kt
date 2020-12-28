package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableSeptuple<A, B, C, D, E, F, G>(
    private val first: A,
    private val second: B,
    private val third: C,
    private val fourth: D,
    private val fifth: E,
    private val sixth: F,
    private val seventh: G,
) : Septuple<A, B, C, D, E, F, G> {

  override fun equals(other: Any?): Boolean {
    return (other is ImmutableSeptuple<*, *, *, *, *, *, *>
        && first() == other.first()
        && second() == other.second()
        && third() == other.third()
        && fourth() == other.fourth()
        && fifth() == other.fifth()
        && sixth() == other.sixth()
        && seventh() == other.seventh())
  }

  override fun hashCode(): Int {
    return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh())
  }

  override fun toString(): String {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "}"
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

  override fun seventh(): G {
    return seventh
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Septuple<R, B, C, D, E, F, G> {
    return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh())
  }

  override fun <R> mapSecond(mapper: Function<in B, out R>): Septuple<A, R, C, D, E, F, G> {
    return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh())
  }

  override fun <R> mapThird(mapper: Function<in C, out R>): Septuple<A, B, R, D, E, F, G> {
    return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh())
  }

  override fun <R> mapFourth(mapper: Function<in D, out R>): Septuple<A, B, C, R, E, F, G> {
    return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh())
  }

  override fun <R> mapFifth(mapper: Function<in E, out R>): Septuple<A, B, C, D, R, F, G> {
    return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh())
  }

  override fun <R> mapSixth(mapper: Function<in F, out R>): Septuple<A, B, C, D, E, R, G> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh())
  }

  override fun <R> mapSeventh(mapper: Function<in G, out R>): Septuple<A, B, C, D, E, F, R> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()))
  }

  override fun dropFirst(): Sextuple<B, C, D, E, F, G> {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh())
  }

  override fun dropSecond(): Sextuple<A, C, D, E, F, G> {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh())
  }

  override fun dropThird(): Sextuple<A, B, D, E, F, G> {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh())
  }

  override fun dropFourth(): Sextuple<A, B, C, E, F, G> {
    return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh())
  }

  override fun dropFifth(): Sextuple<A, B, C, D, F, G> {
    return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh())
  }

  override fun dropSixth(): Sextuple<A, B, C, D, E, G> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh())
  }

  override fun dropSeventh(): Sextuple<A, B, C, D, E, F> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth())
  }

  override fun <T> append(`object`: T): Octuple<A, B, C, D, E, F, G, T> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), `object`)
  }

  override fun <H, I> append(other: Couple<H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), other.first(), other.second())
  }

  override fun <H, I, J> append(other: Triple<H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), other.first(), other.second(), other.third())
  }
}