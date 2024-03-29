package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.tuple.Tuples.AtLeastCouple

fun <K : Any, V: Any> Observable<AtLeastCouple<out K, out V>>.collectIntoImmutableMap(): Single<ImmutableMap<K, V>> =
  this.collectInto(
    ImmutableMap.Companion::builder,
    { builder, couple -> builder.putMapping(couple.first, couple.second) },
    ImmutableMap.Builder<K, V>::build)

fun <T : Any, K : Any, V: Any> Observable<T>.collectIntoImmutableMap(keyMapper: (T) -> K, valueMapper: (T) -> V):
  Single<ImmutableMap<K, V>> =
  this.collectInto(
    ImmutableMap.Companion::builder,
    { builder, item -> builder.putMapping(keyMapper(item), valueMapper(item)) },
    ImmutableMap.Builder<K, V>::build)
