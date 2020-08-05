package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Monuple<A> extends Tuple {

  A first();

  <R> Monuple<R> mapFirst(Function<? super A, ? extends R> mapper);
}
