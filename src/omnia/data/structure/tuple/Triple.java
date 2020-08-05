package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Triple<A, B, C> extends Couple<A, B> {

  C third();

  @Override
  <R> Triple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);
}
