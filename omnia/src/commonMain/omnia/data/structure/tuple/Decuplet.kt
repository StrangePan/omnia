package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtMostDecuplet

interface Decuplet<T : Any> : Decuple<T, T, T, T, T, T, T, T, T, T>, AtMostDecuplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Decuplet<R>
  override fun dropFirst(): Nonuplet<T>
  override fun dropSecond(): Nonuplet<T>
  override fun dropThird(): Nonuplet<T>
  override fun dropFourth(): Nonuplet<T>
  override fun dropFifth(): Nonuplet<T>
  override fun dropSixth(): Nonuplet<T>
  override fun dropSeventh(): Nonuplet<T>
  override fun dropEighth(): Nonuplet<T>
  override fun dropNinth(): Nonuplet<T>
  override fun dropTenth(): Nonuplet<T>

  companion object {

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
      return ImmutableDecuplet(
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
  }
}