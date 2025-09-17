package omnia.io.filesystem.resources

import omnia.io.filesystem.FileSystemObject

interface ResourceFileSystemObject: FileSystemObject {

  override val fileSystem: ResourceFileSystem
}
