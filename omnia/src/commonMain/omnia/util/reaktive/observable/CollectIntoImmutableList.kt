package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.map
import omnia.data.structure.immutable.ImmutableList

fun <T : Any> Observable<T>.collectIntoImmutableList() =
  this.collectInto(ImmutableList.Companion::builder, ImmutableList.Builder<T>::add)
    .map(ImmutableList.Builder<T>::build)