package omnia.data.structure.mutable;

public class HashMap<K, V> extends MaskingMap<K, V> {

  public HashMap() {
    super(new java.util.HashMap<>());
  }
}
