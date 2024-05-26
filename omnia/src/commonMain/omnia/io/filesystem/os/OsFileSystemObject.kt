package omnia.io.filesystem.os

import omnia.io.filesystem.FileSystemObject

interface OsFileSystemObject: FileSystemObject {

  override val fileSystem: OsFileSystem
}
