package omnia.data.structure.tuple

import omnia.data.structure.List

interface Tuplet<T : Any> : Tuple, List<T> {

  fun <R : Any> map(mapper: (T) -> R): Tuplet<R>

  companion object {

    fun <T : Any> of(first: T, second: T): Couplet<T> {
      return Couplet(first, second)
    }

    fun <T : Any> of(first: T, second: T, third: T): Triplet<T> {
      return Triplet(first, second, third)
    }

    fun <T : Any> of(first: T, second: T, third: T, fourth: T): Quadruplet<T> {
      return Quadruplet(first, second, third, fourth)
    }

    fun <T : Any> of(first: T, second: T, third: T, fourth: T, fifth: T): Quintuplet<T> {
      return Quintuplet(first, second, third, fourth, fifth)
    }

    fun <T : Any> of(first: T, second: T, third: T, fourth: T, fifth: T, sixth: T): Sextuplet<T> {
      return Sextuplet(first, second, third, fourth, fifth, sixth)
    }

    fun <T : Any> of(
        first: T,
        second: T,
        third: T,
        fourth: T,
        fifth: T,
        sixth: T,
        seventh: T
    ): Septuplet<T> {
      return Septuplet(first, second, third, fourth, fifth, sixth, seventh)
    }

    fun <T : Any> of(
        first: T,
        second: T,
        third: T,
        fourth: T,
        fifth: T,
        sixth: T,
        seventh: T,
        eighth: T
    ): Octuplet<T> {
      return Octuplet(first, second, third, fourth, fifth, sixth, seventh, eighth)
    }

    fun <T : Any> of(
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
      return Nonuplet(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth)
    }

    fun <T : Any> of(
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
      return Decuplet(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth)
    }

    fun <T : Any> copyOf(couplet: Couple<out T, out T>): Couplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (couplet is Couplet<*>) couplet as Couplet<T> else of(
          couplet.first,
          couplet.second
      )
    }

    fun <T : Any> copyOf(triple: Triple<out T, out T, out T>): Triplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (triple is Triplet<*>) triple as Triplet<T> else of(
          triple.first,
          triple.second,
          triple.third
      )
    }

    fun <T : Any> copyOf(quadruple: Quadruple<out T, out T, out T, out T>): Quadruplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (quadruple is Quadruplet<*>) quadruple as Quadruplet<T> else of(
          quadruple.first,
          quadruple.second,
          quadruple.third,
          quadruple.fourth
      )
    }

    fun <T : Any> copyOf(quintuple: Quintuple<out T, out T, out T, out T, out T>): Quintuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (quintuple is Quintuplet<*>) quintuple as Quintuplet<T> else of(
          quintuple.first,
          quintuple.second,
          quintuple.third,
          quintuple.fourth,
          quintuple.fifth
      )
    }

    fun <T : Any> copyOf(sextuple: Sextuple<out T, out T, out T, out T, out T, out T>): Sextuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (sextuple is Sextuplet<*>) sextuple as Sextuplet<T> else of(
          sextuple.first,
          sextuple.second,
          sextuple.third,
          sextuple.fourth,
          sextuple.fifth,
          sextuple.sixth
      )
    }

    fun <T : Any> copyOf(septuple: Septuple<out T, out T, out T, out T, out T, out T, out T>): Septuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (septuple is Septuplet<*>) septuple as Septuplet<T> else of(
          septuple.first,
          septuple.second,
          septuple.third,
          septuple.fourth,
          septuple.fifth,
          septuple.sixth,
          septuple.seventh
      )
    }

    fun <T : Any> copyOf(octuple: Octuple<out T, out T, out T, out T, out T, out T, out T, out T>): Octuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (octuple is Octuplet<*>) octuple as Octuplet<T> else of(
          octuple.first,
          octuple.second,
          octuple.third,
          octuple.fourth,
          octuple.fifth,
          octuple.sixth,
          octuple.seventh,
          octuple.eighth
      )
    }

    fun <T : Any> copyOf(nonuple: Nonuple<out T, out T, out T, out T, out T, out T, out T, out T, out T>): Nonuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (nonuple is Nonuplet<*>) nonuple as Nonuplet<T> else of(
          nonuple.first,
          nonuple.second,
          nonuple.third,
          nonuple.fourth,
          nonuple.fifth,
          nonuple.sixth,
          nonuple.seventh,
          nonuple.eighth,
          nonuple.ninth
      )
    }

    fun <T : Any> copyOf(decuple: Decuple<out T, out T, out T, out T, out T, out T, out T, out T, out T, out T>): Decuplet<T> {
      @Suppress("UNCHECKED_CAST")
      return if (decuple is Decuplet<*>) decuple as Decuplet<T> else of(
          decuple.first,
          decuple.second,
          decuple.third,
          decuple.fourth,
          decuple.fifth,
          decuple.sixth,
          decuple.seventh,
          decuple.eighth,
          decuple.ninth,
          decuple.tenth
      )
    }
  }
}