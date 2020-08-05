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

    AtLeastCouple<A, B> dropThird();
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

    @Override
    AtLeastTriple<A, B, D> dropThird();

    AtLeastTriple<A, B, C> dropFourth();
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

    @Override
    AtLeastQuadruple<A, B, D, E> dropThird();

    @Override
    AtLeastQuadruple<A, B, C, E> dropFourth();

    AtLeastQuadruple<A, B, C, D> dropFifth();
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

    @Override
    AtLeastQuintuple<A, B, D, E, F> dropThird();

    @Override
    AtLeastQuintuple<A, B, C, E, F> dropFourth();

    @Override
    AtLeastQuintuple<A, B, C, D, F> dropFifth();

    AtLeastQuintuple<A, B, C, D, E> dropSixth();
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

    @Override
    AtLeastSextuple<A, B, D, E, F, G> dropThird();

    @Override
    AtLeastSextuple<A, B, C, E, F, G> dropFourth();

    @Override
    AtLeastSextuple<A, B, C, D, F, G> dropFifth();

    @Override
    AtLeastSextuple<A, B, C, D, E, G> dropSixth();

    AtLeastSextuple<A, B, C, D, E, F> dropSeventh();
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

    @Override
    AtLeastSeptuple<A, B, D, E, F, G, H> dropThird();

    @Override
    AtLeastSeptuple<A, B, C, E, F, G, H> dropFourth();

    @Override
    AtLeastSeptuple<A, B, C, D, F, G, H> dropFifth();

    @Override
    AtLeastSeptuple<A, B, C, D, E, G, H> dropSixth();

    @Override
    AtLeastSeptuple<A, B, C, D, E, F, H> dropSeventh();

    AtLeastSeptuple<A, B, C, D, E, F, G> dropEighth();
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

    @Override
    AtLeastOctuple<A, B, D, E, F, G, H, I> dropThird();

    @Override
    AtLeastOctuple<A, B, C, E, F, G, H, I> dropFourth();

    @Override
    AtLeastOctuple<A, B, C, D, F, G, H, I> dropFifth();

    @Override
    AtLeastOctuple<A, B, C, D, E, G, H, I> dropSixth();

    @Override
    AtLeastOctuple<A, B, C, D, E, F, H, I> dropSeventh();

    @Override
    AtLeastOctuple<A, B, C, D, E, F, G, I> dropEighth();

    AtLeastOctuple<A, B, C, D, E, F, G, H> dropNinth();
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

    @Override
    AtLeastNonuple<A, B, D, E, F, G, H, I, J> dropThird();

    @Override
    AtLeastNonuple<A, B, C, E, F, G, H, I, J> dropFourth();

    @Override
    AtLeastNonuple<A, B, C, D, F, G, H, I, J> dropFifth();

    @Override
    AtLeastNonuple<A, B, C, D, E, G, H, I, J> dropSixth();

    @Override
    AtLeastNonuple<A, B, C, D, E, F, H, I, J> dropSeventh();

    @Override
    AtLeastNonuple<A, B, C, D, E, F, G, I, J> dropEighth();

    @Override
    AtLeastNonuple<A, B, C, D, E, F, G, H, J> dropNinth();

    AtLeastNonuple<A, B, C, D, E, F, G, H, I> dropTenth();
  }

  interface AtMostMonuple extends AtMostCouple {

    @Override
    <T> AtMostCouple append(T object);

    @Override
    <T> AtMostCouple prepend(T object);
  }

  interface AtMostCouple extends AtMostTriple {

    @Override
    <T> AtMostTriple prepend(T object);

    @Override
    <T> AtMostTriple append(T object);
  }

  interface AtMostTriple extends AtMostQuadruple {

    @Override
    <T> AtMostQuadruple prepend(T object);

    @Override
    <T> AtMostQuadruple append(T object);
  }

  interface AtMostQuadruple extends AtMostQuintuple {

    @Override
    <T> AtMostQuintuple prepend(T object);

    @Override
    <T> AtMostQuintuple append(T object);
  }

  interface AtMostQuintuple extends AtMostSextuple {

    @Override
    <T> AtMostSextuple prepend(T object);

    @Override
    <T> AtMostSextuple append(T object);
  }

  interface AtMostSextuple extends AtMostSeptuple {

    @Override
    <T> AtMostSeptuple prepend(T object);

    @Override
    <T> AtMostSeptuple append(T object);
  }

  interface AtMostSeptuple extends AtMostOctuple {

    @Override
    <T> AtMostOctuple prepend(T object);

    @Override
    <T> AtMostOctuple append(T object);
  }

  interface AtMostOctuple extends AtMostNonuple {

    @Override
    <T> AtMostNonuple prepend(T object);

    @Override
    <T> AtMostNonuple append(T object);
  }

  interface AtMostNonuple extends AtMostDecuple {

    <T> AtMostDecuple prepend(T object);

    <T> AtMostDecuple append(T object);
  }

  interface AtMostDecuple {}
}
