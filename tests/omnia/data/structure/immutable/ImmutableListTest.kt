package omnia.data.structure.immutable

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.data.structure.List

class ImmutableListTest {

  @Test
  fun getAt_whenFirst_didReturnFirst() {
    val underTest: List<String> = createTestSubject()
    assertThat(underTest.itemAt(0)).isSameInstanceAs(TEST_ITEM_1)
  }

  @Test
  fun getAt_whenMiddle_didReturnMiddle() {
    val underTest: List<String> = createTestSubject()
    assertThat(underTest.itemAt(1)).isSameInstanceAs(TEST_ITEM_2)
  }

  @Test
  fun getAt_whenLast_didReturnLast() {
    val underTest: List<String> = createTestSubject()
    assertThat(underTest.itemAt(2)).isSameInstanceAs(TEST_ITEM_3)
  }

  @Test
  fun getAt_whenEmpty_didThrowException() {
    assertFailsWith(IndexOutOfBoundsException::class) {
      ImmutableList.builder<Any>().build().itemAt(0)
    }
  }

  @Test
  fun getAt_negative_didThrowException() {
    assertFailsWith(IndexOutOfBoundsException::class) { createTestSubject().itemAt(-1) }
  }

  @Test
  fun getAt_tooHigh_didThrowException() {
    assertFailsWith(IndexOutOfBoundsException::class) { createTestSubject().itemAt(20) }
  }

  @Test
  fun count_whenEmpty_didReturnCount() {
    assertThat(ImmutableList.builder<Any>().build().count().toLong()).isEqualTo(0)
  }

  @Test
  fun count_didReturnCount() {
    assertThat(createTestSubject().count().toLong()).isEqualTo(3)
  }

  @Test
  fun contains_whenFirst_didReturnTrue() {
    assertThat(createTestSubject().contains(TEST_ITEM_1)).isTrue()
  }

  @Test
  fun contains_whenMiddle_didReturnTrue() {
    assertThat(createTestSubject().contains(TEST_ITEM_2)).isTrue()
  }

  @Test
  fun contains_whenLast_didReturnTrue() {
    assertThat(createTestSubject().contains(TEST_ITEM_3)).isTrue()
  }

  @Test
  fun contains_whenUnknown_didReturnFalse() {
    assertThat(createTestSubject().contains("unknown")).isFalse()
  }

  @Test
  fun iterate_didIterateOverAll() {
    val iterator = createTestSubject().iterator()
    assertThat(iterator.next()).isSameInstanceAs(TEST_ITEM_1)
    assertThat(iterator.next()).isSameInstanceAs(TEST_ITEM_2)
    assertThat(iterator.next()).isSameInstanceAs(TEST_ITEM_3)
    assertThat(iterator.hasNext()).isFalse()
  }

  @Test
  fun stream_didStreamAll() {
    val stream = createTestSubject().stream()
    val contents = stream.toArray()
    assertThat(contents.size.toLong()).isEqualTo(3)
    assertThat(contents[0]).isSameInstanceAs(TEST_ITEM_1)
    assertThat(contents[1]).isSameInstanceAs(TEST_ITEM_2)
    assertThat(contents[2]).isSameInstanceAs(TEST_ITEM_3)
  }

  @Test
  fun equals_whenEmpty_didEqual() {
    assertThat(ImmutableList.builder<Any>().build()).isEqualTo(ImmutableList.builder<Any>().build())
  }

  @Test
  fun equals_whenEqual_areEqual() {
    assertThat(createTestSubject()).isEqualTo(createTestSubject())
  }

  @Test
  fun equals_whenUnequal_areNotEqual() {
    assertThat(createTestSubject())
        .isNotEqualTo(ImmutableList.builder<String>().add("hi there").build())
  }

  @Test
  fun hashCode_whenEqual_areEqual() {
    assertThat(createTestSubject().hashCode().toLong())
        .isEqualTo(createTestSubject().hashCode().toLong())
  }

  companion object {

    private const val TEST_ITEM_1 = "first"
    private const val TEST_ITEM_2 = "second"
    private const val TEST_ITEM_3 = "third"
    private fun createTestSubject(): ImmutableList<String> {
      return ImmutableList.builder<String>()
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_2)
        .add(TEST_ITEM_3)
        .build()
    }
  }
}