package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableNonuple<A, B, C, D, E, F, G, H, I>(
  private val first: A,
  private val second: B,
  private val third: C,
  private val fourth: D,
  private val fifth: E,
  private val sixth: F,
  private val seventh: G,
  private val eighth: H,
  private val ninth: I,
) : Nonuple<A, B, C, D, E, F, G, H, I> {

  override fun equals(other: Any?): Boolean {
    return other is ImmutableNonuple<*, *, *, *, *, *, *, *, *>
        && first() == other.first()
        && second() == other.second()
        && third() == other.third()
        && fourth() == other.fourth()
        && fifth() == other.fifth()
        && sixth() == other.sixth()
        && seventh() == other.seventh()
        && eighth() == other.eighth()
        && ninth() == other.ninth()
  }

  override fun hashCode(): Int {
    return Objects.hash(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun toString(): String {
    return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "," + eighth() + "," + ninth() + "}"
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

  override fun eighth(): H {
    return eighth
  }

  override fun ninth(): I {
    return ninth
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Nonuple<R, B, C, D, E, F, G, H, I> {
    return Tuple.of(
      mapper.apply(first()),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapSecond(mapper: Function<in B, out R>): Nonuple<A, R, C, D, E, F, G, H, I> {
    return Tuple.of(
      first(),
      mapper.apply(second()),
      third(),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapThird(mapper: Function<in C, out R>): Nonuple<A, B, R, D, E, F, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      mapper.apply(third()),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapFourth(mapper: Function<in D, out R>): Nonuple<A, B, C, R, E, F, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      mapper.apply(fourth()),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapFifth(mapper: Function<in E, out R>): Nonuple<A, B, C, D, R, F, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      mapper.apply(fifth()),
      sixth(),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapSixth(mapper: Function<in F, out R>): Nonuple<A, B, C, D, E, R, G, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      mapper.apply(sixth()),
      seventh(),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapSeventh(mapper: Function<in G, out R>): Nonuple<A, B, C, D, E, F, R, H, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      mapper.apply(seventh()),
      eighth(),
      ninth()
    )
  }

  override fun <R> mapEighth(mapper: Function<in H, out R>): Nonuple<A, B, C, D, E, F, G, R, I> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      mapper.apply(eighth()),
      ninth()
    )
  }

  override fun <R> mapNinth(mapper: Function<in I, out R>): Nonuple<A, B, C, D, E, F, G, H, R> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      mapper.apply(ninth())
    )
  }

  override fun dropFirst(): Octuple<B, C, D, E, F, G, H, I> {
    return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth())
  }

  override fun dropSecond(): Octuple<A, C, D, E, F, G, H, I> {
    return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth())
  }

  override fun dropThird(): Octuple<A, B, D, E, F, G, H, I> {
    return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth())
  }

  override fun dropFourth(): Octuple<A, B, C, E, F, G, H, I> {
    return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth(), ninth())
  }

  override fun dropFifth(): Octuple<A, B, C, D, F, G, H, I> {
    return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth(), ninth())
  }

  override fun dropSixth(): Octuple<A, B, C, D, E, G, H, I> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth(), ninth())
  }

  override fun dropSeventh(): Octuple<A, B, C, D, E, F, H, I> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth(), ninth())
  }

  override fun dropEighth(): Octuple<A, B, C, D, E, F, G, I> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), ninth())
  }

  override fun dropNinth(): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
  }

  override fun <T> append(`object`: T): Decuple<A, B, C, D, E, F, G, H, I, T> {
    return Tuple.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      seventh(),
      eighth(),
      ninth(),
      `object`
    )
  }
}