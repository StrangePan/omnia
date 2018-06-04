package omnia.data.stream;

import omnia.data.structure.Collection;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableSet;

import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class Collectors {

  public static <E> Collector<E, ?, Set<E>> toSet() {
    return new MaskingCollector<>(java.util.stream.Collectors.toSet(), Set::masking);
  }

  public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
    return new Collector<E, HashSet<E>, ImmutableSet<E>>() {

      @Override
      public Supplier<HashSet<E>> supplier() {
        return HashSet::new;
      }

      @Override
      public BiConsumer<HashSet<E>, E> accumulator() {
        return HashSet::add;
      }

      @Override
      public BinaryOperator<HashSet<E>> combiner() {
        return (set1, set2) -> {
          HashSet<E> set3 = new HashSet<>();
          set3.addAll(set1);
          set3.addAll(set2);
          return set3;
        };
      }

      @Override
      public Function<HashSet<E>, ImmutableSet<E>> finisher() {
        return hashSet -> ImmutableSet.<E>builder().addAll(Collection.masking(hashSet)).build();
      }

      @Override
      public java.util.Set<Characteristics> characteristics() {
        return java.util.Set.of(Characteristics.UNORDERED);
      }
    };
  }

  static final class MaskingCollector<T, A, R1, R2> implements Collector<T, A, R2> {
    private final Collector<T, A, R1> maskedCollector;
    private final Function<R1, R2> finisher;

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
      return maskedCollector.characteristics();
    }
  }

  private Collectors() {}
}
