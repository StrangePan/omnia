package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.map
import omnia.data.structure.immutable.ImmutableSet

fun <T : Any> Observable<T>.collectIntoImmutableSet() =
  this.collectInto(ImmutableSet.Companion::builder, ImmutableSet.Builder<T>::add)
    .map(ImmutableSet.Builder<T>::build)