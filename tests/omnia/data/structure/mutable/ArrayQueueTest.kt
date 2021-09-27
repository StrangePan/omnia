package omnia.data.structure.mutable

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.mutable.ArrayQueue.Companion.createWithInitialCapacity

class ArrayQueueTest {

  @Test
  fun init_isEmpty() {
    val testSubject: Queue<*> = ArrayQueue.create<Any>()
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun init_countIsZero() {
    val testSubject: Queue<*> = ArrayQueue.create<Any>()
    assertThat(testSubject.count().toLong()).isEqualTo(0)
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
    assertThat(testSubject.count().toLong()).isEqualTo(1)
  }

  @Test
  fun enqueue_null_didThrowException() {
    val testSubject: Queue<Any?> = ArrayQueue.create()
    assertFailsWith(NullPointerException::class) { testSubject.enqueue(null) }
  }

  @Test
  fun enqueue_thenDequeue_didReturnObject() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    val item = Any()
    testSubject.enqueue(item)
    val result = testSubject.dequeue()
    assertThat(result.isPresent).isTrue()
    assertThat(result.get()).isSameInstanceAs(item)
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
    assertThat(testSubject.count().toLong()).isEqualTo(0)
  }

  @Test
  fun dequeue_whenEmpty_didReturnEmpty() {
    assertThat(ArrayQueue.create<Any>().dequeue().isPresent).isFalse()
  }

  @Test
  fun enqueue_thenDequeueTwice_didReturnEmpty() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    testSubject.dequeue()
    assertThat(testSubject.dequeue().isPresent).isFalse()
  }

  @Test
  fun enqueue_fiveHundredTimes_isExpectedSize() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    assertThat(testSubject.count().toLong()).isEqualTo(500)
  }

  @Test
  fun enqueue_fiveHundredTimes_thenDequeueFiveHundredTimes_didReturnExpectedItems() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (datum in data) {
      testSubject.enqueue(datum)
    }
    for (i in 0..499) {
      assertThat(testSubject.dequeue().get()).isEqualTo(data.itemAt(i))
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
    assertThat(testSubject.count().toLong()).isEqualTo(0)
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun enqueue_thenDequeue_fiveHundredTimes_didReturnExpectedItems() {
    val data = buildData(500)
    val testSubject: Queue<Int> = ArrayQueue.create()
    for (i in 0..499) {
      val item = data.itemAt(i)
      testSubject.enqueue(item)
      assertThat(testSubject.dequeue().get()).isEqualTo(item)
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
    assertThat(testSubject.count().toLong()).isEqualTo(0)
    assertThat(testSubject.isPopulated).isFalse()
  }

  @Test
  fun peek_whenEmpty_isEmpty() {
    assertThat(ArrayQueue.create<Any>().peek().isPresent).isFalse()
  }

  @Test
  fun peek_withOne_didReturnObject() {
    val testSubject: Queue<Int> = ArrayQueue.create()
    testSubject.enqueue(132)
    val datum = testSubject.peek()
    assertThat(datum.isPresent).isTrue()
    assertThat(datum.get()).isEqualTo(Integer.valueOf(132))
  }

  @Test
  fun peek_withOne_didNotDequeue() {
    val testSubject: Queue<Any> = ArrayQueue.create()
    testSubject.enqueue(Any())
    testSubject.peek()
    assertThat(testSubject.count().toLong()).isEqualTo(1)
    assertThat(testSubject.isPopulated).isTrue()
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
    assertThat(testSubject.count().toLong()).isEqualTo(10)
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
      assertThat(result.isPresent).isTrue()
      assertThat(result.get()).isEqualTo(expected)
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