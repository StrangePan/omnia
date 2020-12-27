package omnia.data.structure.immutable

import omnia.data.structure.List
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImmutableListTest {
  @Test
  fun getAt_whenFirst_didReturnFirst() {
      val underTest: List<String> = createTestSubject()
      Assert.assertSame(TEST_ITEM_1, underTest.itemAt(0))
    }

  @Test
  fun getAt_whenMiddle_didReturnMiddle() {
      val underTest: List<String> = createTestSubject()
      Assert.assertSame(TEST_ITEM_2, underTest.itemAt(1))
    }

  @Test
  fun getAt_whenLast_didReturnLast() {
      val underTest: List<String> = createTestSubject()
      Assert.assertSame(TEST_ITEM_3, underTest.itemAt(2))
    }

  @Test(expected = IndexOutOfBoundsException::class)
  fun getAt_whenEmpty_didThrowException() {
      ImmutableList.builder<Any>().build().itemAt(0)
    }

  @Test(expected = IndexOutOfBoundsException::class)
  fun getAt_negative_didThrowException() {
      createTestSubject().itemAt(-1)
    }

  @Test(expected = IndexOutOfBoundsException::class)
  fun getAt_tooHigh_didThrowException() {
      createTestSubject().itemAt(20)
    }

  @Test
  fun count_whenEmpty_didReturnCount() {
    Assert.assertEquals(0, ImmutableList.builder<Any>().build().count().toLong())
  }

  @Test
  fun count_didReturnCount() {
    Assert.assertEquals(3, createTestSubject().count().toLong())
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
    val iterator = createTestSubject().iterator()
    Assert.assertSame(TEST_ITEM_1, iterator.next())
    Assert.assertSame(TEST_ITEM_2, iterator.next())
    Assert.assertSame(TEST_ITEM_3, iterator.next())
    Assert.assertFalse(iterator.hasNext())
  }

  @Test
  fun stream_didStreamAll() {
    val stream = createTestSubject().stream()
    val contents = stream.toArray()
    Assert.assertEquals(3, contents.size.toLong())
    Assert.assertSame(TEST_ITEM_1, contents[0])
    Assert.assertSame(TEST_ITEM_2, contents[1])
    Assert.assertSame(TEST_ITEM_3, contents[2])
  }

  @Test
  fun equals_whenEmpty_didEqual() {
    Assert.assertEquals(ImmutableList.builder<Any>().build(), ImmutableList.builder<Any>().build())
  }

  @Test
  fun equals_whenEqual_areEqual() {
    Assert.assertEquals(createTestSubject(), createTestSubject())
  }

  @Test
  fun equals_whenUnequal_areNotEqual() {
    Assert.assertNotEquals(ImmutableList.builder<String>().add("hi there").build(), createTestSubject())
  }

  @Test
  fun hashCode_whenEqual_areEqual() {
    Assert.assertEquals(createTestSubject().hashCode().toLong(), createTestSubject().hashCode().toLong())
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