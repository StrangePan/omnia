package omnia.data.structure.tuple;

import java.util.function.Function;

public class Tuples {

  interface AtLeastCouple<A, B> extends Tuple {

    A first();

    B second();

    <R> AtLeastCouple<R, B> mapFirst(Function<? super A, ? extends R> mapper);

    <R> AtLeastCouple<A, R> mapSecond(Function<? super B, ? extends R> mapper);
  }

  interface AtLeastTriple<A, B, C> extends AtLeastCouple<A, B> {

    C third();

    @Override
    <R> AtLeastTriple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);

    @Override
    <R> AtLeastTriple<A, R, C> mapSecond(Function<? super B, ? extends R> mapper);

    <R> AtLeastTriple<A, B, R> mapThird(Function<? super C, ? extends R> mapper);

    AtLeastCouple<B, C> dropFirst();

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

  interface AtMostCouple extends AtMostTriple {

    @Override
    <T> AtMostTriple append(T object);

    @Override
    <C, D> AtMostQuadruple concat(Couple<C, D> other);

    @Override
    <C, D, E> AtMostQuintuple concat(Triple<C, D, E> other);

    @Override
    <C, D, E, F> AtMostSextuple concat(Quadruple<C, D, E, F> other);

    @Override
    <C, D, E, F, G> AtMostSeptuple concat(Quintuple<C, D, E, F, G> other);

    @Override
    <C, D, E, F, G, H> AtMostOctuple concat(Sextuple<C, D, E, F, G, H> other);

    @Override
    <C, D, E, F, G, H, I> AtMostNonuple concat(Septuple<C, D, E, F, G, H, I> other);

    <C, D, E, F, G, H, I, J> AtMostDecuple concat(Octuple<C, D, E, F, G, H, I, J> other);
  }

  interface AtMostTriple extends AtMostQuadruple {

    @Override
    <T> AtMostQuadruple append(T object);

    @Override
    <D, E> AtMostQuintuple concat(Couple<D, E> other);

    @Override
    <D, E, F> AtMostSextuple concat(Triple<D, E, F> other);

    @Override
    <D, E, F, G> AtMostSeptuple concat(Quadruple<D, E, F, G> other);

    @Override
    <D, E, F, G, H> AtMostOctuple concat(Quintuple<D, E, F, G, H> other);

    @Override
    <D, E, F, G, H, I> AtMostNonuple concat(Sextuple<D, E, F, G, H, I> other);

    <D, E, F, G, H, I, J> AtMostDecuple concat(Septuple<D, E, F, G, H, I, J> other);
  }

  interface AtMostQuadruple extends AtMostQuintuple {

    @Override
    <T> AtMostQuintuple append(T object);

    @Override
    <E, F> AtMostSextuple concat(Couple<E, F> other);

    @Override
    <E, F, G> AtMostSeptuple concat(Triple<E, F, G> other);

    @Override
    <E, F, G, H> AtMostOctuple concat(Quadruple<E, F, G, H> other);

    @Override
    <E, F, G, H, I> AtMostNonuple concat(Quintuple<E, F, G, H, I> other);

    <E, F, G, H, I, J> AtMostDecuple concat(Sextuple<E, F, G, H, I, J> other);
  }

  interface AtMostQuintuple extends AtMostSextuple {

    @Override
    <T> AtMostSextuple append(T object);

    @Override
    <F, G> AtMostSeptuple concat(Couple<F, G> other);

    @Override
    <F, G, H> AtMostOctuple concat(Triple<F, G, H> other);

    @Override
    <F, G, H, I> AtMostNonuple concat(Quadruple<F, G, H, I> other);

    <F, G, H, I, J> AtMostDecuple concat(Quintuple<F, G, H, I, J> other);
  }

  interface AtMostSextuple extends AtMostSeptuple {

    @Override
    <T> AtMostSeptuple append(T object);

    @Override
    <G, H> AtMostOctuple concat(Couple<G, H> other);

    @Override
    <G, H, I> AtMostNonuple concat(Triple<G, H, I> other);

    <G, H, I, J> AtMostDecuple concat(Quadruple<G, H, I, J> other);
  }

  interface AtMostSeptuple extends AtMostOctuple {

    @Override
    <T> AtMostOctuple append(T object);

    @Override
    <H, I> AtMostNonuple concat(Couple<H, I> other);

    <H, I, J> AtMostDecuple concat(Triple<H, I, J> other);
  }

  interface AtMostOctuple extends AtMostNonuple {

    @Override
    <T> AtMostNonuple append(T object);

    <I, J> AtMostDecuple concat(Couple<I, J> other);
  }

  interface AtMostNonuple extends AtMostDecuple {

    <T> AtMostDecuple append(T object);
  }

  interface AtMostDecuple {}

  interface AtMostCouplet<T> extends AtMostTriplet<T>, AtMostCouple {

    @Override
    AtMostTriplet<T> concatlet(T object);

    @Override
    AtMostQuadruplet<T> concatlet(Couple<T, T> other);

    @Override
    AtMostQuintuplet<T> concatlet(Triple<T, T, T> other);

    @Override
    AtMostSextuplet<T> concatlet(Quadruple<T, T, T, T> other);

    @Override
    AtMostSeptuplet<T> concatlet(Quintuple<T, T, T, T, T> other);

    @Override
    AtMostOctuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other);

    @Override
    AtMostNonuplet<T> concatlet(Septuple<T, T, T, T, T, T, T> other);

    AtMostDecuplet<T> concatlet(Octuple<T, T, T, T, T, T, T, T> other);
  }

  interface AtMostTriplet<T> extends AtMostQuadruplet<T>, AtMostTriple {

    @Override
    AtMostQuadruplet<T> concatlet(T object);

    @Override
    AtMostQuintuplet<T> concatlet(Couple<T, T> other);

    @Override
    AtMostSextuplet<T> concatlet(Triple<T, T, T> other);

    @Override
    AtMostSeptuplet<T> concatlet(Quadruple<T, T, T, T> other);

    @Override
    AtMostOctuplet<T> concatlet(Quintuple<T, T, T, T, T> other);

    @Override
    AtMostNonuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other);

    AtMostDecuplet<T> concatlet(Septuple<T, T, T, T, T, T, T> other);
  }

  interface AtMostQuadruplet<T> extends AtMostQuintuplet<T>, AtMostQuadruple {

    @Override
    AtMostQuintuplet<T> concatlet(T object);

    @Override
    AtMostSextuplet<T> concatlet(Couple<T, T> other);

    @Override
    AtMostSeptuplet<T> concatlet(Triple<T, T, T> other);

    @Override
    AtMostOctuplet<T> concatlet(Quadruple<T, T, T, T> other);

    @Override
    AtMostNonuplet<T> concatlet(Quintuple<T, T, T, T, T> other);
    
    AtMostDecuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other);
  }

  interface AtMostQuintuplet<T> extends AtMostSextuplet<T>, AtMostQuintuple {

    @Override
    AtMostSextuplet<T> concatlet(T object);

    @Override
    AtMostSeptuplet<T> concatlet(Couple<T, T> other);

    @Override
    AtMostOctuplet<T> concatlet(Triple<T, T, T> other);

    @Override
    AtMostNonuplet<T> concatlet(Quadruple<T, T, T, T> other);
    
    AtMostDecuplet<T> concatlet(Quintuple<T, T, T, T, T> other);
  }

  interface AtMostSextuplet<T> extends AtMostSeptuplet<T>, AtMostSextuple {

    @Override
    AtMostSeptuplet<T> concatlet(T object);

    @Override
    AtMostOctuplet<T> concatlet(Couple<T, T> other);

    @Override
    AtMostNonuplet<T> concatlet(Triple<T, T, T> other);
    
    AtMostDecuplet<T> concatlet(Quadruple<T, T, T, T> other);
  }

  interface AtMostSeptuplet<T> extends AtMostOctuplet<T>, AtMostSeptuple {

    @Override
    AtMostOctuplet<T> concatlet(T object);

    @Override
    AtMostNonuplet<T> concatlet(Couple<T, T> other);

    AtMostDecuplet<T> concatlet(Triple<T, T, T> other);
  }

  interface AtMostOctuplet<T> extends AtMostNonuplet<T>, AtMostOctuple {

    @Override
    AtMostNonuplet<T> concatlet(T object);

    AtMostDecuplet<T> concatlet(Couple<T, T> other);
  }

  interface AtMostNonuplet<T> extends AtMostDecuplet<T>, AtMostNonuple {

    AtMostDecuplet<T> concatlet(T object);
  }

  interface AtMostDecuplet<T> extends AtMostDecuple {}
}
