package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableOctuple<A, B, C, D, E, F, G, H>(
    private val first: A,
    private val second: B,
    private val third: C,
    private val fourth: D,
    private val fifth: E,
    private val sixth: F,
    private val seventh: G,
    private val eighth: H) : Octuple<A, B, C, D, E, F, G, H> {

    override fun equals(other: Any?): Boolean {
        return other is ImmutableOctuple<*, *, *, *, *, *, *, *>
            && first() == other.first()
            && second() == other.second()
            && third() == other.third()
            && fourth() == other.fourth()
            && fifth() == other.fifth()
            && sixth() == other.sixth()
            && seventh() == other.seventh()
            && eighth() == other.eighth()
    }

    override fun hashCode(): Int {
        return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun toString(): String {
        return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "," + eighth() + "}"
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

    override fun <R> mapFirst(mapper: Function<in A, out R>): Octuple<R, B, C, D, E, F, G, H> {
        return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun <R> mapSecond(mapper: Function<in B, out R>): Octuple<A, R, C, D, E, F, G, H> {
        return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun <R> mapThird(mapper: Function<in C, out R>): Octuple<A, B, R, D, E, F, G, H> {
        return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun <R> mapFourth(mapper: Function<in D, out R>): Octuple<A, B, C, R, E, F, G, H> {
        return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh(), eighth())
    }

    override fun <R> mapFifth(mapper: Function<in E, out R>): Octuple<A, B, C, D, R, F, G, H> {
        return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh(), eighth())
    }

    override fun <R> mapSixth(mapper: Function<in F, out R>): Octuple<A, B, C, D, E, R, G, H> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh(), eighth())
    }

    override fun <R> mapSeventh(mapper: Function<in G, out R>): Octuple<A, B, C, D, E, F, R, H> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()), eighth())
    }

    override fun <R> mapEighth(mapper: Function<in H, out R>): Octuple<A, B, C, D, E, F, G, R> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), mapper.apply(eighth()))
    }

    override fun dropFirst(): Septuple<B, C, D, E, F, G, H> {
        return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun dropSecond(): Septuple<A, C, D, E, F, G, H> {
        return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun dropThird(): Septuple<A, B, D, E, F, G, H> {
        return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth())
    }

    override fun dropFourth(): Septuple<A, B, C, E, F, G, H> {
        return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth())
    }

    override fun dropFifth(): Septuple<A, B, C, D, F, G, H> {
        return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth())
    }

    override fun dropSixth(): Septuple<A, B, C, D, E, G, H> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth())
    }

    override fun dropSeventh(): Septuple<A, B, C, D, E, F, H> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth())
    }

    override fun dropEighth(): Septuple<A, B, C, D, E, F, G> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh())
    }

    override fun <T> append(`object`: T): Nonuple<A, B, C, D, E, F, G, H, T> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), `object`)
    }

    override fun <I, J> append(other: Couple<I, J>): Decuple<A, B, C, D, E, F, G, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), other.first(), other.second())
    }
}