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
import java.io.File as JFile
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.asCompletable

actual class File private constructor(private val jFile: JFile) {

  init {
    if (!jFile.isFile) {
      throw NotAFileException(jFile.absolutePath)
    }
  }

  actual val name: String get() = jFile.absoluteFile.name

  actual val directory: Directory get() = Directory.fromJFile(jFile.parentFile)

  actual fun clearAndWriteLines(lines: Observable<String>): Completable {
    lateinit var writer: BufferedWriter

    return lines.doOnBeforeSubscribe { writer = BufferedWriter(FileWriter(jFile)) }
        .doOnBeforeNext {
          writer.write(it)
          writer.write(System.lineSeparator())
        }
        .doOnAfterFinally { writer.close() }
        .asCompletable()
  }

  actual fun readLines(): Observable<String> =
    observable<String> { emitter ->
      if (emitter.isDisposed)
        return@observable
      BufferedReader(FileReader(jFile)).use { reader ->
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
    actual fun fromPath(path: String) = File(JFile(path))

    fun fromJFile(jFile: JFile) = File(jFile)
  }
}