package omnia.data.structure.tuple;

public interface Triple<A, B, C> extends Couple<A, B> {

  C third();
}
