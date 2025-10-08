package omnia.io.filesystem.resources

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.PathComponent

/** A representation of a directory in the application's resource package. */
expect class ResourceDirectory: Directory, ResourceFileSystemObject {

  override val fileSystem: ResourceFileSystem

  override val name: PathComponent

  override val fullPath: AbsolutePath

  override val parentDirectory: ResourceDirectory?

  override val parentDirectories: Iterable<ResourceDirectory>

  override val contents: Iterable<ResourceFileSystemObject>

  override val files: Iterable<ResourceFile>

  override val subdirectories: Iterable<ResourceDirectory>

  override fun createFile(name: PathComponent): ResourceFile

  override fun createSubdirectory(name: PathComponent): ResourceDirectory

  override fun delete()

  override fun moveTo(path: AbsolutePath)

  override fun copyTo(path: AbsolutePath): ResourceDirectory
}
