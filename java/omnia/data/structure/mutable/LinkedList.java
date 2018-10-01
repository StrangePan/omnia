package omnia.data.structure.mutable;

public final class LinkedList<E> extends MaskingList<E> {

  public LinkedList() {
    super(new java.util.LinkedList<>());
  }
}
