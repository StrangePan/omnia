package omnia.util.reaktive.completable

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableOfError
import com.badoo.reaktive.completable.completableOfEmpty
import com.badoo.reaktive.completable.onErrorResumeNext

fun Completable.onErrorComplete(predicate: (Throwable) -> Boolean) =
  this.onErrorResumeNext {
    throwable -> if (predicate(throwable)) completableOfEmpty() else completableOfError(throwable)
  }
