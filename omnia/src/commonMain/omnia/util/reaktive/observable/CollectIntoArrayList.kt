package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import omnia.data.structure.mutable.ArrayList

fun <T : Any> Observable<T>.collectIntoArrayList(): Single<ArrayList<T>> =
  this.collectInto(ArrayList.Companion::create) { list, item -> list.also { it.add(item) } }
