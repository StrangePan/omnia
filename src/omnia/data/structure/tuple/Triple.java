package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Triple<A, B, C> extends Tuples.AtLeastTriple<A, B, C> {

  @Override
  <R> Triple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);
}
