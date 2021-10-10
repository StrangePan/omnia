package omnia.data.iterate

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.mockito.Mockito
import org.mockito.kotlin.mock

class EmptyIteratorTest {

  private val consumer: (Any) -> Unit = mock()

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
  fun forEachRemaining_doesNothing() {
    EmptyIterator.create<Any>().forEachRemaining(consumer)
    Mockito.verifyNoInteractions(consumer)
  }
}