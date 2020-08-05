package omnia.data.structure.tuple;

import java.util.function.Function;

public class Tuples {

  interface AtLeastMonuple<A> extends Tuple {

    @Override
    default boolean isPopulated() {
      return true;
    }

    A first();

    <R> AtLeastMonuple<R> mapFirst(Function<? super A, ? extends R> mapper);

    Tuple dropFirst();
  }

  interface AtLeastCouple<A, B> extends AtLeastMonuple<A> {

    B second();

    @Override
    <R> AtLeastCouple<R, B> mapFirst(Function<? super A, ? extends R> mapper);

    <R> AtLeastCouple<A, R> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    AtLeastMonuple<B> dropFirst();

    AtLeastMonuple<A> dropSecond();
  }

  interface AtLeastTriple<A, B, C> extends AtLeastCouple<A, B> {

    C third();

    @Override
    <R> AtLeastTriple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastTriple<A, R, C> mapSecond(Function<? super B, ? extends R> mapper);

    <R> AtLeastTriple<A, B, R> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    AtLeastCouple<B, C> dropFirst();

    @Override
    AtLeastCouple<A, C> dropSecond();
  }

  interface AtLeastQuadruple<A, B, C, D> extends AtLeastTriple<A, B, C> {

    D fourth();

    @Override
    <R> AtLeastQuadruple<R, B, C, D> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastQuadruple<A, R, C, D> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastQuadruple<A, B, R, D> mapThird(Function<? super C, ? extends R> mapper);

    <R> AtLeastQuadruple<A, B, C, R> mapFourth(Function<? super D, ? extends R> mapper);

    @Override
    AtLeastTriple<B, C, D> dropFirst();

    @Override
    AtLeastTriple<A, C, D> dropSecond();
  }

  interface AtLeastQuintuple<A, B, C, D, E> extends AtLeastQuadruple<A, B, C, D> {

    E fifth();

    @Override
    <R> AtLeastQuintuple<R, B, C, D, E> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastQuintuple<A, R, C, D, E> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastQuintuple<A, B, R, D, E> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    <R> AtLeastQuintuple<A, B, C, R, E> mapFourth(Function<? super D, ? extends R> mapper);

    <R> AtLeastQuintuple<A, B, C, D, R> mapFifth(Function<? super E, ? extends R> mapper);

    @Override
    AtLeastQuadruple<B, C, D, E> dropFirst();

    @Override
    AtLeastQuadruple<A, C, D, E> dropSecond();
  }

  interface AtLeastSextuple<A, B, C, D, E, F> extends AtLeastQuintuple<A, B, C, D, E> {

    F sixth();

    @Override
    <R> AtLeastSextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastSextuple<A, R, C, D, E, F> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastSextuple<A, B, R, D, E, F> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    <R> AtLeastSextuple<A, B, C, R, E, F> mapFourth(Function<? super D, ? extends R> mapper);

    @Override
    <R> AtLeastSextuple<A, B, C, D, R, F> mapFifth(Function<? super E, ? extends R> mapper);

    <R> AtLeastSextuple<A, B, C, D, E, R> mapSixth(Function<? super F, ? extends R> mapper);

    @Override
    AtLeastQuintuple<B, C, D, E, F> dropFirst();

    @Override
    AtLeastQuintuple<A, C, D, E, F> dropSecond();
  }

  interface AtLeastSeptuple<A, B, C, D, E, F, G> extends AtLeastSextuple<A, B, C, D, E, F> {

    G seventh();

    @Override
    <R> AtLeastSeptuple<R, B, C, D, E, F, G> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastSeptuple<A, R, C, D, E, F, G> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastSeptuple<A, B, R, D, E, F, G> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    <R> AtLeastSeptuple<A, B, C, R, E, F, G> mapFourth(Function<? super D, ? extends R> mapper);

    @Override
    <R> AtLeastSeptuple<A, B, C, D, R, F, G> mapFifth(Function<? super E, ? extends R> mapper);

    @Override
    <R> AtLeastSeptuple<A, B, C, D, E, R, G> mapSixth(Function<? super F, ? extends R> mapper);

    <R> AtLeastSeptuple<A, B, C, D, E, F, R> mapSeventh(Function<? super G, ? extends R> mapper);

    @Override
    AtLeastSextuple<B, C, D, E, F, G> dropFirst();

    @Override
    AtLeastSextuple<A, C, D, E, F, G> dropSecond();
  }

  interface AtLeastOctuple<A, B, C, D, E, F, G, H> extends AtLeastSeptuple<A, B, C, D, E, F, G> {

    H eighth();

    @Override
    <R> AtLeastOctuple<R, B, C, D, E, F, G, H> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastOctuple<A, R, C, D, E, F, G, H> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastOctuple<A, B, R, D, E, F, G, H> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    <R> AtLeastOctuple<A, B, C, R, E, F, G, H> mapFourth(Function<? super D, ? extends R> mapper);

    @Override
    <R> AtLeastOctuple<A, B, C, D, R, F, G, H> mapFifth(Function<? super E, ? extends R> mapper);

    @Override
    <R> AtLeastOctuple<A, B, C, D, E, R, G, H> mapSixth(Function<? super F, ? extends R> mapper);

    @Override
    <R> AtLeastOctuple<A, B, C, D, E, F, R, H> mapSeventh(Function<? super G, ? extends R> mapper);

    <R> AtLeastOctuple<A, B, C, D, E, F, G, R> mapEighth(Function<? super H, ? extends R> mapper);

    @Override
    AtLeastSeptuple<B, C, D, E, F, G, H> dropFirst();

    @Override
    AtLeastSeptuple<A, C, D, E, F, G, H> dropSecond();
  }

  interface AtLeastNonuple<A, B, C, D, E, F, G, H, I> extends AtLeastOctuple<A, B, C, D, E, F, G, H> {

    I ninth();

    @Override
    <R> AtLeastNonuple<R, B, C, D, E, F, G, H, I> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, R, C, D, E, F, G, H, I> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, B, R, D, E, F, G, H, I> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, B, C, R, E, F, G, H, I> mapFourth(Function<? super D, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, B, C, D, R, F, G, H, I> mapFifth(Function<? super E, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, B, C, D, E, R, G, H, I> mapSixth(Function<? super F, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, B, C, D, E, F, R, H, I> mapSeventh(Function<? super G, ? extends R> mapper);

    @Override
    <R> AtLeastNonuple<A, B, C, D, E, F, G, R, I> mapEighth(Function<? super H, ? extends R> mapper);

    <R> AtLeastNonuple<A, B, C, D, E, F, G, H, R> mapNinth(Function<? super I, ? extends R> mapper);

    @Override
    AtLeastOctuple<B, C, D, E, F, G, H, I> dropFirst();

    @Override
    AtLeastOctuple<A, C, D, E, F, G, H, I> dropSecond();
  }

  interface AtLeastDecuple<A, B, C, D, E, F, G, H, I, J> extends AtLeastNonuple<A, B, C, D, E, F, G, H, I> {

    J tenth();

    @Override
    <R> AtLeastDecuple<R, B, C, D, E, F, G, H, I, J> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, R, C, D, E, F, G, H, I, J> mapSecond(Function<? super B, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, R, D, E, F, G, H, I, J> mapThird(Function<? super C, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, C, R, E, F, G, H, I, J> mapFourth(Function<? super D, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, C, D, R, F, G, H, I, J> mapFifth(Function<? super E, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, C, D, E, R, G, H, I, J> mapSixth(Function<? super F, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, C, D, E, F, R, H, I, J> mapSeventh(Function<? super G, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, C, D, E, F, G, R, I, J> mapEighth(Function<? super H, ? extends R> mapper);

    @Override
    <R> AtLeastDecuple<A, B, C, D, E, F, G, H, R, J> mapNinth(Function<? super I, ? extends R> mapper);

    <R> AtLeastDecuple<A, B, C, D, E, F, G, H, I, R> mapTenth(Function<? super J, ? extends R> mapper);

    @Override
    AtLeastNonuple<B, C, D, E, F, G, H, I, J> dropFirst();

    @Override
    AtLeastNonuple<A, C, D, E, F, G, H, I, J> dropSecond();
  }
}
