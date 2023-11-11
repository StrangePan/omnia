package omnia.io.filesystem

interface FileSystemObject {

  /**
   * The short name for the file system object. Does not include names of any directories. Cannot be
   * empty.
   */
  val name: PathComponent

  /**
   * The full canonical path of the file system object, including the names of parent directories.
   * Cannot be empty unless this object represents the root directory.
   */
  val fullPath: AbsolutePath

  /**
   * Attempts to delete this file system object. Beware that this may invalidate any lingering references to this
   * [FileSystemObject] held in the program.
   */
  fun delete()

  /**
   * Relocates this object to a new location. Beware that this may invalidate any lingering references to this
   * [FileSystemObject] held in the program.
   */
  fun moveTo(path: AbsolutePath)

  /**
   * Creates a copy of this object at a new location if the new path is not already occupied by a file system object.
   * Returns the newly created [FileSystemObject].
   */
  fun copyTo(path: AbsolutePath): FileSystemObject
}
