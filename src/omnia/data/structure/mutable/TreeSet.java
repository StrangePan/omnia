package omnia.data.structure.mutable;

public final class TreeSet<E> extends MaskingSet<E> {

  public TreeSet() {
    super(new java.util.TreeSet<>());
  }
}
