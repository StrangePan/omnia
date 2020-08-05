package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Triple<A, B, C> extends Tuples.AtLeastTriple<A, B, C> {

  @Override
  default int count() {
    return 3;
  }

  @Override
  <R> Triple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Triple<A, R, C> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Triple<A, B, R> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  Couple<B, C> dropFirst();

  @Override
  Couple<A, C> dropSecond();
}
