package omnia.io

interface FileSystemObject {

  /**
   * The short name for the file system object. Does not include names of any directories. Cannot be
   * empty.
   */
  val name: String

  /**
   * The full canonical path of the file system object, including the names of parent directories.
   * Cannot be empty.
   */
  val fullName: String
}