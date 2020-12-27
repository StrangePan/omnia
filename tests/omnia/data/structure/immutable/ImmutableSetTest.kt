package omnia.data.structure.immutable

import org.junit.Assert
import org.junit.Test

class ImmutableSetTest {
  @Test
  fun count_whenEmpty_didReturnCount() {
    Assert.assertEquals(0, ImmutableSet.builder<Any>().build().count().toLong())
  }

  @Test
  fun count_didReturnCount() {
    Assert.assertEquals(3, createTestSubject().count().toLong())
  }

  @Test
  fun count_withDuplicates_didReturnCount_withoutDuplicates() {
    Assert.assertEquals(2, createTestSubjectWithDuplicates().count().toLong())
  }

  @Test
  fun contains_whenFirst_didReturnTrue() {
    Assert.assertTrue(createTestSubject().contains(TEST_ITEM_1))
  }

  @Test
  fun contains_whenMiddle_didReturnTrue() {
    Assert.assertTrue(createTestSubject().contains(TEST_ITEM_2))
  }

  @Test
  fun contains_whenLast_didReturnTrue() {
    Assert.assertTrue(createTestSubject().contains(TEST_ITEM_3))
  }

  @Test
  fun contains_whenUnknown_didReturnFalse() {
    Assert.assertFalse(createTestSubject().contains("unknown"))
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
    Assert.assertEquals(2, count.toLong())
    Assert.assertTrue(found1)
    Assert.assertTrue(found2)
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
    Assert.assertEquals(2, contents.size.toLong())
    Assert.assertTrue(found1)
    Assert.assertTrue(found2)
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