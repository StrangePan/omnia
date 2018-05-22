package omnia.data.stream;

import omnia.data.structure.Set;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class Collectors {

  public static <E> Collector<E, ?, Set<E>> toSet() {
    return new MaskingCollector<>(java.util.stream.Collectors.toSet(), Set::masking);
  }

  public static final class MaskingCollector<T, A, R1, R2> implements Collector<T, A, R2> {
    private final Collector<T, A, R1> maskedCollector;
    private final Function<R1, R2> finisher;

    public MaskingCollector(Collector<T, A, R1> maskedCollector, Function<R1, R2> finisher) {
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
