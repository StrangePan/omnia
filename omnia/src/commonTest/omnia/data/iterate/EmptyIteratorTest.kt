package omnia.data.iterate

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.assertThatCode
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse

class EmptyIteratorTest {

  @Test
  fun hasNext_isFalse() {
    assertThat(EmptyIterator.create<Any>().hasNext()).isFalse()
  }

  @Test
  fun next_throwsNoSuchElementException() {
    assertThatCode {
      EmptyIterator.create<Any>().next()
    }.failsWith(NoSuchElementException::class)
  }

  @Test
  fun forEach_doesNothing() {
    var invocations = 0
    EmptyIterator.create<Any>().forEach { _ -> ++invocations }
    assertThat(invocations).isEqualTo(0)
  }
}
