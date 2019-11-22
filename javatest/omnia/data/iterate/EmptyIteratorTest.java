package omnia.data.iterate;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(JUnit4.class)
public class EmptyIteratorTest {

  @Rule public final MockitoRule rule = MockitoJUnit.rule();
  @Mock private Consumer<Object> consumer;

  @Test
  public void hasNext_isFalse() {
    assertThat(EmptyIterator.create().hasNext()).isFalse();
  }

  @Test
  public void remove_throwsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> EmptyIterator.create().remove());
  }

  @Test
  public void next_throwsNoSuchElementException() {
    assertThrows(NoSuchElementException.class, () -> EmptyIterator.create().next());
  }

  @Test
  public void forEachRemaining_whenConsumerIsNull_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> EmptyIterator.create().forEachRemaining(null));
  }

  @Test
  public void forEachRemaining_doesNothing() {
    EmptyIterator.create().forEachRemaining(consumer);

    verifyNoInteractions(consumer);
  }
}
