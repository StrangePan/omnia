package omnia.data.structure.mutable;

import java.util.Arrays;

public final class ArrayList<E> extends MaskingList<E> {

  public ArrayList() {
    this(new java.util.ArrayList<>());
  }

  private ArrayList(java.util.ArrayList<E> javaList) {
    super(javaList);
  }

  public static <E> ArrayList<E> of(E[] items) {
    return new ArrayList<>(new java.util.ArrayList<>(Arrays.asList(items)));
  }
}
