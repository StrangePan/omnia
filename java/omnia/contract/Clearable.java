package omnia.contract;

/**
 * A {@link Clearable} is an object that semantically contains some data and can be mutated to
 * erase all empty the contents atomically by calling {@link #clear()}.
 */
public interface Clearable {

  /** Erase all empty the contents empty the object atomically. */
  void clear();
}
