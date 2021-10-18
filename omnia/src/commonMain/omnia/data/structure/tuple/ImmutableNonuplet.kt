package omnia.data.structure.tuple


import omnia.data.structure.immutable.ImmutableList

internal class ImmutableNonuplet<T : Any>(
  first: T,
  second: T,
  third: T,
  fourth: T,
  fifth: T,
  sixth: T,
  seventh: T,
  eighth: T,
  ninth: T
) : ImmutableNonuple<T, T, T, T, T, T, T, T, T>(
  first,
  second,
  third,
  fourth,
  fifth,
  sixth,
  seventh,
  eighth,
  ninth
), Nonuplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Nonuplet<R> {
    return Tuplet.of(
      mapper(first),
      mapper(second),
      mapper(third),
      mapper(fourth),
      mapper(fifth),
      mapper(sixth),
      mapper(seventh),
      mapper(eighth),
      mapper(ninth)
    )
  }

  override fun dropFirst(): Octuplet<T> {
    return Tuplet.of(second, third, fourth, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropSecond(): Octuplet<T> {
    return Tuplet.of(first, third, fourth, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropThird(): Octuplet<T> {
    return Tuplet.of(first, second, fourth, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropFourth(): Octuplet<T> {
    return Tuplet.of(first, second, third, fifth, sixth, seventh, eighth, ninth)
  }

  override fun dropFifth(): Octuplet<T> {
    return Tuplet.of(first, second, third, fourth, sixth, seventh, eighth, ninth)
  }

  override fun dropSixth(): Octuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, seventh, eighth, ninth)
  }

  override fun dropSeventh(): Octuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, eighth, ninth)
  }

  override fun dropEighth(): Octuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, seventh, ninth)
  }

  override fun dropNinth(): Octuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun concat(`object`: T): Decuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      `object`
    )
  }

  override fun iterator(): Iterator<T> {
    return toActualList().iterator()
  }

  override fun itemAt(index: Int): T {
    return toActualList().itemAt(index)
  }

  override fun indexOf(item: Any?): Int? {
    return toActualList().indexOf(item)
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return toActualList().containsUnknownTyped(item)
  }

  private fun toActualList(): ImmutableList<T> {
    return ImmutableList.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
    )
  }
}