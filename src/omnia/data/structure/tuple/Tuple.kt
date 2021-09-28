package omnia.data.structure.tuple

import java.util.Objects
import omnia.contract.Countable
import omnia.data.structure.immutable.ImmutableList

interface Tuple : Countable {
  companion object {

    @JvmStatic
    fun <A, B> of(first: A, second: B): Couple<A, B> {
      return ImmutableCouple(first, second)
    }

    @JvmStatic
    fun <A, B, C> of(first: A, second: B, third: C): Triple<A, B, C> {
      return ImmutableTriple(first, second, third)
    }

    @JvmStatic
    fun <A, B, C, D> of(first: A, second: B, third: C, fourth: D): Quadruple<A, B, C, D> {
      return ImmutableQuadruple(first, second, third, fourth)
    }

    @JvmStatic
    fun <A, B, C, D, E> of(
        first: A,
        second: B,
        third: C,
        fourth: D,
        fifth: E
    ): Quintuple<A, B, C, D, E> {
      return ImmutableQuintuple(first, second, third, fourth, fifth)
    }

    @JvmStatic
    fun <A, B, C, D, E, F> of(
        first: A,
        second: B,
        third: C,
        fourth: D,
        fifth: E,
        sixth: F
    ): Sextuple<A, B, C, D, E, F> {
      return ImmutableSextuple(first, second, third, fourth, fifth, sixth)
    }

    @JvmStatic
    fun <A, B, C, D, E, F, G> of(
        first: A,
        second: B,
        third: C,
        fourth: D,
        fifth: E,
        sixth: F,
        seventh: G
    ): Septuple<A, B, C, D, E, F, G> {
      return ImmutableSeptuple(first, second, third, fourth, fifth, sixth, seventh)
    }

    @JvmStatic
    fun <A, B, C, D, E, F, G, H> of(
        first: A,
        second: B,
        third: C,
        fourth: D,
        fifth: E,
        sixth: F,
        seventh: G,
        eighth: H
    ): Octuple<A, B, C, D, E, F, G, H> {
      return ImmutableOctuple(first, second, third, fourth, fifth, sixth, seventh, eighth)
    }

    @JvmStatic
    fun <A, B, C, D, E, F, G, H, I> of(
        first: A,
        second: B,
        third: C,
        fourth: D,
        fifth: E,
        sixth: F,
        seventh: G,
        eighth: H,
        ninth: I
    ): Nonuple<A, B, C, D, E, F, G, H, I> {
      return ImmutableNonuple(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth)
    }

    @JvmStatic
    fun <A, B, C, D, E, F, G, H, I, J> of(
        first: A,
        second: B,
        third: C,
        fourth: D,
        fifth: E,
        sixth: F,
        seventh: G,
        eighth: H,
        ninth: I,
        tenth: J
    ): Decuple<A, B, C, D, E, F, G, H, I, J> {
      return ImmutableDecuple(
          first,
          second,
          third,
          fourth,
          fifth,
          sixth,
          seventh,
          eighth,
          ninth,
          tenth
      )
    }

    @JvmStatic
    fun toList(tuple: Tuple): ImmutableList<*> {
      Objects.requireNonNull(tuple)
      if (tuple is Couple<*, *>) {
        return toList(tuple)
      }
      if (tuple is Triple<*, *, *>) {
        return toList(tuple)
      }
      if (tuple is Quadruple<*, *, *, *>) {
        return toList(tuple)
      }
      if (tuple is Quintuple<*, *, *, *, *>) {
        return toList(tuple)
      }
      if (tuple is Sextuple<*, *, *, *, *, *>) {
        return toList(tuple)
      }
      if (tuple is Septuple<*, *, *, *, *, *, *>) {
        return toList(tuple)
      }
      if (tuple is Octuple<*, *, *, *, *, *, *, *>) {
        return toList(tuple)
      }
      if (tuple is Nonuple<*, *, *, *, *, *, *, *, *>) {
        return toList(tuple)
      }
      if (tuple is Decuple<*, *, *, *, *, *, *, *, *, *>) {
        return toList(tuple)
      }
      throw IllegalArgumentException("unrecognized tuple " + tuple.javaClass)
    }

    @JvmStatic
    fun <T : Any> toList(couple: Couple<out T, out T>): ImmutableList<T> {
      return ImmutableList.of(couple.first(), couple.second())
    }

    @JvmStatic
    fun <T : Any> toList(triple: Triple<out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(triple.first(), triple.second(), triple.third())
    }

    @JvmStatic
    fun <T : Any> toList(quadruple: Quadruple<out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
          quadruple.first(),
          quadruple.second(),
          quadruple.third(),
          quadruple.fourth()
      )
    }

    @JvmStatic
    fun <T : Any> toList(quintuple: Quintuple<out T, out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
          quintuple.first(),
          quintuple.second(),
          quintuple.third(),
          quintuple.fourth(),
          quintuple.fifth()
      )
    }

    @JvmStatic
    fun <T : Any> toList(sextuple: Sextuple<out T, out T, out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
          sextuple.first(),
          sextuple.second(),
          sextuple.third(),
          sextuple.fourth(),
          sextuple.fifth(),
          sextuple.sixth()
      )
    }

    @JvmStatic
    fun <T : Any> toList(septuple: Septuple<out T, out T, out T, out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
          septuple.first(),
          septuple.second(),
          septuple.third(),
          septuple.fourth(),
          septuple.fifth(),
          septuple.sixth(),
          septuple.seventh()
      )
    }

    @JvmStatic
    fun <T : Any> toList(octuple: Octuple<out T, out T, out T, out T, out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
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

    @JvmStatic
    fun <T : Any> toList(nonuple: Nonuple<out T, out T, out T, out T, out T, out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
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

    @JvmStatic
    fun <T : Any> toList(decuple: Decuple<out T, out T, out T, out T, out T, out T, out T, out T, out T, out T>): ImmutableList<T> {
      return ImmutableList.of(
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