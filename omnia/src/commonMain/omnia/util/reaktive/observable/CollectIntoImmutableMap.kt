package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.map
import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.tuple.Tuples.AtLeastCouple

fun <K : Any, V: Any> Observable<AtLeastCouple<out K, out V>>.collectIntoImmutableMap() =
  this.collectInto(
    { ImmutableMap.builder<K, V>() },
    { builder, couple -> builder.putMapping(couple.first, couple.second) })
    .map(ImmutableMap.Builder<K, V>::build)

fun <T : Any, K : Any, V: Any> Observable<T>.collectIntoImmutableMap(
  keyMapper: (T) -> K, valueMapper: (T) -> V) =
  this.collectInto(
    { ImmutableMap.builder<K, V>() },
    { builder, item -> builder.putMapping(keyMapper(item), valueMapper(item)) })
    .map(ImmutableMap.Builder<K, V>::build)