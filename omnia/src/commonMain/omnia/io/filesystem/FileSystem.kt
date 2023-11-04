package omnia.io.filesystem

// TODO flesh out the documentation to include exceptions
/** A representation of a filesystem. Can be used to open, create, and search for files and directories. */
interface FileSystem {

  /** The root [Directory] of the file system. */
  val rootDirectory: Directory

  /** The application's current working [Directory] within the file system. */
  val workingDirectory: Directory

  /** Checks if a file system object exists at the given path. */
  fun objectExistsAt(path: AbsolutePath): Boolean

  /** Checks if a file system object exists at the given path, and if that object is a directory. */
  fun isDirectory(path: AbsolutePath): Boolean

  /** Checks if a file system object exists at the given path, and if that object is a file. */
  fun isFile(path: AbsolutePath): Boolean

  /** Searches the file system for a file system object at the given path and returns it. */
  fun getObjectAt(path: AbsolutePath): FileSystemObject

  /** Searches the file system for a directory at the given path and returns it, or null if it doesn't exist. */
  fun getDirectory(path: AbsolutePath): Directory

  /** Searches the file system for a file at the given path and returns it, or null if it doesn't exist. */
  fun getFile(path: AbsolutePath): File

  /**
   * Create a directory at the given path if the parent directory already exists. If the directory already exists,
   * returns the existing directory instead of creating a new one.
   */
  fun createDirectory(path: AbsolutePath): Directory

  /**
   * Create a regular file at the given path if the parent directory already exists. If the file already exists,
   * returns the existing file instead of creating a new one.
   */
  fun createFile(path: AbsolutePath): File
}
