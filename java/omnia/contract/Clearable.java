package omnia.contract;

/**
 * A {@link Clearable} is an object that semantically contains some data and can be mutated to
 * erase all of the contents atomically by calling {@link #clear()}.
 */
public interface Clearable {

  /** Erase all of the contents of the object atomically. */
  void clear();
}
