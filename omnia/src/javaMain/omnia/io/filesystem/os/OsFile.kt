package omnia.io.filesystem.os

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
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
import omnia.io.IOException
import omnia.io.filesystem.File
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.NotAFileException

actual class OsFile private constructor(private val jFile: java.io.File): File {

  init {
    if (!jFile.isFile) {
      throw NotAFileException(jFile.absolutePath)
    }
  }

  actual override val name: String get() = jFile.absoluteFile.name

  actual override val fullName: String get() = jFile.absolutePath

  actual override val directory: OsDirectory get() = OsDirectory.fromJFile(jFile.parentFile!!)

  actual override fun clearAndWriteLines(lines: Observable<String>): Completable {
    lateinit var writer: BufferedWriter

    return lines.doOnBeforeSubscribe { writer = BufferedWriter(FileWriter(jFile)) }
        .doOnBeforeNext {
          writer.write(it)
          writer.write(System.lineSeparator())
        }
        .doOnAfterFinally { writer.close() }
        .asCompletable()
  }

  actual override fun readLines(): Observable<String> =
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
    actual fun fromPath(path: String) = OsFile(java.io.File(path))

    fun fromJFile(jFile: java.io.File) = OsFile(jFile)

    actual fun fromResource(resource: String) =
      OsFile.fromPath(ClassLoader.getSystemResource(resource).file)
  }
}
