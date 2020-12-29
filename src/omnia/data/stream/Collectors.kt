package omnia.data.stream

import java.util.function.BiConsumer
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collector
import omnia.data.structure.List
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.mutable.ArrayList
import omnia.data.structure.mutable.HashMap
import omnia.data.structure.mutable.HashSet
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.tuple.Couple

/** Collection empty collectors for Omnia data structures that can be used with Java streams.  */
object Collectors {

  /** Creates a [Collector] that collects stream items into a [Set].  */
  @JvmStatic
  fun <E> toSet(): Collector<E, *, Set<E>> {
    return MaskingCollector(java.util.stream.Collectors.toSet(), Set.Companion::masking)
  }

  /** Creates a [Collector] that collects stream items into an [ImmutableSet].  */
  @JvmStatic
  fun <E> toImmutableSet(): Collector<E, *, ImmutableSet<E>> {
    return object : Collector<E, MutableSet<E>, ImmutableSet<E>> {
      override fun supplier(): Supplier<MutableSet<E>> {
        return Supplier { HashSet.create() }
      }

      override fun accumulator(): BiConsumer<MutableSet<E>, E> {
        return BiConsumer { obj: MutableSet<E>, element: E -> obj.add(element) }
      }

      override fun combiner(): BinaryOperator<MutableSet<E>> {
        return BinaryOperator { set1: MutableSet<E>, set2: MutableSet<E> ->
          HashSet.copyOf(
            ImmutableSet.builder<E>().addAll(set1).addAll(set2).build()
          )
        }
      }

      override fun finisher(): Function<MutableSet<E>, ImmutableSet<E>> {
        return Function { iterable: MutableSet<E> -> ImmutableSet.copyOf(iterable) }
      }

      override fun characteristics(): kotlin.collections.MutableSet<Collector.Characteristics> {
        return mutableSetOf(Collector.Characteristics.UNORDERED)
      }
    }
  }

  @JvmStatic
  fun <E> toList(): Collector<E, *, List<E>> {
    return MaskingCollector(java.util.stream.Collectors.toList(), List.Companion::masking)
  }

  @JvmStatic
  fun <E> toImmutableList(): Collector<E, *, ImmutableList<E>> {
    return object : Collector<E, ArrayList<E>, ImmutableList<E>> {
      override fun supplier(): Supplier<ArrayList<E>> {
        return Supplier { ArrayList.create() }
      }

      override fun accumulator(): BiConsumer<ArrayList<E>, E> {
        return BiConsumer { obj: ArrayList<E>, element: E -> obj.add(element) }
      }

      override fun combiner(): BinaryOperator<ArrayList<E>> {
        return BinaryOperator { list1: ArrayList<E>, list2: ArrayList<E> ->
          ArrayList.copyOf(
            ImmutableList.builder<E>().addAll(list1).addAll(list2).build()
          )
        }
      }

      override fun finisher(): Function<ArrayList<E>, ImmutableList<E>> {
        return Function { iterable: ArrayList<E> -> ImmutableList.copyOf(iterable) }
      }

      override fun characteristics(): kotlin.collections.MutableSet<Collector.Characteristics> {
        return mutableSetOf()
      }
    }
  }

  /**
   * Collects a stream of [Couple]s into an [ImmutableMap] where the keys are derived
   * from [Couple.first] and the values are derived from [Couple.second]. It is
   * undefined how duplicate keys are handled.
   *
   * @param K the first type of the [Couple] and the key type for the map
   * @param V the second type of the [Couple] and the value type for the map
   */
  @JvmStatic
  fun <K, V> toImmutableMap(): Collector<Couple<out K, out V>, *, ImmutableMap<K, V>> {
    return toImmutableMap({ obj -> obj.first() }, { obj -> obj.second() })
  }

  /**
   * Collects a stream's contents into an ImmutableMap using the provided function to derive a key
   * for each stream item. It is undefined how duplicate keys are handled. Each stream item is its
   * value.
   *
   * @param keyExtractor mapping function that produces a key for each entry
   * @param E the item type of the stream and the value type of the produced map
   * @param K the key type for the map
   */
  @JvmStatic
  fun <E, K> toImmutableMap(
    keyExtractor: Function<in E, K>,
  ): Collector<E, *, ImmutableMap<K, E>> {
    return toImmutableMap(keyExtractor, Function.identity())
  }

  /**
   * Collects the stream contents into an ImmutableMap using the provided functions to derive a key
   * and value for each stream item. It is undefined how duplicate keys will be handled.
   *
   * @param keyExtractor mapping function that produces a key for each entry
   * @param valueExtractor mapping function that produces a value for each entry
   * @param E the item type of the stream
   * @param K the key type for the map
   * @param V the value type for the map
   */
  @JvmStatic
  fun <E, K, V> toImmutableMap(
    keyExtractor: Function<in E, K>,
    valueExtractor: Function<in E, V>,
  ): Collector<E, *, ImmutableMap<K, V>> {
    return groupBy(keyExtractor, valueExtractor, ImmutableMap.Companion::copyOf)
  }

  /**
   * Collects the stream contents into a HashMap using the provided functions to derive the key and
   * value for each stream item. It is undefined how duplicate keys will be handled.
   *
   * @param keyExtractor mapping function that produces a key for each entry
   * @param valueExtractor mapping function that produces a value for each entry
   * @param E the item type of the stream
   * @param K the key type for the map
   * @param V the value type for the map
   */
  @JvmStatic
  fun <E, K, V> toHashMap(
    keyExtractor: Function<in E, K>, valueExtractor: Function<in E, V>,
  ): Collector<E, *, HashMap<K, V>> {
    return groupBy(keyExtractor, valueExtractor, Function.identity())
  }

  private fun <E, K, V, R> groupBy(
    keyExtractor: Function<in E, K>,
    valueExtractor: Function<in E, V>,
    finisher: Function<HashMap<K, V>, R>,
  ): Collector<E, *, R> {
    return object : Collector<E, HashMap<K, V>, R> {
      override fun supplier(): Supplier<HashMap<K, V>> {
        return Supplier { HashMap.create() }
      }

      override fun accumulator(): BiConsumer<HashMap<K, V>, E> {
        return BiConsumer { map: HashMap<K, V>, item: E ->
          map.putMapping(
            keyExtractor.apply(item),
            valueExtractor.apply(item)
          )
        }
      }

      override fun combiner(): BinaryOperator<HashMap<K, V>> {
        return BinaryOperator { map1: HashMap<K, V>, map2: HashMap<K, V> ->
          HashMap.copyOf(ImmutableMap.builder<K, V>().putAll(map1).putAll(map2).build())
        }
      }

      override fun finisher(): Function<HashMap<K, V>, R> {
        return finisher
      }

      override fun characteristics(): kotlin.collections.MutableSet<Collector.Characteristics> {
        return mutableSetOf(Collector.Characteristics.UNORDERED)
      }
    }
  }

  /**
   * A simple forwarding [Collector] that applies a custom finisher on top the finisher
   * provided by the given target [Collector].
   *
   * @param T the type empty item in the stream
   * @param A the mutable data structure type empty the target collector `R1`
   * @param R1 the return type empty the target collector
   * @param R2 the return type empty this collector
   * @param maskedCollector the collector to delegate calls to before finishing
   * @param finisher the finisher function that transforms the final result from [maskedCollector]
   */
  internal class MaskingCollector<T, A, R1, R2>(
    private val maskedCollector: Collector<T, A, R1>,
    private val finisher: Function<in R1, out R2>,
  ) : Collector<T, A, R2> {

    override fun supplier(): Supplier<A> {
      return maskedCollector.supplier()
    }

    override fun accumulator(): BiConsumer<A, T> {
      return maskedCollector.accumulator()
    }

    override fun combiner(): BinaryOperator<A> {
      return maskedCollector.combiner()
    }

    override fun finisher(): Function<A, R2> {
      return Function { a -> finisher.apply(maskedCollector.finisher().apply(a)) }
    }

    override fun characteristics(): kotlin.collections.MutableSet<Collector.Characteristics> {
      val characteristics: kotlin.collections.MutableSet<Collector.Characteristics> =
        java.util.HashSet(maskedCollector.characteristics())
      characteristics.remove(Collector.Characteristics.IDENTITY_FINISH)
      return characteristics
    }
  }
}