package omnia.data.structure;

/**
 * A {@link HomogeneousPair} is a specific subclass of {@link Pair} in which both elements are of
 * the same type.
 *
 * <p>This type of {@link Pair} is useful in cases where the specific ordering of the items in the
 * pair are of no consequence.
 *
 * @param <E> the type of both elements contained in the pair
 */
public interface HomogeneousPair<E> extends Pair<E, E>, Collection<E> {}
