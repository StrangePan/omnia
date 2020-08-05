package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Sextuple<A, B, C, D, E, F> extends Tuples.AtLeastSextuple<A, B, C, D, E, F> {

  @Override
  default int count() {
    return 6;
  }

  @Override
  <R> Sextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Sextuple<A, R, C, D, E, F> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, R, D, E, F> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, C, R, E, F> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, C, D, R, F> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, C, D, E, R> mapSixth(Function<? super F, ? extends R> mapper);

  @Override
  Quintuple<B, C, D, E, F> dropFirst();

  @Override
  Quintuple<A, C, D, E, F> dropSecond();

  @Override
  Quintuple<A, B, D, E, F> dropThird();

  @Override
  Quintuple<A, B, C, E, F> dropFourth();

  @Override
  Quintuple<A, B, C, D, F> dropFifth();

  @Override
  Quintuple<A, B, C, D, E> dropSixth();
}
