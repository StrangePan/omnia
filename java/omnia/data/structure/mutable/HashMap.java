package omnia.data.structure.mutable;

import omnia.data.structure.Map;

public class HashMap<K, V> extends MaskingMap<K, V> {

  public static <K, V> HashMap<K, V> create() {
    return new HashMap<>();
  }

  public static <K, V> HashMap<K, V> copyOf(Map<? extends K, ? extends V> original) {
    return new HashMap<>(
        original.entries().stream()
            .collect(java.util.stream.Collectors.toMap(Map.Entry::key, Map.Entry::value)));
  }

  private HashMap() {
    super(new java.util.HashMap<>());
  }

  private HashMap(java.util.Map<? extends K, ? extends V> original) {
    super(new java.util.HashMap<>(original));
  }
}
