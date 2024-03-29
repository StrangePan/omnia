package omnia.data.structure.observable

import com.badoo.reaktive.observable.flatMapIterable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.skip
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.algorithm.SetAlgorithms
import omnia.algorithm.SetAlgorithms.unionOf
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.observable.ObservableSet.SetOperation
import omnia.data.structure.observable.writable.WritableObservableSet
import omnia.util.reaktive.observable.test.assertThatValue
import omnia.util.reaktive.observable.test.assertValueCount
import omnia.util.reaktive.observable.test.assertValues
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.allMatch
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.containsExactlyElementsIn
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isA
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isTrue

class WritableObservableSetTest {

  @Test
  fun getIsPopulated_whenInit_isEmpty() {
    assertThat(WritableObservableSet.create<Any>().isPopulated).isFalse()
  }

  @Test
  fun getIsPopulated_whenItemsAdded_isPopulated() {
    val set: MutableSet<Any> = WritableObservableSet.create()
    set.add(Any())
    assertThat(set.isPopulated).isTrue()
  }

  @Test
  fun getIsPopulated_whenItemsAddedThenRemoved_isEmpty() {
    val set: MutableSet<Any> = WritableObservableSet.create()
    val value = Any()
    set.add(value)
    set.remove(value)
    assertThat(set.isPopulated).isFalse()
  }

  @Test
  fun getIsPopulated_whenItemsAddedThenCleared_isEmpty() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(set::add)
    set.clear()
    assertThat(set.isPopulated).isFalse()
  }

  @Test
  fun count_whenInit_isZero() {
    assertThat(WritableObservableSet.create<Any>()).hasCount(0)
  }

  @Test
  fun count_whenItemsAdded_isEqualToItemsAdded() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(set::add)
    assertThat(set).hasCount(contents.count)
  }

  @Test
  fun count_whenItemsAddedThenRemoved_isZero() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(set::add)
    contents.forEach(set::remove)
    assertThat(set).hasCount(0)
  }

  @Test
  fun count_whenItemsAddedThenCleared_isZero() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set: MutableSet<Any> = WritableObservableSet.create()
    contents.forEach(set::add)
    set.clear()
    assertThat(set).hasCount(0)
  }

  @Test
  fun observeStates_whenEmpty_emitsEmpty() {
    val set: ObservableSet<Any> = WritableObservableSet.create()
    set.observables.states.test().assertThatValue().isEmpty()
  }

  @Test
  fun observeMutations_whenEmpty_emitsEmpty() {
    val set: ObservableSet<Any> = WritableObservableSet.create()
    val subscriber = set.observables.mutations.test()
    subscriber.assertThatValue { it.state }.isEmpty()
    subscriber.assertThatValue { it.operations }.isEmpty()
  }

  @Test
  fun observeStates_whenPopulated_emitsValues() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(set::add)
    set.observables.states.test().assertThatValue().containsExactlyElementsIn(contents)
  }

  @Test
  fun observeMutations_whenPopulated_emitsValues() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(set::add)
     set.observables.mutations.map { it.state }.test()
        .assertThatValue()
        .containsExactlyElementsIn(contents)
  }

  @Test
  fun observeMutations_whenPopulated_emitsMutations() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(set::add)
    val testSubscriber = set.observables.mutations.flatMapIterable { it.operations }.test()
    testSubscriber.assertValues { it is ObservableSet.AddToSet<*> }
    assertThat(
      testSubscriber.values
        .map { (it as ObservableSet.AddToSet<*>).item }
        .toImmutableSet())
      .containsExactlyElementsIn(contents)
  }

  @Test
  fun observeStates_whenPopulated_thenAddItems_emitsStates() {
    val originalContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val addedContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val finalContents = unionOf(originalContents, addedContents)
    val set = WritableObservableSet.create<Any>()
    originalContents.forEach(set::add)

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observables.states.skip(1).test()
    addedContents.forEach(set::add)
    testSubscriber.assertValueCount(addedContents.count)
      .assertValues { SetAlgorithms.intersectionOf(it, finalContents) == it }
      .assertThatValue(addedContents.count - 1).isEqualTo(finalContents)
  }

  @Test
  fun observeMutations_whenPopulated_thenRemoveItems_emitsMutations() {
    val finalContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val removedContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val originalContents = unionOf(finalContents, removedContents)
    val set = WritableObservableSet.create<Any>()
    originalContents.forEach(set::add)

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observables.mutations.skip(1).test()
    removedContents.forEach(set::remove)
    testSubscriber.assertValueCount(removedContents.count)
    testSubscriber.values.forEach { assertThat(it.operations).hasCount(1) }
    val operations: Set<out SetOperation<Any>> =
      testSubscriber.values.flatMap { it.operations }.toImmutableSet()
    operations.forEach {
      assertThat(it).isA(ObservableSet.RemoveFromSet::class)
    }
    val actualRemovedContents: Set<Any> = operations
      .map { (it as ObservableSet.RemoveFromSet<Any>).item }
      .toImmutableSet()
    assertThat(actualRemovedContents).containsExactlyElementsIn(removedContents)
  }

  @Test
  fun observeMutations_whenPopulated_thenCleared_emitsMutation() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach { set.add(it) }

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observables.mutations.skip(1).test()
    set.clear()
    testSubscriber.assertValueCount(1)
    testSubscriber.assertThatValue { it.operations }.hasCount(contents.count)
    val operations: Set<out SetOperation<Any>> = testSubscriber.values
      .flatMap { it.operations }
      .toImmutableSet()
    operations.forEach {
      assertThat(it).isA(ObservableSet.RemoveFromSet::class)
    }
    val actualRemovedContents: Set<Any> = operations
      .map { (it as ObservableSet.RemoveFromSet<Any>).item }
      .toImmutableSet()
    assertThat(actualRemovedContents).containsExactlyElementsIn(contents)
  }

  @Test
  fun observeStates_whenPopulated_thenCleared_emitsState() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(set::add)

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observables.states.skip(1).test()
    set.clear()
    testSubscriber.assertValueCount(1).assertThatValue().isEmpty()
  }

  @Test
  fun observeMutations_multipleSubscribers_eachReceivesIndependentMutations() {
    val contents1 = ImmutableSet.of(1, 2)
    val contents2 = contents1.toBuilder().add(3).build()
    val contents3 = contents2.toBuilder().add(4).build()

    val set = WritableObservableSet.create<Int>()
    val observable = set.observables.mutations
    set.addAll(contents1)

    val subscriber1 = observable.test()
    set.addAll(contents2)
    val subscriber2 = observable.test()
    set.addAll(contents3)
    val subscriber3 = observable.test()

    subscriber1.assertValueCount(3)
        .assertThatValue(0) { it.operations }
        .allMatch { it is ObservableSet.AddToSet<*> }
        .andThat { it.map { it as ObservableSet.AddToSet<Int> }.map { it.item }.toImmutableSet() }
        .isEqualTo(contents1)
    subscriber2.assertValueCount(2)
        .assertThatValue(0) { it.operations }
        .allMatch { it is ObservableSet.AddToSet<*> }
        .andThat { it.map { it as ObservableSet.AddToSet<Int> }.map { it.item }.toImmutableSet() }
        .isEqualTo(contents2)
    subscriber3.assertValueCount(1)
        .assertThatValue(0) { it.operations }
        .allMatch { it is ObservableSet.AddToSet<*> }
        .andThat { it.map { it as ObservableSet.AddToSet<Int> }.map { it.item }.toImmutableSet() }
        .isEqualTo(contents3)
  }
}