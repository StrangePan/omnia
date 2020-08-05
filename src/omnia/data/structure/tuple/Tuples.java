package omnia.data.structure.tuple;

import java.util.function.Function;

public class Tuples {

  interface AtLeastMonuple<A> extends Tuple {

    A first();

    <R> AtLeastMonuple<R> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastCouple<A, B> extends AtLeastMonuple<A> {

    B second();

    @Override
    <R> AtLeastCouple<R, B> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastTriple<A, B, C> extends AtLeastCouple<A, B> {

    C third();

    @Override
    <R> AtLeastTriple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastQuadruple<A, B, C, D> extends AtLeastTriple<A, B, C> {

    D fourth();

    @Override
    <R> AtLeastQuadruple<R, B, C, D> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastQuintuple<A, B, C, D, E> extends AtLeastQuadruple<A, B, C, D> {

    E fifth();

    @Override
    <R> AtLeastQuintuple<R, B, C, D, E> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastSextuple<A, B, C, D, E, F> extends AtLeastQuintuple<A, B, C, D, E> {

    F sixth();

    @Override
    <R> AtLeastSextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastSeptuple<A, B, C, D, E, F, G> extends AtLeastSextuple<A, B, C, D, E, F> {

    G seventh();

    @Override
    <R> AtLeastSeptuple<R, B, C, D, E, F, G> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastOctuple<A, B, C, D, E, F, G, H> extends AtLeastSeptuple<A, B, C, D, E, F, G> {

    H eighth();

    @Override
    <R> AtLeastOctuple<R, B, C, D, E, F, G, H> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastNonuple<A, B, C, D, E, F, G, H, I> extends AtLeastOctuple<A, B, C, D, E, F, G, H> {

    I ninth();

    @Override
    <R> AtLeastNonuple<R, B, C, D, E, F, G, H, I> mapFirst(Function<? super A, ? extends R> mapper);
  }

  interface AtLeastDecuple<A, B, C, D, E, F, G, H, I, J> extends AtLeastNonuple<A, B, C, D, E, F, G, H, I> {

    J tenth();

    @Override
    <R> AtLeastDecuple<R, B, C, D, E, F, G, H, I, J> mapFirst(Function<? super A, ? extends R> mapper);
  }
}
