package omnia.data.structure.mutable

import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.mutable.ArrayQueue.Companion.createWithInitialCapacity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ArrayQueueTest {
  @Test
  fun init_isEmpty() {
    val testSubject: Queue<*> = ArrayQueue.create<Any>()
    Assert.assertFalse(testSubject.isPopulated)
  }

  @Test
  fun init_countIsZero() {
    val testSubject: Queue<*> = ArrayQueue.create<Any>()
    Assert.assertEquals(0, testSubject.count().toLong())
  }

  @Test(expected = IllegalArgumentException::class)
  fun init_withCapacityZero_didThrowException() {
    createWithInitialCapacity<Any>( /* capacity= */0)
  }

  @Test
  fun enqueue_one_isPopulated() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    Assert.assertTrue(testSubject.isPopulated)
  }

  @Test
  fun enqueue_one_countIsOne() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    Assert.assertEquals(1, testSubject.count().toLong())
  }

  @Test(expected = NullPointerException::class)
  fun enqueue_null_didThrowException() {
    val testSubject: Queue<Any?> = ArrayQueue.create()
    testSubject.enqueue(null)
  }

  @Test
  fun enqueue_thenDequeue_didReturnObject() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    val result = testSubject.dequeue()
    Assert.assertTrue(result.isPresent)
    Assert.assertSame(item, result.get())
  }

  @Test
  fun enqueue_thenDequeue_isEmpty() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    testSubject.dequeue()
    Assert.assertFalse(testSubject.isPopulated)
  }

  @Test
  fun enqueue_thenDequeue_countIsZero() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    testSubject.dequeue()
    Assert.assertEquals(0, testSubject.count().toLong())
  }

  @Test
  fun dequeue_whenEmpty_didReturnEmpty() {
    Assert.assertFalse(ArrayQueue.create<Any>().dequeue().isPresent)
  }

  @Test
  fun enqueue_thenDequeueTwice_didReturnEmpty() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    testSubject.dequeue()
    Assert.assertFalse(testSubject.dequeue().isPresent)
  }

  @Test
  fun enqueue_fiveHundredTimes_isExpectedSize() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    Assert.assertEquals(500, testSubject.count().toLong())
  }

  @Test
  fun enqueue_fiveHundredTimes_thenDequeueFiveHundredTimes_didReturnExpectedItems() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    for (i in 0..499) {
      Assert.assertEquals(data.itemAt(i), testSubject.dequeue().get())
    }
  }

  @Test
  fun enqueue_fiveHundredTimes_thenDequeueFiveHundredTimes_isEmpty() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    for (i in 0..499) {
      testSubject.dequeue().get()
    }
    Assert.assertEquals(0, testSubject.count().toLong())
    Assert.assertFalse(testSubject.isPopulated)
  }

  @Test
  fun enqueue_thenDequeue_fiveHundredTimes_didReturnExpectedItems() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (i in 0..499) {
      val item = data.itemAt(i)
      testSubject.enqueue(item)
      Assert.assertEquals(item, testSubject.dequeue().get())
    }
  }

  @Test
  fun enqueue_thenDequeue_fiveHundredTimes_isEmpty() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (i in 0..499) {
      val item = data.itemAt(i)
      testSubject.enqueue(item)
      testSubject.dequeue()
    }
    Assert.assertEquals(0, testSubject.count().toLong())
    Assert.assertFalse(testSubject.isPopulated)
  }

  @Test
  fun peek_whenEmpty_isEmpty() {
    Assert.assertFalse(ArrayQueue.create<Any>().peek().isPresent)
  }

  @Test
  fun peek_withOne_didReturnObject() {
    val testSubject: Queue<Int> = ArrayQueue.create()
    testSubject.enqueue(132)
    val datum = testSubject.peek()
    Assert.assertTrue(datum.isPresent)
    Assert.assertEquals(Integer.valueOf(132), datum.get())
  }

  @Test
  fun peek_withOne_didNotDequeue() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    testSubject.peek()
    Assert.assertEquals(1, testSubject.count().toLong())
    Assert.assertTrue(testSubject.isPopulated)
  }

  @Test
  fun peekRepeatedly_withContent_didNotDequeue() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    for (i in 0..9) {
      testSubject.enqueue(Any())
    }
    for (i in 0..199) {
      testSubject.peek()
    }
    Assert.assertEquals(10, testSubject.count().toLong())
    Assert.assertTrue(testSubject.isPopulated)
  }

  @Test
  fun peekRepeatedly_withContent_didReturnFirst() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    // First item (which we'll compare with)
    val expected = Any()
    testSubject.enqueue(expected)
    // Additional arbitrary objects
    for (i in 0..9) {
      testSubject.enqueue(Any())
    }
    for (i in 0..199) {
      val result = testSubject.peek()
      Assert.assertTrue(result.isPresent)
      Assert.assertEquals(expected, result.get())
    }
  }

  companion object {
    private fun buildData(count: Int): List<Int> {
      require(count >= 0) { String.format("count must be positive (was %d)", count) }
      val data = ImmutableList.builder<Int>()
      for (i in 0 until count) {
        data.add(i)
      }
      return data.build()
    }
  }
}