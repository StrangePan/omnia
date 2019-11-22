package omnia.data.iterate;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;
import omnia.data.structure.mutable.ArrayList;
import omnia.data.structure.mutable.MutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FilterIteratorTest {

  private static final List<Integer> TEST_DATA =
      ImmutableList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

  @Test
  public void iterate_whenPredicateMatchesAll_iteratesOverAll() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), unused -> true);
    assertThat(underTest.next()).isEqualTo(0);
    assertThat(underTest.next()).isEqualTo(1);
    assertThat(underTest.next()).isEqualTo(2);
    assertThat(underTest.next()).isEqualTo(3);
    assertThat(underTest.next()).isEqualTo(4);
    assertThat(underTest.next()).isEqualTo(5);
    assertThat(underTest.next()).isEqualTo(6);
    assertThat(underTest.next()).isEqualTo(7);
    assertThat(underTest.next()).isEqualTo(8);
    assertThat(underTest.next()).isEqualTo(9);
    assertThat(underTest.next()).isEqualTo(10);
    assertThrows(NoSuchElementException.class, underTest::next);
  }

  @Test
  public void iterate_whenPredicateMatchesEvens_iteratesOverEvens() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), isEven());
    assertThat(underTest.next()).isEqualTo(0);
    assertThat(underTest.next()).isEqualTo(2);
    assertThat(underTest.next()).isEqualTo(4);
    assertThat(underTest.next()).isEqualTo(6);
    assertThat(underTest.next()).isEqualTo(8);
    assertThat(underTest.next()).isEqualTo(10);
    assertThrows(NoSuchElementException.class, underTest::next);
  }

  @Test
  public void iterate_whenPredicateMatchesOdds_iteratesOverOdds() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), isOdd());
    assertThat(underTest.next()).isEqualTo(1);
    assertThat(underTest.next()).isEqualTo(3);
    assertThat(underTest.next()).isEqualTo(5);
    assertThat(underTest.next()).isEqualTo(7);
    assertThat(underTest.next()).isEqualTo(9);
    assertThrows(NoSuchElementException.class, underTest::next);
  }

  @Test
  public void iterate_whenPredicateMatchesNone_iteratesOverNone() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), unused -> false);
    assertThrows(NoSuchElementException.class, underTest::next);
  }

  @Test
  public void hasNext_whenPredicateMatchesAll_isTrue() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), unused -> true);
    assertThat(underTest.hasNext()).isTrue();
  }

  @Test
  public void hasNext_whenPredicateMatchesNone_isFalse() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), unused -> false);
    assertThat(underTest.hasNext()).isFalse();
  }

  @Test
  public void hasNext_whenPredicateMatchesFirstHalf_whenHalfwayIterated_isFalse() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), number -> number < 5);

    underTest.next(); // 0
    underTest.next(); // 1
    underTest.next(); // 2
    underTest.next(); // 3
    underTest.next(); // 4

    assertThat(underTest.hasNext()).isFalse();
  }

  @Test
  public void hasNext_whenPredicateMatchesFirstHalf_whenHasValuesLeft_isTrue() {
    Iterator<Integer> underTest = new FilterIterator<>(TEST_DATA.iterator(), number -> number < 5);

    underTest.next(); // 0
    underTest.next(); // 1
    underTest.next(); // 2
    underTest.next(); // 3

    assertThat(underTest.hasNext()).isTrue();
  }

  @Test
  public void remove_whenPredicateMatchesEvens_removesOnlyEvens() {
    MutableList<Integer> list = ArrayList.copyOf(TEST_DATA);
    Iterator<Integer> underTest = new FilterIterator<>(list.iterator(), isEven());

    underTest.next(); // 0
    underTest.remove();
    underTest.next(); // 2
    underTest.remove();
    underTest.next(); // 4
    underTest.remove();
    underTest.next(); // 6
    underTest.remove();
    underTest.next(); // 8
    underTest.remove();
    underTest.next(); // 10
    underTest.remove();

    assertThat(list.count()).isEqualTo(5);
    assertThat(list.itemAt(0)).isEqualTo(1);
    assertThat(list.itemAt(1)).isEqualTo(3);
    assertThat(list.itemAt(2)).isEqualTo(5);
    assertThat(list.itemAt(3)).isEqualTo(7);
    assertThat(list.itemAt(4)).isEqualTo(9);
  }

  private static Predicate<Integer> isEven() {
    return number -> number % 2 == 0;
  }

  private static Predicate<Integer> isOdd() {
    return number -> number % 2 == 1;
  }
}
