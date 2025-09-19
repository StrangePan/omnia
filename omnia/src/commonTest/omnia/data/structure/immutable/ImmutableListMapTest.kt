package omnia.data.structure.immutable

import kotlin.test.Test
import omnia.data.structure.Map
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.tuple.Tuple
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isNull
import omnia.util.test.fluent.isTrue

class ImmutableListMapTest {

  @Test
  fun empty_isEmpty() {
    val underTest = ImmutableListMap.empty<Any, Any>()
    assertThat(underTest.count).isEqualTo(0)
    assertThat(underTest.isPopulated).isFalse()
    assertThat(underTest.indexOf(null)).isNull()
    assertThat(underTest.indexOf(Any())).isNull()
    assertThat(underTest.indexOf(1)).isNull()
    assertThat(underTest.entries).isEmpty().hasCount(0)
    assertThat(underTest.keys).isEmpty().hasCount(0)
    assertThat(underTest.values).isEmpty().hasCount(0)
  }

  @Test
  fun withDuplicates_removesDuplicates() {
    val expectedValues =
      ImmutableList.of(
        Tuple.of(0, "zero"),
        Tuple.of(1, "one"),
        Tuple.of(2, "two"),
        Tuple.of(3, "three"),
        Tuple.of(5, "five"),
        Tuple.of(7, "seven"),
        Tuple.of(6, "six"))
    val underTest =
      ImmutableListMap.builder<Int, String>()
        .putMapping(0, "zero")
        .putMapping(1, "one")
        .putMapping(2, "two")
        .putMapping(3, "three")
        .putMapping(3, "-e")
        .putMapping(0, "15")
        .putMapping(2, "    ")
        .putMapping(1, "-15t2")
        .putMapping(5, "five")
        .putMapping(7, "seven")
        .putMapping(6, "six")
        .putMapping(3, "cbc vbfvx b")
        .putMapping(6, "nm cvcxbnmnb")
        .build()

    assertThat(underTest.isPopulated).isTrue()
    assertThat(underTest.count).isEqualTo(7)

    val key: (Map.Entry<Int, String>) -> Int = Map.Entry<Int, String>::key
    val value: (Map.Entry<Int, String>) -> String = Map.Entry<Int, String>::value

    assertThat(underTest.itemAt(0)).andThat(key) { it.isEqualTo(0) }.andThat(value) { it.isEqualTo("zero") }
    assertThat(underTest.itemAt(1)).andThat(key) { it.isEqualTo(1) }.andThat(value) { it.isEqualTo("one") }
    assertThat(underTest.itemAt(2)).andThat(key) { it.isEqualTo(2) }.andThat(value) { it.isEqualTo("two") }
    assertThat(underTest.itemAt(3)).andThat(key) { it.isEqualTo(3) }.andThat(value) { it.isEqualTo("three") }
    assertThat(underTest.itemAt(4)).andThat(key) { it.isEqualTo(5) }.andThat(value) { it.isEqualTo("five") }
    assertThat(underTest.itemAt(5)).andThat(key) { it.isEqualTo(7) }.andThat(value) { it.isEqualTo("seven") }
    assertThat(underTest.itemAt(6)).andThat(key) { it.isEqualTo(6) }.andThat(value) { it.isEqualTo("six") }
    assertThat { underTest.itemAt(7) }.failsWith(IndexOutOfBoundsException::class)


    assertThat(underTest.map { Tuple.of(it.key, it.value) }.toImmutableList()).isEqualTo(expectedValues)
    assertThat(underTest.map { Tuple.of(it.key, it.value) }.toImmutableSet()).isEqualTo(expectedValues.toImmutableSet())
    assertThat(underTest.keys.toImmutableList())
      .isEqualTo(ImmutableListSet.of(0, 0, 1, 0, 2, 1, 2, 3, 5, 0, 5, 7, 6).toImmutableList())
      .isEqualTo(expectedValues.map { it.first }.toImmutableList())

    assertThat(underTest.values.toImmutableList())
      .isEqualTo(expectedValues.map { it.second }.toImmutableList())

    assertThat(underTest.valueOf(0)).isEqualTo("zero")
    assertThat(underTest.valueOf(1)).isEqualTo("one")
    assertThat(underTest.valueOf(2)).isEqualTo("two")
    assertThat(underTest.valueOf(3)).isEqualTo("three")
    assertThat(underTest.valueOf(5)).isEqualTo("five")
    assertThat(underTest.valueOf(7)).isEqualTo("seven")
    assertThat(underTest.valueOf(6)).isEqualTo("six")
  }
}
