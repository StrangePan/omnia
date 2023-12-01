package omnia.io.filesystem.os

import java.io.File as JavaFile
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.doOnAfterFinally
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observableUsing
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.NotAFileException
import omnia.io.filesystem.PathComponent
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent

actual class OsFile internal constructor(override val fileSystem: OsFileSystem, private var jFile: JavaFile):
    File, OsFileSystemObject {

  internal constructor(fileSystem: OsFileSystem, path: String) : this(fileSystem, JavaFile(path))

  init {
    if (!fileSystem.isFile(jFile)) {
      throw NotAFileException(jFile.absolutePath)
    }
  }

  actual override val name: PathComponent get() =
    jFile.absoluteFile.name.asPathComponent()

  actual override val fullPath: AbsolutePath get() =
    jFile.absolutePath.asAbsolutePath()

  actual override val directory: OsDirectory get() =
    OsDirectory(fileSystem, jFile.parentFile!!)

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
    observableUsing(
      resourceSupplier = { BufferedReader(FileReader(jFile)) },
      resourceCleanup = { it.close() },
      eager = true) { reader: BufferedReader ->
        observable<String> { emitter ->
          while (!emitter.isDisposed) {
            try {
              emitter.onNext(reader.readLine() ?: break)
            } catch (e: java.io.FileNotFoundException) {
              throw FileNotFoundException(e)
            } catch (e: java.io.IOException) {
              throw IOException(e)
            }
          }
          if (!emitter.isDisposed) {
            emitter.onComplete()
          }
        }
      }

  actual override fun delete() {
    if (!jFile.delete()) {
      throw IOException("Unable to delete file: $fullPath")
    }
  }

  actual override fun moveTo(path: AbsolutePath) {
    val newJFile = JavaFile(path.toString())
    if (jFile.renameTo(newJFile)) {
      this.jFile = newJFile
    } else {
      throw IOException("Unable to move file: $fullPath => $path")
    }
  }

  actual override fun copyTo(path: AbsolutePath): OsFile {
    try {
      return OsFile(fileSystem, jFile.copyTo(JavaFile(path.toString()), overwrite = false))
    } catch (e: kotlin.io.NoSuchFileException) {
      throw omnia.io.filesystem.FileNotFoundException(e)
    } catch (e: kotlin.io.FileAlreadyExistsException) {
      throw omnia.io.filesystem.FileAlreadyExistsException(path, e)
    } catch (e: java.io.IOException) {
      throw omnia.io.IOException(e)
    }
  }
}
