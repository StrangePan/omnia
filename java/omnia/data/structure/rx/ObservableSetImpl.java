package omnia.data.structure.rx;

import static omnia.data.stream.Collectors.toSet;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import omnia.algorithm.SetAlgorithms;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableSet;

final class ObservableSetImpl<E> implements ObservableSet<E> {
  private volatile ImmutableSet<E> currentState = ImmutableSet.empty();
  private final Subject<MutationEvent<E>> subject = PublishSubject.create();
  private final ObservableChannels observableChannels = new ObservableChannels();

  @Override
  public void add(E element) {
    mutateState(
        state -> !state.contains(element),
        state -> ImmutableSet.copyOf(SetAlgorithms.unionOf(state, ImmutableSet.of(element))),
        (previousState, currentState) -> ImmutableSet.of(new AddToSet<>(element)));
  }

  @Override
  public boolean remove(E element) {
    return mutateState(
        state -> state.contains(element),
        state -> ImmutableSet.copyOf(
            SetAlgorithms.differenceBetween(state, ImmutableSet.of(element))),
        (previousState, currentState) -> ImmutableSet.of(new RemoveFromSet<>(element)));
  }

  @Override
  public void clear() {
    mutateState(
        ImmutableSet::isPopulated,
        state -> ImmutableSet.empty(),
        (previousState, currentState) ->
            previousState.stream().map(RemoveFromSet::new).collect(toSet()));
  }

  private boolean mutateState(
      Predicate<ImmutableSet<E>> shouldMutate,
      Function<ImmutableSet<E>, ImmutableSet<E>> mutator,
      BiFunction<ImmutableSet<E>, ImmutableSet<E>, Set<SetMutation<E>>> mutationsGenerator) {
    synchronized (this) {
      ImmutableSet<E> previousState = currentState;
      if (!shouldMutate.test(previousState)) {
        return false;
      }
      ImmutableSet<E> newState = mutator.apply(previousState);
      currentState = newState;
      subject.onNext(
          new MutationEvent<>(newState, mutationsGenerator.apply(previousState, newState)));
      return true;
    }
  }

  @Override
  public Iterator<E> iterator() {
    return getState().iterator();
  }

  @Override
  public boolean contains(Object element) {
    return getState().contains(element);
  }

  @Override
  public int count() {
    return getState().count();
  }

  @Override
  public boolean isPopulated() {
    return getState().isPopulated();
  }

  @Override
  public Stream<E> stream() {
    return getState().stream();
  }

  @Override
  public
  ObservableDataStructure.ObservableChannels<Set<E>, ObservableSet.SetMutations<E>> observe() {
    return observableChannels;
  }

  private ImmutableSet<E> getState() {
    synchronized (this) {
      return currentState;
    }
  }

  private static final class MutationEvent<E>
      implements ObservableDataStructure.MutationEvent<Set<E>, SetMutations<E>> {
    private final Set<E> state;
    private final SetMutations<E> mutations;

    private MutationEvent(Set<E> state, Set<SetMutation<E>> mutations) {
      this.state = state;
      this.mutations = new SetMutations<>(mutations);
    }

    @Override
    public Set<E> state() {
      return state;
    }

    @Override
    public SetMutations<E> mutations() {
      return mutations;
    }
  }

  private static final class SetMutations<E> implements ObservableSet.SetMutations<E> {
    private final Set<SetMutation<E>> mutations;

    private SetMutations(Set<SetMutation<E>> mutations) {
      this.mutations = mutations;
    }

    @Override
    public Set<SetMutation<E>> asSet() {
      return mutations;
    }

    @Override
    public Iterator<SetMutation<E>> iterator() {
      return mutations.iterator();
    }

    @Override
    public int count() {
      return mutations.count();
    }

    @Override
    public boolean isPopulated() {
      return mutations.isPopulated();
    }

    @Override
    public Stream<SetMutation<E>> stream() {
      return mutations.stream();
    }
  }

  private static final class AddToSet<E> implements ObservableSet.AddToSet<E> {
    private final E item;

    private AddToSet(E item) {
      this.item = item;
    }

    @Override
    public E item() {
      return item;
    }
  }

  private static final class RemoveFromSet<E> implements ObservableSet.RemoveFromSet<E> {
    private final E item;

    private RemoveFromSet(E item) {
      this.item = item;
    }

    @Override
    public E item() {
      return item;
    }
  }

  private class ObservableChannels
      implements ObservableDataStructure.ObservableChannels<Set<E>, ObservableSet.SetMutations<E>> {

    // When something subscribes, in what order do the subscriptions occur?
    private final Flowable<MutationEvent<E>> mutations =
        Flowable.<MutationEvent<E>>create(
            flowableEmitter -> {
              flowableEmitter.onNext(generateMutationEventForNewSubscription());
              flowableEmitter.onComplete();
            },
            BackpressureStrategy.BUFFER)
            .concatWith(subject.toFlowable(BackpressureStrategy.BUFFER));

    private final Flowable<Set<E>> states = mutations.map(MutationEvent::state);

    @Override
    public Flowable<? extends Set<E>> states() {
      return states;
    }

    @Override
    public Flowable<
            ? extends ObservableDataStructure.MutationEvent<? extends Set<E>,
            ? extends ObservableSet.SetMutations<E>>> mutations() {
      return mutations;
    }
  }

  private MutationEvent<E> generateMutationEventForNewSubscription() {
    Set<E> state = getState();
    return new MutationEvent<>(
        state,
        state.stream().map(AddToSet::new).collect(toSet()));
  }
}
