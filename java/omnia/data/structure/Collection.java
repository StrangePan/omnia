package omnia.data.structure;

import omnia.contract.Container;
import omnia.contract.Countable;

public interface Collection<E> extends Countable, Iterable<E>, Container<E> {}
