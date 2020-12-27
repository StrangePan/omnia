package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableTriple<A, B, C>(
    private val first: A,
    private val second: B,
    private val third: C) : Triple<A, B, C> {

    override fun equals(other: Any?): Boolean {
        return other is ImmutableTriple<*, *, *>
                && first() == other.first()
                && second() == other.second()
                && third() == other.third()
    }

    override fun hashCode(): Int {
        return Objects.hash(first(), second(), third())
    }

    override fun toString(): String {
        return "Tuple{" + first() + "," + second() + "," + third() + "}"
    }

    override fun first(): A {
        return Objects.requireNonNull(first)
    }

    override fun second(): B {
        return Objects.requireNonNull(second)
    }

    override fun third(): C {
        return Objects.requireNonNull(third)
    }

    override fun <R> mapFirst(mapper: Function<in A, out R>): Triple<R, B, C> {
        return Tuple.of(mapper.apply(first()), second(), third())
    }

    override fun <R> mapSecond(mapper: Function<in B, out R>): Triple<A, R, C> {
        return Tuple.of(first(), mapper.apply(second()), third())
    }

    override fun <R> mapThird(mapper: Function<in C, out R>): Triple<A, B, R> {
        return Tuple.of(first(), second(), mapper.apply(third()))
    }

    override fun dropFirst(): Couple<B, C> {
        return Tuple.of(second(), third())
    }

    override fun dropSecond(): Couple<A, C> {
        return Tuple.of(first(), third())
    }

    override fun dropThird(): Couple<A, B> {
        return Tuple.of(first(), second())
    }

    override fun <T> append(`object`: T): Quadruple<A, B, C, T> {
        return Tuple.of(first(), second(), third(), `object`)
    }

    override fun <D, E> append(other: Couple<D, E>): Quintuple<A, B, C, D, E> {
        return Tuple.of(first(), second(), third(), other.first(), other.second())
    }

    override fun <D, E, F> append(other: Triple<D, E, F>): Sextuple<A, B, C, D, E, F> {
        return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third())
    }

    override fun <D, E, F, G> append(other: Quadruple<D, E, F, G>): Septuple<A, B, C, D, E, F, G> {
        return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth())
    }

    override fun <D, E, F, G, H> append(other: Quintuple<D, E, F, G, H>): Octuple<A, B, C, D, E, F, G, H> {
        return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth())
    }

    override fun <D, E, F, G, H, I> append(other: Sextuple<D, E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I> {
        return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth())
    }

    override fun <D, E, F, G, H, I, J> append(other: Septuple<D, E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
        return Tuple.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh())
    }
}