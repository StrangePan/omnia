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
  private final Subject<MutationEvent> mutationEventSubject =
      PublishSubject.create();
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
      BiFunction<ImmutableSet<E>, ImmutableSet<E>, Set<SetOperation<E>>> mutationsGenerator) {
    synchronized(this) {
      ImmutableSet<E> previousState = currentState;
      if (!shouldMutate.test(previousState)) {
        return false;
      }
      ImmutableSet<E> newState = mutator.apply(previousState);
      currentState = newState;
      mutationEventSubject.onNext(
          new MutationEvent(newState, mutationsGenerator.apply(previousState, newState)));
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
  public ObservableChannels observe() {
    return observableChannels;
  }

  private ImmutableSet<E> getState() {
    synchronized (this) {
      return currentState;
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

  private class ObservableChannels extends GenericObservableChannels<Set<E>, MutationEvent>
      implements ObservableSet.ObservableChannels<E> {

    protected ObservableChannels() {
      super(
          Flowable.<Set<E>>create(
              flowableEmitter -> {
                flowableEmitter.onNext(getState());
                flowableEmitter.onComplete();
              },
              BackpressureStrategy.BUFFER)
              .concatWith(
                  mutationEventSubject.toFlowable(BackpressureStrategy.BUFFER)
                      .map(MutationEvent::state)),
          Flowable.<MutationEvent>create(
              flowableEmitter -> {
                flowableEmitter.onNext(generateMutationEventForNewSubscription());
                flowableEmitter.onComplete();
              },
              BackpressureStrategy.BUFFER)
              .concatWith(mutationEventSubject.toFlowable(BackpressureStrategy.BUFFER)));
    }
  }

  private class MutationEvent extends GenericMutationEvent<Set<E>, Set<SetOperation<E>>>
      implements ObservableSet.MutationEvent<E> {
    private MutationEvent(Set<E> state, Set<SetOperation<E>> operations) {
      super(state, operations);
    }
  }

  private MutationEvent generateMutationEventForNewSubscription() {
    Set<E> state = getState();
    return new MutationEvent(state, state.stream().map(AddToSet::new).collect(toSet()));
  }
}
