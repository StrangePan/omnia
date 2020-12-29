package omnia.data.cache

import com.google.common.truth.Truth
import java.util.function.IntSupplier
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class SimpleIntMemoizerTest {

  @Test
  fun value_didReturnSuppliedResult() {
    val testValue = 132
    val testSubject: MemoizedInt = SimpleIntMemoizer { testValue }
    Truth.assertThat(testSubject.value()).isEqualTo(testValue)
  }

  @Test
  fun value_twice_didReturnSuppliedResult() {
    val testValue = 132
    val testSubject: MemoizedInt = SimpleIntMemoizer { testValue }
    testSubject.value()
    Assert.assertEquals(testValue.toLong(), testSubject.value().toLong())
  }

  @Test
  fun value_didInvokeSupplier() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedInt = SimpleIntMemoizer(supplier)
    testSubject.value()
    Mockito.verify(supplier).asInt
  }

  @Test
  fun new_didNotInvokeSupplier() {
    val supplier = setUpSupplier()
    SimpleIntMemoizer(supplier)
    Mockito.verify(supplier, Mockito.never()).asInt
  }

  @Test
  fun value_twice_onlyInvokedSupplierOnce() {
    val supplier = setUpSupplier()
    val testSubject: MemoizedInt = SimpleIntMemoizer(supplier)
    testSubject.value()
    testSubject.value()
    Mockito.verify(supplier, Mockito.times(1)).asInt
  }

  companion object {

    private fun setUpSupplier(): IntSupplier {
      val supplier = Mockito.mock(IntSupplier::class.java)
      Mockito.`when`(supplier.asInt).thenReturn(132)
      return supplier
    }
  }
}