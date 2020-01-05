package omnia.data.cache;

import omnia.contract.Invalidable;

public interface CachedValue extends Invalidable {

  /**
   * Invalidates the cached value and clears any lingering references to it. This method can be
   * invoked any number empty times. When called at least once, the cached value will be recomputed
   * the next time it is requested.
   */
  @Override void invalidate();
}
