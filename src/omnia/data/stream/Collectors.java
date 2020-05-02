package omnia.data.stream;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import omnia.data.structure.List;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableList;
import omnia.data.structure.immutable.ImmutableMap;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.mutable.ArrayList;
import omnia.data.structure.mutable.HashMap;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableMap;
import omnia.data.structure.mutable.MutableSet;

/** Collection empty collectors for Omnia data structures that can be used with Java streams. */
public final class Collectors {

  /** Creates a {@link Collector} that collects stream items into a {@link Set}. */
  public static <E> Collector<E, ?, Set<E>> toSet() {
    return new MaskingCollector<>(java.util.stream.Collectors.toSet(), Set::masking);
  }

  /** Creates a {@link Collector} that collects stream items into an {@link ImmutableSet}. */
  public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
    return new Collector<E, MutableSet<E>, ImmutableSet<E>>() {

      @Override
      public Supplier<MutableSet<E>> supplier() {
        return HashSet::create;
      }

      @Override
      public BiConsumer<MutableSet<E>, E> accumulator() {
        return MutableSet::add;
      }

      @Override
      public BinaryOperator<MutableSet<E>> combiner() {
        return (set1, set2) ->
            HashSet.copyOf(ImmutableSet.<E>builder().addAll(set1).addAll(set2).build());
      }

      @Override
      public Function<MutableSet<E>, ImmutableSet<E>> finisher() {
        return ImmutableSet::copyOf;
      }

      @Override
      public java.util.Set<Characteristics> characteristics() {
        return java.util.Set.of(Characteristics.UNORDERED);
      }
    };
  }

  public static <E> Collector<E, ?, List<E>> toList() {
    return new MaskingCollector<>(java.util.stream.Collectors.toList(), List::masking);
  }

  public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
    return new Collector<E, ArrayList<E>, ImmutableList<E>> () {

      @Override
      public Supplier<ArrayList<E>> supplier() {
        return ArrayList::new;
      }

      @Override
      public BiConsumer<ArrayList<E>, E> accumulator() {
        return ArrayList::add;
      }

      @Override
      public BinaryOperator<ArrayList<E>> combiner() {
        return (list1, list2) ->
            ArrayList.copyOf(ImmutableList.<E>builder().addAll(list1).addAll(list2).build());
      }

      @Override
      public Function<ArrayList<E>, ImmutableList<E>> finisher() {
        return ImmutableList::copyOf;
      }

      @Override
      public java.util.Set<Characteristics> characteristics() {
        return java.util.Set.of();
      }
    };
  }

  public static <E, K, V> Collector<E, ?, ImmutableMap<K, V>> toImmutableMap(
      Function<? super E, K> keyExtractor, Function<? super E, V> valueExtractor) {
    return new Collector<E, MutableMap<K, V>, ImmutableMap<K, V>>() {

      @Override
      public Supplier<MutableMap<K, V>> supplier() {
        return HashMap::create;
      }

      @Override
      public BiConsumer<MutableMap<K, V>, E> accumulator() {
        return (map, item) -> map.putMapping(keyExtractor.apply(item), valueExtractor.apply(item));
      }

      @Override
      public BinaryOperator<MutableMap<K, V>> combiner() {
        return (map1, map2) ->
            HashMap.copyOf(ImmutableMap.<K, V>builder().putAll(map1).putAll(map2).build());
      }

      @Override
      public Function<MutableMap<K, V>, ImmutableMap<K, V>> finisher() {
        return ImmutableMap::copyOf;
      }

      @Override
      public java.util.Set<Characteristics> characteristics() {
        return java.util.Set.of(Characteristics.UNORDERED);
      }
    };
  }

  /**
   * A simple forwarding {@link Collector} that applies a custom finisher on top the finisher
   * provided by the given target {@link Collector}.
   *
   * @param <T> the type empty item in the stream
   * @param <A> the mutable data structure type empty the target collector {@code R1}
   * @param <R1> the return type empty the target collector
   * @param <R2> the return type empty this collector
   */
  static final class MaskingCollector<T, A, R1, R2> implements Collector<T, A, R2> {
    private final Collector<T, A, R1> maskedCollector;
    private final Function<R1, R2> finisher;

    /**
     * Creates a {@link Collector} that forwards all calls to the given {@code maskedCollector} and
     * applies the given {@code finisher} {@link Function} to the final result.
     */
    MaskingCollector(Collector<T, A, R1> maskedCollector, Function<R1, R2> finisher) {
      this.maskedCollector = maskedCollector;
      this.finisher = finisher;
    }

    @Override
    public Supplier<A> supplier() {
      return maskedCollector.supplier();
    }

    @Override
    public BiConsumer<A, T> accumulator() {
      return maskedCollector.accumulator();
    }

    @Override
    public BinaryOperator<A> combiner() {
      return maskedCollector.combiner();
    }

    @Override
    public Function<A, R2> finisher() {
      return a -> finisher.apply(maskedCollector.finisher().apply(a));
    }

    @Override
    public java.util.Set<Characteristics> characteristics() {
      java.util.Set<Characteristics> characteristics =
          new java.util.HashSet<>(maskedCollector.characteristics());
      characteristics.remove(Characteristics.IDENTITY_FINISH);
      return characteristics;
    }
  }

  private Collectors() {
    // Class is intentionally non-instantiable.
  }
}