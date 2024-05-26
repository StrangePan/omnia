package omnia.io.filesystem.os

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.PathComponent

/** A representation of a directory in the operating system's filesystem. */
expect class OsDirectory: Directory, OsFileSystemObject {

  override val fileSystem: OsFileSystem

  override val name: PathComponent

  override val fullPath: AbsolutePath

  override val parentDirectory: OsDirectory?

  override val parentDirectories: Iterable<OsDirectory>

  override val contents: Iterable<OsFileSystemObject>

  override val files: Iterable<OsFile>

  override val subdirectories: Iterable<OsDirectory>

  override fun createFile(name: PathComponent): OsFile

  override fun createSubdirectory(name: PathComponent): OsDirectory

  override fun delete()

  override fun moveTo(path: AbsolutePath)

  override fun copyTo(path: AbsolutePath): OsDirectory
}
