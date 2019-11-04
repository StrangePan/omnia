package omnia.data.structure.rx;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import omnia.data.iterate.IntegerRangeIterator;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;

final class ObservableListImpl<E> implements ObservableList<E> {
  private volatile ImmutableList<E> currentState = ImmutableList.empty();
  private final Subject<MutationEvent<E>> subject = PublishSubject.create();
  private final ObservableChannels observableChannels = new ObservableChannels();

  @Override
  public void insertAt(int index, E item) {
    IndexRange range = IndexRange.just(index);
    mutateState(
        state -> true,
        state -> state.toBuilder().insertAt(index, item).build(),
        (previousState, currentState) -> generateInsertAtMutations(currentState, range));
  }

  @Override
  public void removeAt(int index) {
    IndexRange range = IndexRange.just(index);
    mutateState(
        state -> true,
        state -> state.toBuilder().removeAt(index).build(),
        (previousState, currentState) -> generateRemoveAtMutations(previousState, range));
  }

  @Override
  public void replaceAt(int index, E item) {
    IndexRange range = IndexRange.just(index);
    mutateState(
        state -> true,
        state -> state.toBuilder().replaceAt(index, item).build(),
        (previousState, currentState) ->
            generateReplaceAtMutations(previousState, currentState, range));
  }

  @Override
  public void add(E item) {
    mutateState(
        state -> true,
        state -> state.toBuilder().add(item).build(),
        (previousState, currentState) ->
            generateInsertAtMutations(currentState, IndexRange.just(currentState.count() - 1)));
  }

  @Override
  public boolean remove(E item) {
    synchronized(this) {
      OptionalInt index = getState().indexOf(item);
      return mutateState(
          state -> index.isPresent(),
          state -> state.toBuilder().removeAt(index.getAsInt()).build(),
          (previousState, currentState) ->
              generateRemoveAtMutations(
                  previousState, IndexRange.just(index.getAsInt())));
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
                  previousState, new IndexRange(0, previousState.count())));
    }
  }

  private boolean mutateState(
      Predicate<ImmutableList<E>> shouldMutate,
      Function<ImmutableList<E>, ImmutableList<E>> mutator,
      BiFunction<ImmutableList<E>, ImmutableList<E>, List<ListMutation<E>>> mutationsGenerator) {
    synchronized(this) {
      ImmutableList<E> previousState = currentState;
      if (!shouldMutate.test(previousState)) {
        return false;
      }
      ImmutableList<E> newState = mutator.apply(previousState);
      currentState = newState;
      subject.onNext(
          new MutationEvent<>(newState, mutationsGenerator.apply(previousState, newState)));
      return true;
    }
  }

  private static <E> ImmutableList<ListMutation<E>> generateInsertAtMutations(
      ImmutableList<E> state, IndexRange range) {
    ImmutableList.Builder<ListMutation<E>> builder = ImmutableList.builder();

    // Move must be done before insert to make way for new items
    if (range.end() < state.count()) {
      int numItemsMoved = state.count() - range.end();
      IndexRange moveFromRange = new IndexRange(range.start(), range.start() + numItemsMoved);
      IndexRange moveToRange = new IndexRange(range.end(), range.end() + numItemsMoved);
      builder.add(new MoveInList<>(moveToRange.asSublistOf(state), moveFromRange, moveToRange));
    }

    builder.add(new AddToList<>(range.asSublistOf(state), range));

    return builder.build();
  }

  private static <E> ImmutableList<ListMutation<E>> generateRemoveAtMutations(
      ImmutableList<E> previousState, IndexRange range) {
    ImmutableList.Builder<ListMutation<E>> builder = ImmutableList.builder();

    /// Removal must be done BEFORE move to make space for moved items
    builder.add(new RemoveFromList<>(range.asSublistOf(previousState), range));

    if (range.end() < previousState.count()) {
      int numItemsMoved = previousState.count() - range.end();
      IndexRange moveFromRange = new IndexRange(range.end(), range.end() + numItemsMoved);
      IndexRange moveToRange = new IndexRange(range.start(), range.start() + numItemsMoved);
      builder.add(
          new MoveInList<>(moveFromRange.asSublistOf(previousState), moveFromRange, moveToRange));
    }

    return builder.build();
  }

  private static <E> ImmutableList<ListMutation<E>> generateReplaceAtMutations(
      ImmutableList<E> previousState, ImmutableList<E> currentState, IndexRange range) {
    return ImmutableList.of(
        new ReplaceInList<>(
            range.asSublistOf(previousState), range.asSublistOf(currentState), range));
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
  public ObservableChannels observe() {
    return observableChannels;
  }

  private ImmutableList<E> getState() {
    synchronized(this) {
      return currentState;
    }
  }

  private MutationEvent<E> generateMutationEventForNewSubscription() {
    List<E> state = getState();
    return new MutationEvent<>(
        state,
        ImmutableList.of(
            new AddToList<>(state, new IndexRange(0, state.count()))));
  }

  private final static class MutationEvent<E>
      implements ObservableDataStructure.MutationEvent<List<E>, ListMutations<E>> {
    private final List<E> state;
    private final ListMutations<E> mutations;

    private MutationEvent(List<E> state, List<ListMutation<E>> mutations) {
      this.state = state;
      this.mutations = new ListMutations<>(mutations);
    }

    @Override
    public List<E> state() {
      return state;
    }

    @Override
    public ListMutations<E> mutations() {
      return mutations;
    }
  }

  private final static class ListMutations<E> implements ObservableList.ListMutations<E> {
    private final List<ListMutation<E>> mutations;

    private ListMutations(List<ListMutation<E>> mutations) {
      this.mutations = mutations;
    }

    @Override
    public List<ListMutation<E>> asList() {
      return mutations;
    }

    @Override
    public Iterator<ListMutation<E>> iterator() {
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
    public Stream<ListMutation<E>> stream() {
      return mutations.stream();
    }
  }

  private final static class AddToList<E> implements ObservableList.AddToList<E> {
    private final List<E> items;
    private final IndexRange indices;

    private AddToList(List<E> items, IndexRange indices) {
      this.items = items;
      this.indices = indices;
    }

    @Override
    public List<E> items() {
      return items;
    }

    @Override
    public IndexRange indices() {
      return indices;
    }
  }

  private final static class MoveInList<E> implements ObservableList.MoveInList<E> {
    private final List<E> items;
    private final IndexRange previousIndices;
    private final IndexRange currentIndices;

    private MoveInList(List<E> items, IndexRange previousIndices, IndexRange currentIndices) {
      this.items = items;
      this.previousIndices = previousIndices;
      this.currentIndices = currentIndices;
    }

    @Override
    public List<E> items() {
      return items;
    }

    @Override
    public IndexRange previousIndicies() {
      return previousIndices;
    }

    @Override
    public IndexRange currentIndicies() {
      return currentIndices;
    }
  }

  private final static class RemoveFromList<E> implements ObservableList.RemoveFromList<E> {
    private final List<E> items;
    private final IndexRange indices;

    private RemoveFromList(List<E> items, IndexRange indices) {
      this.items = items;
      this.indices = indices;
    }

    @Override
    public List<E> items() {
      return items;
    }

    @Override
    public IndexRange indices() {
      return indices;
    }
  }

  private final static class ReplaceInList<E> implements ObservableList.ReplaceInList<E> {
    private final List<E> replacedItems;
    private final List<E> newItems;
    private final IndexRange indices;

    private ReplaceInList(List<E> replacedItems, List<E> newItems, IndexRange indices) {
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
    public IndexRange indices() {
      return indices;
    }
  }

  private final static class IndexRange implements ObservableList.IndexRange {
    private final int start;
    private final int end;

    private static IndexRange just(int index) {
      return new IndexRange(index, index + 1);
    }

    private IndexRange(int start, int end) {
      if (start <= end) {
        throw new IllegalArgumentException(
            "range must describe a non-empty set of integers. start=" + start + " end=" + end);
      }
      this.start = start;
      this.end = end;
    }

    @Override
    public int start() {
      return start;
    }

    @Override
    public int end() {
      return end;
    }

    @Override
    public int endInclusive() {
      return end - 1;
    }

    @Override
    public int count() {
      return end - start;
    }

    @Override
    public boolean isPopulated() {
      return start < end;
    }

    @Override
    public Iterator<Integer> iterator() {
      return IntegerRangeIterator.create(start, end);
    }

    private <E> ImmutableList<E> asSublistOf(ImmutableList<E> list) {
      return list.sublistStartingAt(start()).to(end());
    }
  }

  private class ObservableChannels
      implements ObservableDataStructure.ObservableChannels<
              List<E>, ObservableList.ListMutations<E>> {

    // When something subscribes, in what order do the subscriptions occur?
    private final Flowable<MutationEvent<E>> mutations =
        Flowable.<MutationEvent<E>>create(
            flowableEmitter -> {
              flowableEmitter.onNext(generateMutationEventForNewSubscription());
              flowableEmitter.onComplete();
            },
            BackpressureStrategy.BUFFER)
        .concatWith(subject.toFlowable(BackpressureStrategy.BUFFER));

    private final Flowable<List<E>> states = mutations.map(MutationEvent::state);

    @Override
    public Flowable<List<E>> states() {
      return states;
    }

    @Override
    public Flowable<
            ? extends ObservableDataStructure.MutationEvent<? extends List<E>,
            ? extends ObservableList.ListMutations<E>>> mutations() {
      return mutations;
    }
  }
}