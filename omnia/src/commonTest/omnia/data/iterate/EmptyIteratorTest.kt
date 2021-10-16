package omnia.data.iterate

import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse

class EmptyIteratorTest {

  @Test
  fun hasNext_isFalse() {
    assertThat(EmptyIterator.create<Any>().hasNext()).isFalse()
  }

  @Test
  fun next_throwsNoSuchElementException() {
    assertFailsWith(NoSuchElementException::class) {
      EmptyIterator.create<Any>().next()
    }
  }

  @Test
  fun forEach_doesNothing() {
    var invocations = 0
    EmptyIterator.create<Any>().forEach { _ -> ++invocations }
    assertThat(invocations).isEqualTo(0)
  }
}