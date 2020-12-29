package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.List

interface Tuplet<T> : Tuple, List<T> {

  fun <R> map(mapper: Function<in T, out R>): Tuplet<R>

  companion object {

    fun <T> of(first: T, second: T): Couplet<T> {
      return Couplet.of(first, second)
    }

    fun <T> of(first: T, second: T, third: T): Triplet<T> {
      return Triplet.of(first, second, third)
    }

    fun <T> of(first: T, second: T, third: T, fourth: T): Quadruplet<T> {
      return Quadruplet.of(first, second, third, fourth)
    }

    fun <T> of(first: T, second: T, third: T, fourth: T, fifth: T): Quintuplet<T> {
      return Quintuplet.of(first, second, third, fourth, fifth)
    }

    fun <T> of(first: T, second: T, third: T, fourth: T, fifth: T, sixth: T): Sextuplet<T> {
      return Sextuplet.of(first, second, third, fourth, fifth, sixth)
    }

    fun <T> of(
      first: T,
      second: T,
      third: T,
      fourth: T,
      fifth: T,
      sixth: T,
      seventh: T
    ): Septuplet<T> {
      return Septuplet.of(first, second, third, fourth, fifth, sixth, seventh)
    }

    fun <T> of(
      first: T,
      second: T,
      third: T,
      fourth: T,
      fifth: T,
      sixth: T,
      seventh: T,
      eighth: T
    ): Octuplet<T> {
      return Octuplet.of(first, second, third, fourth, fifth, sixth, seventh, eighth)
    }

    fun <T> of(
      first: T,
      second: T,
      third: T,
      fourth: T,
      fifth: T,
      sixth: T,
      seventh: T,
      eighth: T,
      ninth: T
    ): Nonuplet<T> {
      return Nonuplet.of(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth)
    }

    fun <T> of(
      first: T,
      second: T,
      third: T,
      fourth: T,
      fifth: T,
      sixth: T,
      seventh: T,
      eighth: T,
      ninth: T,
      tenth: T
    ): Decuplet<T> {
      return Decuplet.of(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth)
    }

    fun <T> copyOf(couplet: Couple<out T, out T>): Couplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (couplet is ImmutableCouplet<*>) couplet as ImmutableCouplet<T> else of(
        couplet.first(),
        couplet.second()
      )
    }

    fun <T> copyOf(triple: Triple<out T, out T, out T>): Triplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (triple is ImmutableTriplet<*>) triple as ImmutableTriplet<T> else of(
        triple.first(),
        triple.second(),
        triple.third()
      )
    }

    fun <T> copyOf(quadruple: Quadruple<out T, out T, out T, out T>): Quadruplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (quadruple is ImmutableQuadruplet<*>) quadruple as ImmutableQuadruplet<T> else of(
        quadruple.first(),
        quadruple.second(),
        quadruple.third(),
        quadruple.fourth()
      )
    }

    fun <T> copyOf(quintuple: Quintuple<out T, out T, out T, out T, out T>): Quintuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (quintuple is ImmutableQuintuplet<*>) quintuple as ImmutableQuintuplet<T> else of(
        quintuple.first(),
        quintuple.second(),
        quintuple.third(),
        quintuple.fourth(),
        quintuple.fifth()
      )
    }

    fun <T> copyOf(sextuple: Sextuple<out T, out T, out T, out T, out T, out T>): Sextuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (sextuple is ImmutableSextuplet<*>) sextuple as ImmutableSextuplet<T> else of(
        sextuple.first(),
        sextuple.second(),
        sextuple.third(),
        sextuple.fourth(),
        sextuple.fifth(),
        sextuple.sixth()
      )
    }

    fun <T> copyOf(septuple: Septuple<out T, out T, out T, out T, out T, out T, out T>): Septuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (septuple is ImmutableSeptuplet<*>) septuple as ImmutableSeptuplet<T> else of(
        septuple.first(),
        septuple.second(),
        septuple.third(),
        septuple.fourth(),
        septuple.fifth(),
        septuple.sixth(),
        septuple.seventh()
      )
    }

    fun <T> copyOf(octuple: Octuple<out T, out T, out T, out T, out T, out T, out T, out T>): Octuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (octuple is ImmutableOctuplet<*>) octuple as ImmutableOctuplet<T> else of(
        octuple.first(),
        octuple.second(),
        octuple.third(),
        octuple.fourth(),
        octuple.fifth(),
        octuple.sixth(),
        octuple.seventh(),
        octuple.eighth()
      )
    }

    fun <T> copyOf(nonuple: Nonuple<out T, out T, out T, out T, out T, out T, out T, out T, out T>): Nonuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (nonuple is ImmutableNonuplet<*>) nonuple as ImmutableNonuplet<T> else of(
        nonuple.first(),
        nonuple.second(),
        nonuple.third(),
        nonuple.fourth(),
        nonuple.fifth(),
        nonuple.sixth(),
        nonuple.seventh(),
        nonuple.eighth(),
        nonuple.ninth()
      )
    }

    fun <T> copyOf(decuple: Decuple<out T, out T, out T, out T, out T, out T, out T, out T, out T, out T>): Decuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (decuple is ImmutableDecuplet<*>) decuple as ImmutableDecuplet<T> else of(
        decuple.first(),
        decuple.second(),
        decuple.third(),
        decuple.fourth(),
        decuple.fifth(),
        decuple.sixth(),
        decuple.seventh(),
        decuple.eighth(),
        decuple.ninth(),
        decuple.tenth()
      )
    }
  }
}