package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Couple<A, B> extends Monuple<A> {

  B second();

  @Override
  <R> Couple<R, B> mapFirst(Function<? super A, ? extends R> mapper);
}
