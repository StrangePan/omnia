package omnia.data.iterate

import com.google.common.truth.Truth.assertThat
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EmptyIteratorTest {

  @Mock private lateinit var consumer: (Any) -> Unit

  @BeforeTest
  fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

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