package omnia.data.structure;

import omnia.contract.Countable;

public interface Pair<E1, E2> extends Countable {

  E1 first();

  E2 second();
}
