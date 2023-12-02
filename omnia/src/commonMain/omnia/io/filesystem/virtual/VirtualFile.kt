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
class VirtualFile internal constructor(
  override val fileSystem: VirtualFileSystem,
  internal var fullPathMutable: AbsolutePath,
  internal val lines: ArrayList<String> = ArrayList.create()):
  VirtualFileSystemObject, File {

  init {
    require(!fullPath.isRoot)
  }

  override val fullPath: AbsolutePath get() = this.fullPathMutable

  override val name: PathComponent get() =
    fullPath.components.last()

  override val directory: VirtualDirectory get() =
    fileSystem.getDirectoryAt(fullPath - 1)

  override fun clearAndWriteLines(lines: Observable<String>): Completable =
    lines
      .doOnBeforeSubscribe { require(this.fileSystem.getFileAt(fullPath) == this) }
      .doOnBeforeSubscribe { this.lines.clear() }
      .doOnAfterNext { this.lines.add(it) }
      .asCompletable()

  override fun readLines(): Observable<String> =
    this.lines.asObservable()

  override fun toString() =
    "VirtualFile@$fullPath"

  override fun equals(other: Any?) =
    other === this
      || other is VirtualFile
      && other.fullPath == this.fullPath
      && other.lines == this.lines

  override fun hashCode() =
    fullPath.hashCode()

  override fun delete() {
    require(fileSystem.getFileAt(fullPath) == this)
    fileSystem.tree.deleteFile(fullPath)
  }

  override fun moveTo(path: AbsolutePath) {
    require(fileSystem.getFileAt(fullPath) == this)
    fileSystem.tree.moveFile(fullPath, path)
    this.fullPathMutable = path
  }

  override fun copyTo(path: AbsolutePath): VirtualFile {
    require(fileSystem.getFileAt(fullPath) == this)
    return fileSystem.tree.copyFile(fullPath, path)
  }
}
