package omnia.data.structure.rx;

import static com.google.common.truth.Truth.assertThat;
import static omnia.data.stream.Collectors.toImmutableSet;

import io.reactivex.subscribers.TestSubscriber;
import omnia.algorithm.SetAlgorithms;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.rx.ObservableSet.AddToSet;
import omnia.data.structure.rx.ObservableSet.MutationEvent;
import omnia.data.structure.rx.ObservableSet.RemoveFromSet;
import omnia.data.structure.rx.ObservableSet.SetOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ObservableSetTest {

  @Test
  public void isPopulated_whenInit_isEmpty() {
    assertThat(ObservableSet.create().isPopulated()).isFalse();
  }

  @Test
  public void isPopulated_whenItemsAdded_isPopulated() {
    MutableSet<Object> set = ObservableSet.create();

    set.add(new Object());

    assertThat(set.isPopulated()).isTrue();
  }

  @Test
  public void isPopulated_whenItemsAddedThenRemoved_isEmpty() {
    MutableSet<Object> set = ObservableSet.create();
    Object object = new Object();

    set.add(object);
    set.remove(object);

    assertThat(set.isPopulated()).isFalse();
  }

  @Test
  public void isPopulated_whenItemsAddedThenCleared_isEmpty() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object());
    MutableSet<Object> set = ObservableSet.create();

    contents.forEach(set::add);
    set.clear();

    assertThat(set.isPopulated()).isFalse();
  }

  @Test
  public void count_whenInit_isZero() {
    assertThat(ObservableSet.create().count()).isEqualTo(0);
  }

  @Test
  public void count_whenItemsAdded_isEqualToItemsAdded() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object());
    MutableSet<Object> set = ObservableSet.create();

    contents.forEach(set::add);

    assertThat(set.count()).isEqualTo(contents.count());
  }

  @Test
  public void count_whenItemsAddedThenRemoved_isZero() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object());
    MutableSet<Object> set = ObservableSet.create();

    contents.forEach(set::add);
    contents.forEach(set::remove);

    assertThat(set.count()).isEqualTo(0);
  }

  @Test
  public void count_whenItemsAddedThenCleared_isZero() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object());
    MutableSet<Object> set = ObservableSet.create();

    contents.forEach(set::add);
    set.clear();

    assertThat(set.count()).isEqualTo(0);
  }

  @Test
  public void observeStates_whenEmpty_emitsEmpty() {
    ObservableSet<Object> set = ObservableSet.create();

    set.observe().states().test().assertValue(state -> !state.isPopulated());
  }

  @Test
  public void observeMutations_whenEmpty_emitsEmpty() {
    ObservableSet<Object> set = ObservableSet.create();

    TestSubscriber<? extends MutationEvent<Object>> subscriber =
        set.observe().mutations().test();
    subscriber.assertValue(event -> !event.state().isPopulated());
    subscriber.assertValue(event -> !event.operations().isPopulated());
  }

  @Test
  public void observeStates_whenPopulated_emitsValues() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object(), new Object());
    ObservableSet<Object> set = ObservableSet.create();
    contents.forEach(set::add);

    set.observe().states().test().assertValue(state -> Set.areEqual(state, contents));
  }

  @Test
  public void observeMutations_whenPopulated_emitsValues() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object(), new Object());
    ObservableSet<Object> set = ObservableSet.create();
    contents.forEach(set::add);

    TestSubscriber<Set<Object>> testSubscriber =
        set.observe().mutations().map(MutationEvent::state).test();

    testSubscriber.assertValue(state -> Set.areEqual(state, contents));
  }

  @Test
  public void observeMutations_whenPopulated_emitsMutations() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object(), new Object());
    ObservableSet<Object> set = ObservableSet.create();
    contents.forEach(set::add);

    TestSubscriber<? extends SetOperation<Object>> testSubscriber =
        set.observe().mutations().map(MutationEvent::operations).flatMapIterable(s -> s).test();

    testSubscriber.values().forEach(
        operation -> assertThat(operation).isInstanceOf(AddToSet.class));
    assertThat(
        testSubscriber.values().stream()
            .map(operation -> (AddToSet<?>) operation)
            .map(AddToSet::item)
            .collect(toImmutableSet()))
        .containsExactlyElementsIn(contents);
  }

  @Test
  public void observeStates_whenPopulated_thenAddItems_emitsStates() {
    Set<Object> originalContents = ImmutableSet.of(new Object(), new Object());
    Set<Object> addedContents = ImmutableSet.of(new Object(), new Object());
    Set<Object> finalContents = SetAlgorithms.unionOf(originalContents, addedContents);
    ObservableSet<Object> set = ObservableSet.create();

    originalContents.forEach(set::add);

    // Skip 1 because first emission is the initialization subscription
    TestSubscriber<? extends Set<Object>> testSubscriber =
        set.observe().states().skip(1).test();

    addedContents.forEach(set::add);

    testSubscriber.assertValueCount(addedContents.count());
    testSubscriber.values().forEach(
        actualState -> assertThat(finalContents).containsAtLeastElementsIn(actualState));
    assertThat(testSubscriber.values().get(addedContents.count() - 1))
        .containsExactlyElementsIn(finalContents);
  }

  @Test
  public void observeMutations_whenPopulated_thenRemoveItems_emitsMutations() {
    Set<Object> finalContents = ImmutableSet.of(new Object(), new Object());
    Set<Object> removedContents = ImmutableSet.of(new Object(), new Object());
    Set<Object> originalContents = SetAlgorithms.unionOf(finalContents, removedContents);
    ObservableSet<Object> set = ObservableSet.create();

    originalContents.forEach(set::add);

    // Skip 1 because first emission is the initialization subscription
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        set.observe().mutations().skip(1).test();

    removedContents.forEach(set::remove);

    testSubscriber.assertValueCount(removedContents.count());
    testSubscriber.values().forEach(event -> assertThat(event.operations().count()).isEqualTo(1));
    Set<? extends SetOperation<Object>> operations =
        testSubscriber.values().stream()
            .map(MutationEvent::operations)
            .flatMap(Set::stream)
            .collect(toImmutableSet());
    operations.forEach(operation -> assertThat(operation).isInstanceOf(RemoveFromSet.class));
    Set<Object> actualRemovedContents =
        operations.stream()
            .map(operation -> (RemoveFromSet<Object>) operation)
            .map(RemoveFromSet::item)
            .collect(toImmutableSet());
    assertThat(actualRemovedContents).containsExactlyElementsIn(removedContents);
  }

  @Test
  public void observeMutations_whenPopulated_thenCleared_emitsMutation() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object());
    ObservableSet<Object> set = ObservableSet.create();

    contents.forEach(set::add);

    // Skip 1 because first emission is the initialization subscription
    TestSubscriber<? extends MutationEvent<Object>> testSubscriber =
        set.observe().mutations().skip(1).test();

    set.clear();

    testSubscriber.assertValueCount(1);
    testSubscriber.assertValue(event -> event.operations().count() == contents.count());
    Set<? extends SetOperation<Object>> operations =
        testSubscriber.values().stream()
            .map(MutationEvent::operations)
            .flatMap(Set::stream)
            .collect(toImmutableSet());
    operations.forEach(operation -> assertThat(operation).isInstanceOf(RemoveFromSet.class));
    Set<Object> actualRemovedContents =
        operations.stream()
            .map(operation -> (RemoveFromSet<Object>) operation)
            .map(RemoveFromSet::item)
            .collect(toImmutableSet());
    assertThat(actualRemovedContents).containsExactlyElementsIn(contents);
  }

  @Test
  public void observeStates_whenPopulated_thenCleared_emitsState() {
    Set<Object> contents = ImmutableSet.of(new Object(), new Object());
    ObservableSet<Object> set = ObservableSet.create();

    contents.forEach(set::add);

    // Skip 1 because first emission is the initialization subscription
    TestSubscriber<? extends Set<Object>> testSubscriber =
        set.observe().states().skip(1).test();

    set.clear();

    testSubscriber.assertValueCount(1);
    testSubscriber.assertValue(actualState -> Set.areEqual(actualState, ImmutableSet.empty()));
  }
}
