package omnia.data.structure.immutable

import kotlin.test.Test
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isNull
import omnia.util.test.fluent.isTrue

class ImmutableListSetTest {

  @Test
  fun empty_isEmpty() {
    val underTest = ImmutableListSet.empty<Any>()
    assertThat(underTest.count).isEqualTo(0)
    assertThat(underTest.isPopulated).isFalse()
    assertThat(underTest.indexOf(null)).isNull()
    assertThat(underTest.indexOf(Any())).isNull()
    assertThat(underTest.indexOf(1)).isNull()
  }

  @Test
  fun withDuplicates_removesDuplicates() {
    val expectedValues = ImmutableList.of(0, 1, 2, 3, 5, 7, 6)
    val underTest = ImmutableListSet.of(0, 1, 2, 3, 3, 3, 0, 2, 1, 5, 7, 6, 3, 6)

    assertThat(underTest.count).isEqualTo(7)
    assertThat(underTest.itemAt(0)).isEqualTo(0)
    assertThat(underTest.itemAt(1)).isEqualTo(1)
    assertThat(underTest.itemAt(2)).isEqualTo(2)
    assertThat(underTest.itemAt(3)).isEqualTo(3)
    assertThat(underTest.itemAt(4)).isEqualTo(5)
    assertThat(underTest.itemAt(5)).isEqualTo(7)
    assertThat(underTest.itemAt(6)).isEqualTo(6)

    assertThat { underTest.itemAt(7) }.failsWith(IndexOutOfBoundsException::class)
    assertThat(underTest.isPopulated).isTrue()

    assertThat(underTest.toImmutableList()).isEqualTo(expectedValues)
    assertThat(underTest.toImmutableSet()).isEqualTo(expectedValues.toImmutableSet())
    assertThat(underTest).isEqualTo(ImmutableListSet.of(0, 0, 1, 0, 2, 1, 2, 3, 5, 0, 5, 7, 6))
  }
}
