package omnia.io.filesystem.os

import omnia.io.filesystem.Directory

/** A representation of a directory in the operating system's filesystem. */
expect class OsDirectory: Directory {

  override val name: String

  override val fullName: String

  override val parentDirectory: OsDirectory?

  override val parentDirectories: Iterable<OsDirectory>

  override val files: Iterable<OsFile>

  override val subdirectories: Iterable<OsDirectory>

  override fun createFile(name: String): OsFile

  override fun createSubdirectory(name: String): OsDirectory

  companion object {
    /** The directory for the program's current working directory. */
    val workingDirectory: OsDirectory

    /** The root file system [OsDirectory]. */
    val rootDirectory: OsDirectory

    /** Looks for a directory with the given path in the operating system's filesystem. */
    fun fromPath(path: String): OsDirectory
  }
}
