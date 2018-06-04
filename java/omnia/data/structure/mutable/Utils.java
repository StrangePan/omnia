package omnia.data.structure.mutable;

final class Utils {

  private Utils() {}

  static void checkIndex(int index, int lower, int upper) {
    if (index < lower || index >= upper) {
      throw new IndexOutOfBoundsException(
          String.format("%d outside the range [%d, %d)", index, lower, upper));
    }
  }

  static void checkIndexInclusive(int index, int lower, int upper) {
    if (index < lower || index > upper) {
      throw new IndexOutOfBoundsException(
          String.format("%d outside the range [%d, %d]", index, lower, upper));
    }
  }
}