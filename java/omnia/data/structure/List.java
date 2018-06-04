package omnia.data.structure;

import java.util.OptionalInt;

public interface List<E> extends Collection<E> {

  E getAt(int index);

  OptionalInt indexOf(E element);
}
