package omnia.data.structure.observable

import com.badoo.reaktive.observable.flatMapIterable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.skip
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.algorithm.SetAlgorithms
import omnia.algorithm.SetAlgorithms.unionOf
import omnia.data.structure.Set
import omnia.data.structure.Set.Companion.areEqual
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.observable.ObservableSet.SetOperation
import omnia.data.structure.observable.writable.WritableObservableSet
import omnia.util.reaktive.observable.test.assertValue
import omnia.util.reaktive.observable.test.assertValueCount
import omnia.util.reaktive.observable.test.assertValues
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.containsExactlyElementsIn
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isA
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
    val `object` = Any()
    set.add(`object`)
    set.remove(`object`)
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
    set.observables.states.test().assertValue { !it.isPopulated }
  }

  @Test
  fun observeMutations_whenEmpty_emitsEmpty() {
    val set: ObservableSet<Any> = WritableObservableSet.create()
    val subscriber = set.observables.mutations.test()
    subscriber.assertValue { !it.state.isPopulated }
    subscriber.assertValue { !it.operations.isPopulated }
  }

  @Test
  fun observeStates_whenPopulated_emitsValues() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(set::add)
    set.observables.states.test().assertValue { areEqual(it, contents) }
  }

  @Test
  fun observeMutations_whenPopulated_emitsValues() {
    val contents: Set<Any> = ImmutableSet.of(Any(), Any(), Any())
    val set = WritableObservableSet.create<Any>()
    contents.forEach(set::add)
    val testSubscriber = set.observables.mutations.map { it.state }.test()
    testSubscriber.assertValue { areEqual(it, contents) }
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
    val testSubscriber = set.observables.states.skip(1).test(autoFreeze = false)
    addedContents.forEach(set::add)
    testSubscriber.assertValueCount(addedContents.count)
      .assertValues { SetAlgorithms.intersectionOf(it, finalContents) == it }
      .assertValue(addedContents.count - 1) { it == finalContents }
  }

  @Test
  fun observeMutations_whenPopulated_thenRemoveItems_emitsMutations() {
    val finalContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val removedContents: Set<Any> = ImmutableSet.of(Any(), Any())
    val originalContents = unionOf(finalContents, removedContents)
    val set = WritableObservableSet.create<Any>()
    originalContents.forEach(set::add)

    // Skip 1 because first emission is the initialization subscription
    val testSubscriber = set.observables.mutations.skip(1).test(autoFreeze = false)
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
    val testSubscriber = set.observables.mutations.skip(1).test(autoFreeze = false)
    set.clear()
    testSubscriber.assertValueCount(1)
    testSubscriber.assertValue { it.operations.count == contents.count }
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
    val testSubscriber = set.observables.states.skip(1).test(autoFreeze = false)
    set.clear()
    testSubscriber.assertValueCount(1)
    testSubscriber.assertValue { areEqual(it, ImmutableSet.empty<Any>()) }
  }
}