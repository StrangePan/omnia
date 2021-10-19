package omnia.data.structure.mutable

import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.mutable.ArrayQueue.Companion.createWithInitialCapacity
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isNull
import omnia.util.test.fluent.isSameAs
import omnia.util.test.fluent.isTrue

class ArrayQueueTest {

  @Test
  fun init_isEmpty() {
    val testSubject: Queue<*> = ArrayQueue.create<Any>()
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun init_countIsZero() {
    val testSubject: Queue<*> = ArrayQueue.create<Any>()
    assertThat(testSubject).hasCount(0)
  }

  @Test
  fun init_withCapacityZero_didThrowException() {
    assertFailsWith(IllegalArgumentException::class) {
      createWithInitialCapacity<Any>(capacity = 0)
    }
  }

  @Test
  fun enqueue_one_isPopulated() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    assertThat(testSubject.isPopulated).isTrue()
  }

  @Test
  fun enqueue_one_countIsOne() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    assertThat(testSubject).hasCount(1)
  }

  @Test
  fun enqueue_thenDequeue_didReturnObject() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    val result = testSubject.dequeue()
    assertThat(result).isSameAs(item)
  }

  @Test
  fun enqueue_thenDequeue_isEmpty() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    testSubject.dequeue()
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun enqueue_thenDequeue_countIsZero() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    testSubject.dequeue()
    assertThat(testSubject).hasCount(0)
  }

  @Test
  fun dequeue_whenEmpty_didReturnEmpty() {
    assertThat(ArrayQueue.create<Any>().dequeue()).isNull()
  }

  @Test
  fun enqueue_thenDequeueTwice_didReturnEmpty() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    testSubject.dequeue()
    assertThat(testSubject.dequeue()).isNull()
  }

  @Test
  fun enqueue_fiveHundredTimes_isExpectedSize() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    assertThat(testSubject).hasCount(500)
  }

  @Test
  fun enqueue_fiveHundredTimes_thenDequeueFiveHundredTimes_didReturnExpectedItems() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    for (i in 0..499) {
      assertThat(testSubject.dequeue()).isEqualTo(data.itemAt(i))
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
      testSubject.dequeue()
    }
    assertThat(testSubject).hasCount(0)
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun enqueue_thenDequeue_fiveHundredTimes_didReturnExpectedItems() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (i in 0..499) {
      val item = data.itemAt(i)
      testSubject.enqueue(item)
      assertThat(testSubject.dequeue()).isEqualTo(item)
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
    assertThat(testSubject).hasCount(0)
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun peek_whenEmpty_isEmpty() {
    assertThat(ArrayQueue.create<Any>().peek()).isNull()
  }

  @Test
  fun peek_withOne_didReturnObject() {
    val testSubject: Queue<Int> = ArrayQueue.create()
    testSubject.enqueue(132)
    val datum = testSubject.peek()
    assertThat(datum).isEqualTo(132)
  }

  @Test
  fun peek_withOne_didNotDequeue() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    testSubject.peek()
    assertThat(testSubject).hasCount(1)
    assertThat(testSubject.isPopulated).isTrue()
  }

  @Test
  fun peekRepeatedly_withContent_didNotDequeue() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    (0..9).forEach { _ ->
      testSubject.enqueue(Any())
    }
    (0..199).forEach { _ ->
      testSubject.peek()
    }
    assertThat(testSubject).hasCount(10)
    assertThat(testSubject.isPopulated).isTrue()
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
      assertThat(result).isEqualTo(expected)
    }
  }

  companion object {

    private fun buildData(count: Int): List<Int> {
      require(count >= 0) { "count must be positive (was $count)" }
      val data = ImmutableList.builder<Int>()
      for (i in 0 until count) {
        data.add(i)
      }
      return data.build()
    }
  }
}