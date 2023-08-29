package omnia.io.filesystem.virtual

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import omnia.data.structure.mutable.ArrayList
import omnia.io.filesystem.File

/**
 * An in-memory virtual file object. Useful for tests, intermediate migration steps, and other situations where
 * we want to run file-manipulation algorithms without accessing the operating system's storage.
 */
class VirtualFile internal constructor(private val fileSystem: VirtualFileSystem, private val path: String):
  VirtualFileSystemObject, File {

  private val lines = ArrayList.create<String>()

  override val name: String get() =
    path.substringAfterLast("/")

  override val fullName: String get() =
    path

  override val directory: VirtualDirectory get() =
    path.substringBeforeLast("/", missingDelimiterValue = "/")
      .ifEmpty { "/" }
      .let(fileSystem::getDirectory)

  override fun clearAndWriteLines(lines: Observable<String>): Completable =
    lines.doOnBeforeSubscribe { this.lines.clear() }
      .doOnAfterNext { this.lines.add(it) }
      .asCompletable()

  override fun readLines(): Observable<String> =
    this.lines.asObservable()

  override fun toString() =
    "VirtualFile@$fullName"

  override fun equals(other: Any?) =
    other is VirtualFile && this.fileSystem === other.fileSystem && this.fullName == other.fullName

  override fun hashCode() =
    fullName.hashCode()
}
