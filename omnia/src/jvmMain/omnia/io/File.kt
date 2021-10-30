package omnia.io

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.doOnAfterFinally
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observableOfError
import com.badoo.reaktive.observable.onErrorResumeNext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter

actual class File private constructor(private val path: String) {

  actual fun clearAndWriteLines(lines: Observable<String>): Observable<String> {
    lateinit var writer: BufferedWriter

    return lines.doOnBeforeSubscribe { writer = BufferedWriter(FileWriter(path)) }
      .doOnBeforeNext {
        writer.write(it)
        writer.write(System.lineSeparator())
      }
      .doOnAfterFinally { writer.close() }
  }

  actual fun readLines(): Observable<String> =
    observable<String> { emitter ->
      if (emitter.isDisposed)
        return@observable
      BufferedReader(FileReader(path)).use { reader ->
        while (!emitter.isDisposed) {
          emitter.onNext(reader.readLine() ?: break)
        }
        if (!emitter.isDisposed) {
          emitter.onComplete()
        }
      }
    }
      .onErrorResumeNext { error ->
        when (error) {
          is java.io.FileNotFoundException -> observableOfError(FileNotFoundException(cause = error))
          is java.io.IOException -> observableOfError(IOException(cause = error))
          else -> observableOfError(error)
        }
      }

  actual companion object {
    actual fun fromPath(path: String) = File(path)
  }
}