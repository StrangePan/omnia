package omnia.io.filesystem.sandbox

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.PathComponent

class SandboxFile internal constructor(private val fileSystem: SandboxFileSystem, private val baseFile: File): File {

  init {
    require(fileSystem.baseRootPath.contains(baseFile.directory.fullPath))
  }

  override val name: PathComponent get() =
    baseFile.name

  override val fullPath: AbsolutePath get() =
    fileSystem.toSandboxPath(baseFile.fullPath)

  override val directory: SandboxDirectory get() =
    SandboxDirectory(fileSystem, baseFile.directory)

  override fun clearAndWriteLines(lines: Observable<String>): Completable =
    baseFile.clearAndWriteLines(lines)

  override fun readLines(): Observable<String> =
    baseFile.readLines()
}
