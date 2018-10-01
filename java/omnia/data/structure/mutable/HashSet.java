package omnia.data.structure.mutable;

public final class HashSet<E> extends MaskingSet<E> {

  public HashSet() {
    super(new java.util.HashSet<>());
  }
}
