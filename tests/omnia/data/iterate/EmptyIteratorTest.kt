package omnia.data.iterate

import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.NoSuchElementException
import java.util.function.Consumer

@RunWith(JUnit4::class)
class EmptyIteratorTest {

  @Rule @JvmField val rule: MockitoRule = MockitoJUnit.rule()

  @Mock private lateinit var consumer: Consumer<Any>

  @Test
  fun hasNext_isFalse() {
    Truth.assertThat(EmptyIterator.create<Any>().hasNext()).isFalse()
  }

  @Test
  fun next_throwsNoSuchElementException() {
    Assertions.assertThrows(NoSuchElementException::class.java) { EmptyIterator.create<Any>().next() }
  }

  @Test
  fun forEachRemaining_doesNothing() {
    EmptyIterator.create<Any>().forEachRemaining(consumer!!)
    Mockito.verifyNoInteractions(consumer)
  }
}