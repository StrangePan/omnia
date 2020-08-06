package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Octuple<A, B, C, D, E, F, G, H> extends Tuples.AtLeastOctuple<A, B, C, D, E, F, G, H>, Tuples.AtMostOctuple {

  @Override
  default int count() {
    return 8;
  }

  @Override
  <R> Octuple<R, B, C, D, E, F, G, H> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Octuple<A, R, C, D, E, F, G, H> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Octuple<A, B, R, D, E, F, G, H> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Octuple<A, B, C, R, E, F, G, H> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Octuple<A, B, C, D, R, F, G, H> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  <R> Octuple<A, B, C, D, E, R, G, H> mapSixth(Function<? super F, ? extends R> mapper);

  @Override
  <R> Octuple<A, B, C, D, E, F, R, H> mapSeventh(Function<? super G, ? extends R> mapper);

  @Override
  <R> Octuple<A, B, C, D, E, F, G, R> mapEighth(Function<? super H, ? extends R> mapper);

  @Override
  Septuple<B, C, D, E, F, G, H> dropFirst();

  @Override
  Septuple<A, C, D, E, F, G, H> dropSecond();

  @Override
  Septuple<A, B, D, E, F, G, H> dropThird();

  @Override
  Septuple<A, B, C, E, F, G, H> dropFourth();

  @Override
  Septuple<A, B, C, D, F, G, H> dropFifth();

  @Override
  Septuple<A, B, C, D, E, G, H> dropSixth();

  @Override
  Septuple<A, B, C, D, E, F, H> dropSeventh();

  @Override
  Septuple<A, B, C, D, E, F, G> dropEighth();

  @Override
  <T> Nonuple<A, B, C, D, E, F, G, H, T> append(T object);

  @Override
  <I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Monuple<I> other);

  @Override
  <I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Couple<I, J> other);
}
