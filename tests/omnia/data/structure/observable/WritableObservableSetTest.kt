package omnia.data.structure.observable

import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import omnia.algorithm.SetAlgorithms.unionOf
import omnia.data.stream.Collectors.toImmutableSet
import omnia.data.structure.Set
import omnia.data.structure.Set.Companion.areEqual
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.observable.ObservableSet.SetOperation
import omnia.data.structure.observable.writable.WritableObservableSet
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.function.Consumer

@RunWith(JUnit4::class)
class WritableObservableSetTest {

  @Test
  fun getIsPopulated_whenInit_isEmpty() {
      Truth.assertThat(WritableObservableSet.create<Any>().isPopulated).isFalse()
    }


  @Test
  fun getIsPopulated_whenItemsAdded_isPopulated() {
      val set: MutableSet<Any> = WritableObservableSet.create()
      set.add(Any())
      Truth.assertThat(set.isPopulated).isTrue()
    }


  @Test
  fun getIsPopulated_whenItemsAddedThenRemoved_isEmpty() {
      val set: MutableSet<Any> = WritableObservableSet.create()
      val `object` = Any()
      set.add(`object`)
      set.remove(`object`)
      Truth.assertThat(set.isPopulated).isFalse()
    }


  @Test
  fun getIsPopulated_whenItemsAddedThenCleared_isEmpty() {
      val contents: Set<Any> = ImmutableSet.of(Any(), Any())
      val set: MutableSet<Any> = WritableObservableSet.create()
      contents.forEach(Consumer { item: Any -> set.add(item) })
      set.clear()
      Truth.assertThat(set.isPopulated).isFalse()
    }

  @Test
  fun count_whenInit_isZero() {
    Truth.assertThat(WritableObservableSet.create<Any>().count()).isEqualTo(0)
  }

  @Test
  fun count_whenItemsAdded_isEqualToItemsAdded() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(Consumer { item: Any -> set.add(item) })
    Truth.assertThat(set.count()).isEqualTo(contents.count())
  }

  @Test
  fun count_whenItemsAddedThenRemoved_isZero() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(Consumer { item: Any -> set.add(item) })
    contents.forEach(Consumer { item: Any -> set.remove(item) })
    Truth.assertThat(set.count()).isEqualTo(0)
  }

  @Test
  fun count_whenItemsAddedThenCleared_isZero() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(Consumer { item: Any -> set.add(item) })
    set.clear()
    Truth.assertThat(set.count()).isEqualTo(0)
  }

  @Test
  fun observeStates_whenEmpty_emitsEmpty() {
    val set: ObservableSet<Any> = WritableObservableSet.create()
    set.observe().states().test().assertValue { state: Set<Any> -> !state.isPopulated }
  }

  @Test
  fun observeMutations_whenEmpty_emitsEmpty() {
    val set: ObservableSet<Any> = WritableObservableSet.create()
    val subscriber = set.observe().mutations().test()
    subscriber.assertValue { event: ObservableSet.MutationEvent<Any> -> !event.state().isPopulated }
    subscriber.assertValue { event: ObservableSet.MutationEvent<Any> -> !event.operations().isPopulated }
  }

  @Test
  fun observeStates_whenPopulated_emitsValues() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(Consumer { item: Any -> set.add(item) })
    set.observe().states().test().assertValue { state: Set<Any> -> areEqual(state, contents) }
  }

  @Test
  fun observeMutations_whenPopulated_emitsValues() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(Consumer { item: Any -> set.add(item) })
    val testSubscriber = set.observe().mutations().map { obj -> obj.state() }.test()
    testSubscriber.assertValue { state: Set<Any> -> areEqual(state, contents) }
  }

  @Test
  fun observeMutations_whenPopulated_emitsMutations() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(Consumer { item: Any -> set.add(item) })
    val testSubscriber: TestObserver<out SetOperation<Any>> =
        set.observe().mutations()
            .map { obj -> obj.operations() }
            .flatMapIterable { s -> s }
            .test()
    testSubscriber.values().forEach { operation -> Truth.assertThat(operation).isInstanceOf(ObservableSet.AddToSet::class.java) }
    Truth.assertThat(
        testSubscriber.values().stream()
            .map { operation -> operation as ObservableSet.AddToSet<*> }
            .map { obj -> obj.item() }
            .collect(toImmutableSet()))
        .containsExactlyElementsIn(contents)
  }

  @Test
  fun observeStates_whenPopulated_thenAddItems_emitsStates() {
    val originalContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val addedContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val finalContents = unionOf(originalContents, addedContents)
    val set = WritableObservableSet.create<Any>()
    originalContents.forEach(Consumer { item: Any -> set.add(item) })

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observe().states().skip(1).test()
    addedContents.forEach(Consumer { item: Any -> set.add(item) })
    testSubscriber.assertValueCount(addedContents.count())
    testSubscriber.values().forEach { actualState: Set<Any> -> Truth.assertThat(finalContents).containsAtLeastElementsIn(actualState) }
    Truth.assertThat(testSubscriber.values()[addedContents.count() - 1])
        .containsExactlyElementsIn(finalContents)
  }

  @Test
  fun observeMutations_whenPopulated_thenRemoveItems_emitsMutations() {
    val finalContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val removedContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val originalContents = unionOf(finalContents, removedContents)
    val set = WritableObservableSet.create<Any>()
    originalContents.forEach(Consumer { item: Any -> set.add(item) })

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observe().mutations().skip(1).test()
    removedContents.forEach(Consumer { item: Any -> set.remove(item) })
    testSubscriber.assertValueCount(removedContents.count())
    testSubscriber.values().forEach { event -> Truth.assertThat(event.operations().count()).isEqualTo(1) }
    val operations: Set<out SetOperation<Any>> =
        testSubscriber.values().stream()
            .map { obj -> obj.operations() }
            .flatMap { obj -> obj.stream() }
            .collect(toImmutableSet())
    operations.forEach { operation -> Truth.assertThat(operation).isInstanceOf(ObservableSet.RemoveFromSet::class.java) }
    val actualRemovedContents: Set<Any> = operations.stream()
        .map { operation -> operation as ObservableSet.RemoveFromSet<Any> }
        .map { obj -> obj.item() }
        .collect(toImmutableSet())
    Truth.assertThat(actualRemovedContents).containsExactlyElementsIn(removedContents)
  }

  @Test
  fun observeMutations_whenPopulated_thenCleared_emitsMutation() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(Consumer { item: Any -> set.add(item) })

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observe().mutations().skip(1).test()
    set.clear()
    testSubscriber.assertValueCount(1)
    testSubscriber.assertValue { event -> event.operations().count() == contents.count() }
    val operations: Set<out SetOperation<Any>> = testSubscriber.values().stream()
        .map { obj -> obj.operations() }
        .flatMap { obj -> obj.stream() }
        .collect(toImmutableSet())
    operations.forEach { operation -> Truth.assertThat(operation).isInstanceOf(ObservableSet.RemoveFromSet::class.java) }
    val actualRemovedContents: Set<Any> = operations.stream()
        .map { operation -> operation as ObservableSet.RemoveFromSet<Any> }
        .map { obj -> obj.item() }
        .collect(toImmutableSet())
    Truth.assertThat(actualRemovedContents).containsExactlyElementsIn(contents)
  }

  @Test
  fun observeStates_whenPopulated_thenCleared_emitsState() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(Consumer { item: Any -> set.add(item) })

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observe().states().skip(1).test()
    set.clear()
    testSubscriber.assertValueCount(1)
    testSubscriber.assertValue { actualState: Set<Any> -> areEqual(actualState, ImmutableSet.empty<Any>()) }
  }
}