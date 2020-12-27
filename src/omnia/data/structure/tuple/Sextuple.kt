package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastSextuple
import omnia.data.structure.tuple.Tuples.AtMostSextuple
import java.util.function.Function

interface Sextuple<A, B, C, D, E, F> : AtLeastSextuple<A, B, C, D, E, F>, AtMostSextuple {
    override fun count(): Int {
        return 6
    }

    override fun <R> mapFirst(mapper: Function<in A, out R>): Sextuple<R, B, C, D, E, F>
    override fun <R> mapSecond(mapper: Function<in B, out R>): Sextuple<A, R, C, D, E, F>
    override fun <R> mapThird(mapper: Function<in C, out R>): Sextuple<A, B, R, D, E, F>
    override fun <R> mapFourth(mapper: Function<in D, out R>): Sextuple<A, B, C, R, E, F>
    override fun <R> mapFifth(mapper: Function<in E, out R>): Sextuple<A, B, C, D, R, F>
    override fun <R> mapSixth(mapper: Function<in F, out R>): Sextuple<A, B, C, D, E, R>
    override fun dropFirst(): Quintuple<B, C, D, E, F>
    override fun dropSecond(): Quintuple<A, C, D, E, F>
    override fun dropThird(): Quintuple<A, B, D, E, F>
    override fun dropFourth(): Quintuple<A, B, C, E, F>
    override fun dropFifth(): Quintuple<A, B, C, D, F>
    override fun dropSixth(): Quintuple<A, B, C, D, E>
    override fun <T> append(`object`: T): Septuple<A, B, C, D, E, F, T>
    override fun <G, H> append(other: Couple<G, H>): Octuple<A, B, C, D, E, F, G, H>
    override fun <G, H, I> append(other: Triple<G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I>
    override fun <G, H, I, J> append(other: Quadruple<G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}