package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtMostTriplet
import java.util.function.Function

interface Triplet<T> : Triple<T, T, T>, AtMostTriplet<T> {
    override fun <R> map(mapper: Function<in T, out R>): Triplet<R>
    override fun dropFirst(): Couplet<T>
    override fun dropSecond(): Couplet<T>
    override fun dropThird(): Couplet<T>
    override fun concat(`object`: T): Quadruplet<T>
    override fun concat(other: Couple<T, T>): Quintuplet<T>
    override fun concat(other: Triple<T, T, T>): Sextuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): Septuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): Octuplet<T>
    override fun concat(other: Sextuple<T, T, T, T, T, T>): Nonuplet<T>
    override fun concat(other: Septuple<T, T, T, T, T, T, T>): Decuplet<T>

    companion object {
        fun <T> of(first: T, second: T, third: T): Triplet<T> {
            return ImmutableTriplet(first, second, third)
        }
    }
}