package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableCouple<A, B>(private val first: A, private val second: B) :
    Couple<A, B> {

  override fun equals(other: Any?): Boolean {
    return other is ImmutableCouple<*, *>
        && first() == other.first()
        && second() == other.second()
  }

  override fun hashCode(): Int {
    return Objects.hash(first(), second())
  }

  override fun toString(): String {
    return "Tuple{" + first() + "," + second() + "}"
  }

  override fun first(): A {
    return first
  }

  override fun second(): B {
    return second
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Couple<R, B> {
    return Tuple.of(mapper.apply(first()), second())
  }

  override fun <R> mapSecond(mapper: Function<in B, out R>): Couple<A, R> {
    return Tuple.of(first(), mapper.apply(second()))
  }

  override fun <T> append(`object`: T): Triple<A, B, T> {
    return Tuple.of(first(), second(), `object`)
  }

  override fun <C, D> append(other: Couple<C, D>): Quadruple<A, B, C, D> {
    return Tuple.of(first(), second(), other.first(), other.second())
  }

  override fun <C, D, E> append(other: Triple<C, D, E>): Quintuple<A, B, C, D, E> {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third())
  }

  override fun <C, D, E, F> append(other: Quadruple<C, D, E, F>): Sextuple<A, B, C, D, E, F> {
    return Tuple.of(first(), second(), other.first(), other.second(), other.third(), other.fourth())
  }

  override fun <C, D, E, F, G> append(other: Quintuple<C, D, E, F, G>): Septuple<A, B, C, D, E, F, G> {
    return Tuple.of(
        first(),
        second(),
        other.first(),
        other.second(),
        other.third(),
        other.fourth(),
        other.fifth()
    )
  }

  override fun <C, D, E, F, G, H> append(other: Sextuple<C, D, E, F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
    return Tuple.of(
        first(),
        second(),
        other.first(),
        other.second(),
        other.third(),
        other.fourth(),
        other.fifth(),
        other.sixth()
    )
  }

  override fun <C, D, E, F, G, H, I> append(other: Septuple<C, D, E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
    return Tuple.of(
        first(),
        second(),
        other.first(),
        other.second(),
        other.third(),
        other.fourth(),
        other.fifth(),
        other.sixth(),
        other.seventh()
    )
  }

  override fun <C, D, E, F, G, H, I, J> append(other: Octuple<C, D, E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
    return Tuple.of(
        first(),
        second(),
        other.first(),
        other.second(),
        other.third(),
        other.fourth(),
        other.fifth(),
        other.sixth(),
        other.seventh(),
        other.eighth()
    )
  }
}