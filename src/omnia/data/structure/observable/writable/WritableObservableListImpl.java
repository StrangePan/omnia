package omnia.data.structure.observable.writable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import omnia.data.structure.Collection;
import omnia.data.structure.IntRange;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;
import omnia.data.structure.observable.ObservableList;

final class WritableObservableListImpl<E> implements WritableObservableList<E> {

  private volatile ImmutableList<E> currentState = ImmutableList.empty();
  private final FlowableProcessor<MutationEvent> mutationEventProcessor =
      PublishProcessor.<MutationEvent>create().toSerialized();
  private final ObservableChannels observableChannels = new ObservableChannels();

  @Override
  public void insertAt(int index, E item) {
    IntRange range = IntRange.startingAt(index).withLength(1);
    mutateState(
        state -> true,
        state -> state.toBuilder().insertAt(index, item).build(),
        (previousState, currentState) -> generateInsertAtMutations(currentState, range));
  }

  @Override
  public E removeAt(int index) {
    IntRange range = IntRange.startingAt(index).withLength(1);
    E value;
    synchronized (this) {
      value = getState().itemAt(index);
      mutateState(
          state -> true,
          state -> state.toBuilder().removeAt(index).build(),
          (previousState, currentState) -> generateRemoveAtMutations(previousState, range));
    }
    return value;
  }

  @Override
  public E replaceAt(int index, E item) {
    IntRange range = IntRange.startingAt(index).withLength(1);
    E value;
    synchronized (this) {
      value = getState().itemAt(index);
      mutateState(
          state -> true,
          state -> state.toBuilder().replaceAt(index, item).build(),
          (previousState, currentState) ->
              generateReplaceAtMutations(previousState, currentState, range));
    }
    return value;
  }

  @Override
  public void add(E item) {
    mutateState(
        state -> true,
        state -> state.toBuilder().add(item).build(),
        (previousState, currentState) -> generateInsertAtMutations(currentState, IntRange.startingAt(currentState.count() - 1).withLength(1)));
  }

  @Override
  public void addAll(Collection<? extends E> elements) {
    mutateState(
        state -> true,
        state -> state.toBuilder().addAll(elements).build(),
        (previousState, currentState) -> generateInsertAtMutations(
            currentState, IntRange.startingAt(previousState.count()).endingAt(currentState.count())));
  }

  @Override
  public boolean removeUnknownTyped(Object item) {
    synchronized(this) {
      OptionalInt index = getState().indexOf(item);
      return mutateState(
          state -> index.isPresent(),
          state -> state.toBuilder().removeAt(index.orElseThrow()).build(),
          (previousState, currentState) -> generateRemoveAtMutations(
              previousState, IntRange.startingAt(index.orElseThrow()).withLength(1)));
    }
  }

  @Override
  public void clear() {
    synchronized(this) {
      mutateState(
          ImmutableList::isPopulated,
          state -> ImmutableList.empty(),
          (previousState, currentState) ->
              generateRemoveAtMutations(
                  previousState, IntRange.startingAt(0).endingAt(previousState.count())));
    }
  }

  private boolean mutateState(
      Predicate<ImmutableList<E>> shouldMutate,
      Function<ImmutableList<E>, ImmutableList<E>> mutator,
      BiFunction<ImmutableList<E>, ImmutableList<E>, List<ListOperation<E>>> mutationsGenerator) {
    synchronized(this) {
      ImmutableList<E> previousState = currentState;
      if (!shouldMutate.test(previousState)) {
        return false;
      }
      ImmutableList<E> newState = mutator.apply(previousState);
      currentState = newState;
      mutationEventProcessor.onNext(
          new MutationEvent(newState, mutationsGenerator.apply(previousState, newState)));
      return true;
    }
  }

  private static <E> ImmutableList<ListOperation<E>> generateInsertAtMutations(
      ImmutableList<E> state, IntRange range) {
    ImmutableList.Builder<ListOperation<E>> builder = ImmutableList.builder();

    // Move must be done before insert to make way for new items
    if (range.end() < state.count()) {
      int numItemsMoved = state.count() - range.end();
      IntRange moveFromRange = IntRange.startingAt(range.start()).withLength(numItemsMoved);
      IntRange moveToRange = IntRange.startingAt(range.end()).withLength(numItemsMoved);
      builder.add(new MoveInList<>(state.getSublist(moveToRange), moveFromRange, moveToRange));
    }

    builder.add(new AddToList<>(state.getSublist(range), range));

    return builder.build();
  }

  private static <E> ImmutableList<ListOperation<E>> generateRemoveAtMutations(
      ImmutableList<E> previousState, IntRange range) {
    ImmutableList.Builder<ListOperation<E>> builder = ImmutableList.builder();

    /// Removal must be done BEFORE move to make space for moved items
    builder.add(new RemoveFromList<>(previousState.getSublist(range), range));

    if (range.end() < previousState.count()) {
      int numItemsMoved = previousState.count() - range.end();
      IntRange moveFromRange = IntRange.startingAt(range.end()).withLength(numItemsMoved);
      IntRange moveToRange = IntRange.startingAt(range.start()).withLength(numItemsMoved);
      builder.add(
          new MoveInList<>(previousState.getSublist(moveFromRange), moveFromRange, moveToRange));
    }

    return builder.build();
  }

  private static <E> ImmutableList<ListOperation<E>> generateReplaceAtMutations(
      ImmutableList<E> previousState, ImmutableList<E> currentState, IntRange range) {
    return ImmutableList.of(
        new ReplaceInList<>(
            previousState.getSublist(range), currentState.getSublist(range), range));
  }

  @Override
  public Iterator<E> iterator() {
    return getState().iterator();
  }

  @Override
  public boolean containsUnknownTyped(Object element) {
    return getState().containsUnknownTyped(element);
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
  public E itemAt(int index) {
    return getState().itemAt(index);
  }

  @Override
  public OptionalInt indexOf(Object item) {
    return getState().indexOf(item);
  }

  @Override
  public Stream<E> stream() {
    return getState().stream();
  }

  @Override
  public ObservableList<E> toReadOnly() {
    return new ObservableList<>() {
      @Override
      public ObservableChannels<E> observe() {
        return WritableObservableListImpl.this.observe();
      }

      @Override
      public Iterator<E> iterator() {
        return WritableObservableListImpl.this.iterator();
      }

      @Override
      public boolean containsUnknownTyped(Object element) {
        return WritableObservableListImpl.this.containsUnknownTyped(element);
      }

      @Override
      public int count() {
        return WritableObservableListImpl.this.count();
      }

      @Override
      public E itemAt(int index) {
        return WritableObservableListImpl.this.itemAt(index);
      }

      @Override
      public OptionalInt indexOf(Object item) {
        return WritableObservableListImpl.this.indexOf(item);
      }

      @Override
      public Stream<E> stream() {
        return WritableObservableListImpl.this.stream();
      }
    };
  }

  @Override
  public ObservableChannels observe() {
    return observableChannels;
  }

  private ImmutableList<E> getState() {
    synchronized(this) {
      return currentState;
    }
  }

  private static final class AddToList<E> implements ObservableList.AddToList<E> {
    private final List<E> items;
    private final IntRange indices;

    private AddToList(List<E> items, IntRange indices) {
      this.items = items;
      this.indices = indices;
    }

    @Override
    public List<E> items() {
      return items;
    }

    @Override
    public IntRange indices() {
      return indices;
    }
  }

  private static final class MoveInList<E> implements ObservableList.MoveInList<E> {
    private final List<E> items;
    private final IntRange previousIndices;
    private final IntRange currentIndices;

    private MoveInList(List<E> items, IntRange previousIndices, IntRange currentIndices) {
      this.items = items;
      this.previousIndices = previousIndices;
      this.currentIndices = currentIndices;
    }

    @Override
    public List<E> items() {
      return items;
    }

    @Override
    public IntRange previousIndices() {
      return previousIndices;
    }

    @Override
    public IntRange currentIndices() {
      return currentIndices;
    }
  }

  private static final class RemoveFromList<E> implements ObservableList.RemoveFromList<E> {
    private final List<E> items;
    private final IntRange indices;

    private RemoveFromList(List<E> items, IntRange indices) {
      this.items = items;
      this.indices = indices;
    }

    @Override
    public List<E> items() {
      return items;
    }

    @Override
    public IntRange indices() {
      return indices;
    }
  }

  private static final class ReplaceInList<E> implements ObservableList.ReplaceInList<E> {
    private final List<E> replacedItems;
    private final List<E> newItems;
    private final IntRange indices;

    private ReplaceInList(List<E> replacedItems, List<E> newItems, IntRange indices) {
      this.replacedItems = replacedItems;
      this.newItems = newItems;
      this.indices = indices;
    }

    @Override
    public List<E> replacedItems() {
      return replacedItems;
    }

    @Override
    public List<E> newItems() {
      return newItems;
    }

    @Override
    public IntRange indices() {
      return indices;
    }
  }

  private class ObservableChannels extends GenericObservableChannels<List<E>, MutationEvent>
      implements ObservableList.ObservableChannels<E> {


    protected ObservableChannels() {
      super(
          Flowable.<List<E>>create(
                  flowableEmitter -> {
                    flowableEmitter.onNext(getState());
                    flowableEmitter.onComplete();
                  },
                  BackpressureStrategy.BUFFER)
              .concatWith(mutationEventProcessor.map(MutationEvent::state)),
          Flowable.<MutationEvent>create(
                  flowableEmitter -> {
                    flowableEmitter.onNext(generateMutationEventForNewSubscription());
                    flowableEmitter.onComplete();
                  },
                  BackpressureStrategy.BUFFER)
              .concatWith(mutationEventProcessor));
    }
  }

  private class MutationEvent
      extends GenericMutationEvent<List<E>, List<ObservableList.ListOperation<E>>>
      implements ObservableList.MutationEvent<E> {
    private MutationEvent(List<E> state, List<ListOperation<E>> operations) {
      super(state, operations);
    }
  }

  private MutationEvent generateMutationEventForNewSubscription() {
    List<E> state = getState();
    return new MutationEvent(
        state, ImmutableList.of(new AddToList<>(state, IntRange.startingAt(0).endingAt(state.count()))));
  }
}
