package omnia.io

/** A representation of a filesystem directory. Can be used to navigate the file system. */
expect class Directory: FileSystemObject {

  override val name: String

  override val fullName: String

  /**
   * Returns the parent [Directory] of this directory, or `null` if this is the root directory of
   * the file system.
   */
  val parentDirectory: Directory?

  /**
   * Iterates over all the parent directories of the current directory, starting at the immediate
   * parent directory and ending at the root file system directory.
   *
   * Handling of symlinks is undefined.
   */
  val parentDirectories: Iterable<Directory>

  /** Iterable for all non-directory files in the current directory. May be empty. */
  val files: Iterable<File>

  /**
   * Iterable that iterates over the directories contained in the current directory. May be empty.
   * Does not recurse further into the subdirectories of these subdirectories.
   */
  val subdirectories: Iterable<Directory>

  fun createFile(name: String): File

  companion object {
    /** The directory for the programs current working directory. */
    val workingDirectory: Directory

    /** The root file system directory. */
    val rootDirectory: Directory

    /** Looks up a file system directory with the given path. */
    fun fromPath(path: String): Directory
  }
}