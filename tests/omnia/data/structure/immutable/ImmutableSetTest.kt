package omnia.data.structure.immutable

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ImmutableSetTest {

  @Test
  fun count_whenEmpty_didReturnCount() {
    assertThat(ImmutableSet.builder<Any>().build().count().toLong()).isEqualTo(0)
  }

  @Test
  fun count_didReturnCount() {
    assertThat(createTestSubject().count().toLong()).isEqualTo(3)
  }

  @Test
  fun count_withDuplicates_didReturnCount_withoutDuplicates() {
    assertThat(createTestSubjectWithDuplicates().count().toLong()).isEqualTo(2)
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
    val iterator = createTestSubjectWithDuplicates().iterator()
    var count = 0
    var found1 = false
    var found2 = false
    while (iterator.hasNext()) {
      val item = iterator.next()
      found1 = found1 or (item === TEST_ITEM_1)
      found2 = found2 or (item === TEST_ITEM_2)
      count++
    }
    assertThat(count.toLong()).isEqualTo(2)
    assertThat(found1).isTrue()
    assertThat(found2).isTrue()
  }

  @Test
  fun stream_didStreamAll() {
    val stream = createTestSubjectWithDuplicates().stream()
    val contents = stream.toArray()
    var found1 = false
    var found2 = false
    for (item in contents) {
      found1 = found1 or (item === TEST_ITEM_1)
      found2 = found2 or (item === TEST_ITEM_2)
    }
    assertThat(contents.size.toLong()).isEqualTo(2)
    assertThat(found1).isTrue()
    assertThat(found2).isTrue()
  }

  companion object {

    private const val TEST_ITEM_1 = "first"
    private const val TEST_ITEM_2 = "second"
    private const val TEST_ITEM_3 = "third"
    private fun createTestSubject(): ImmutableSet<String> {
      return ImmutableSet.builder<String>()
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_2)
        .add(TEST_ITEM_3)
        .build()
    }

    private fun createTestSubjectWithDuplicates(): ImmutableSet<String> {
      return ImmutableSet.builder<String>()
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_1)
        .add(TEST_ITEM_2)
        .add(TEST_ITEM_2)
        .build()
    }
  }
}