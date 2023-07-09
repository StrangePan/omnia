package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import omnia.data.structure.immutable.ImmutableList

fun <T : Any> Observable<T>.collectIntoImmutableList(): Single<ImmutableList<T>> =
  this.collectInto(ImmutableList.Companion::builder, ImmutableList.Builder<T>::add, ImmutableList.Builder<T>::build)
