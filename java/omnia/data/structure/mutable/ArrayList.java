package omnia.data.structure.mutable;

import java.util.Arrays;
import omnia.data.structure.List;

public final class ArrayList<E> extends MaskingList<E> {

  public ArrayList() {
    this(new java.util.ArrayList<>());
  }

  private ArrayList(java.util.ArrayList<E> javaList) {
    super(javaList);
  }

  @SafeVarargs
  public static <E> ArrayList<E> of(E...items) {
    return new ArrayList<>(new java.util.ArrayList<>(Arrays.asList(items)));
  }

  public static <E> ArrayList<E> copyOf(List<? extends E> otherList) {
    ArrayList<E> newList = new ArrayList<>();
    otherList.iterator().forEachRemaining(newList::add);
    return newList;
  }
}
