package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Monuple<A> extends Tuples.AtLeastMonuple<A> {

  @Override
  default int count() {
    return 1;
  }

  @Override
  <R> Monuple<R> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  Tuple dropFirst();
}
