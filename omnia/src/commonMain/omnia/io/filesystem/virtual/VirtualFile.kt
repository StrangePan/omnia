package omnia.io.filesystem.virtual

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import omnia.data.structure.mutable.ArrayList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.PathComponent

/**
 * An in-memory virtual file object. Useful for tests, intermediate migration steps, and other situations where
 * we want to run file-manipulation algorithms without accessing the operating system's storage.
 */
class VirtualFile internal constructor(private val fileSystem: VirtualFileSystem, override val fullPath: AbsolutePath):
  VirtualFileSystemObject, File {

    init {
      require(!fullPath.isRoot)
    }

  private val lines = ArrayList.create<String>()

  override val name: PathComponent get() =
    fullPath.components.last()

  override val directory: VirtualDirectory get() =
    fileSystem.getDirectory(fullPath - 1)

  override fun clearAndWriteLines(lines: Observable<String>): Completable =
    lines.doOnBeforeSubscribe { this.lines.clear() }
      .doOnAfterNext { this.lines.add(it) }
      .asCompletable()

  override fun readLines(): Observable<String> =
    this.lines.asObservable()

  override fun toString() =
    "VirtualFile@$fullPath"

  override fun equals(other: Any?) =
    other is VirtualFile && this.fileSystem === other.fileSystem && this.fullPath == other.fullPath

  override fun hashCode() =
    fullPath.hashCode()
}
