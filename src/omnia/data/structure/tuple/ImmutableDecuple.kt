package omnia.data.structure.tuple

import java.util.Objects
import java.util.function.Function

internal open class ImmutableDecuple<A, B, C, D, E, F, G, H, I, J>(
    private val first: A,
    private val second: B,
    private val third: C,
    private val fourth: D,
    private val fifth: E,
    private val sixth: F,
    private val seventh: G,
    private val eighth: H,
    private val ninth: I,
    private val tenth: J) : Decuple<A, B, C, D, E, F, G, H, I, J> {

    override fun equals(other: Any?): Boolean {
        return (!(other !is ImmutableDecuple<*, *, *, *, *, *, *, *, *, *> || first() != other.first() || second() != other.second() || third() != other.third() || fourth() != other.fourth() || fifth() != other.fifth() || sixth() != other.sixth() || seventh() != other.seventh() || eighth() != other.eighth() || ninth() != other.ninth() || tenth() != other.tenth()))
    }

    override fun hashCode(): Int {
        return Objects.hash(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun toString(): String {
        return "Tuple{" + first() + "," + second() + "," + third() + "," + fourth() + "," + fifth() + "," + sixth() + "," + seventh() + "," + eighth() + "," + ninth() + "," + tenth() + "}"
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

    override fun tenth(): J {
        return tenth
    }

    override fun <R> mapFirst(mapper: Function<in A, out R>): Decuple<R, B, C, D, E, F, G, H, I, J> {
        return Tuple.of(mapper.apply(first()), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun <R> mapSecond(mapper: Function<in B, out R>): Decuple<A, R, C, D, E, F, G, H, I, J> {
        return Tuple.of(first(), mapper.apply(second()), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun <R> mapThird(mapper: Function<in C, out R>): Decuple<A, B, R, D, E, F, G, H, I, J> {
        return Tuple.of(first(), second(), mapper.apply(third()), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun <R> mapFourth(mapper: Function<in D, out R>): Decuple<A, B, C, R, E, F, G, H, I, J> {
        return Tuple.of(first(), second(), third(), mapper.apply(fourth()), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun <R> mapFifth(mapper: Function<in E, out R>): Decuple<A, B, C, D, R, F, G, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), mapper.apply(fifth()), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun <R> mapSixth(mapper: Function<in F, out R>): Decuple<A, B, C, D, E, R, G, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), mapper.apply(sixth()), seventh(), eighth(), ninth(), tenth())
    }

    override fun <R> mapSeventh(mapper: Function<in G, out R>): Decuple<A, B, C, D, E, F, R, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), mapper.apply(seventh()), eighth(), ninth(), tenth())
    }

    override fun <R> mapEighth(mapper: Function<in H, out R>): Decuple<A, B, C, D, E, F, G, R, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), mapper.apply(eighth()), ninth(), tenth())
    }

    override fun <R> mapNinth(mapper: Function<in I, out R>): Decuple<A, B, C, D, E, F, G, H, R, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), mapper.apply(ninth()), tenth())
    }

    override fun <R> mapTenth(mapper: Function<in J, out R>): Decuple<A, B, C, D, E, F, G, H, I, R> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), mapper.apply(tenth()))
    }

    override fun dropFirst(): Nonuple<B, C, D, E, F, G, H, I, J> {
        return Tuple.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun dropSecond(): Nonuple<A, C, D, E, F, G, H, I, J> {
        return Tuple.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun dropThird(): Nonuple<A, B, D, E, F, G, H, I, J> {
        return Tuple.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun dropFourth(): Nonuple<A, B, C, E, F, G, H, I, J> {
        return Tuple.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun dropFifth(): Nonuple<A, B, C, D, F, G, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun dropSixth(): Nonuple<A, B, C, D, E, G, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth(), ninth(), tenth())
    }

    override fun dropSeventh(): Nonuple<A, B, C, D, E, F, H, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth(), ninth(), tenth())
    }

    override fun dropEighth(): Nonuple<A, B, C, D, E, F, G, I, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), ninth(), tenth())
    }

    override fun dropNinth(): Nonuple<A, B, C, D, E, F, G, H, J> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), tenth())
    }

    override fun dropTenth(): Nonuple<A, B, C, D, E, F, G, H, I> {
        return Tuple.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth())
    }
}