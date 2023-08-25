package omnia.io.filesystem

// TODO flesh out the documentation to include exceptions
/** A representation of a filesystem. Can be used to open, create, and search for files and directories. */
interface FileSystem {

  /** The root [Directory] of the file system. */
  val rootDirectory: Directory

  /** The application's current working [Directory] within the file system. */
  val workingDirectory: Directory

  /** Searches the file system for a directory at the given path and returns it, or null if it doesn't exist. */
  fun getDirectory(path: String): Directory?

  /** Searches the file system for a file at the given path and returns it, or null if it doesn't exist. */
  fun getFile(path: String): File?

  /**
   * Create a directory at the given path if the parent directory already exists. If the directory already exists,
   * returns the existing directory instead of creating a new one.
   */
  fun createDirectory(path: String): Directory

  /**
   * Create a regular file at the given path if the parent directory already exists. If the file already exists,
   * returns the existing file instead of creating a new one.
   */
  fun createFile(path: String): File

  /** Creates a directory and all missing intermediate parent directories. */
  fun createDirectoryAndParentDirectories(path: String): Directory

  /** Creates a regular file at the given path, and all missing intermediate parent directories. */
  fun createFileAndParentDirectories(path: String): File
}
