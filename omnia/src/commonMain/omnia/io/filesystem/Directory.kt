package omnia.io.filesystem

/** A representation of a filesystem directory. Can be used to navigate the file system. */
interface Directory: FileSystemObject {

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

  /**
   * Iterable for all `FileSystemObjects` contained within this directory. May be empty. Does not recurse into
   * subdirectories.
   */
  val contents: Iterable<FileSystemObject>

  /** Iterable for all non-directory files in the current directory. May be empty. */
  val files: Iterable<File>

  /**
   * Iterable that iterates over the directories contained in the current directory. May be empty.
   * Does not recurse further into the subdirectories of these subdirectories.
   */
  val subdirectories: Iterable<Directory>

  /**
   * Creates a new file with the given name within the current directory and returns the newly created file.
   */
  fun createFile(name: PathComponent): File

  /**
   * Attempts to create a new subdirectory within the current directory and returns the newly created directory.
   */
  fun createSubdirectory(name: PathComponent): Directory

  /**
   * Deletes this directory from the filesystem along with all subdirectories and files contained within. Note that the
   * root directory and the working directory cannot be deleted.
   * @see FileSystemObject.delete
   */
  override fun delete()

  /**
   * Moves this directory to a new location if that new location is not already occupied. Note that the root directory
   * and the working directory cannot be moved.
   * @see FileSystemObject.moveTo
   */
  override fun moveTo(path: AbsolutePath)

  /**
   * Creates a copy of this directory at a new location if that location is not already occupied.
   * @see FileSystemObject.copyTo
   */
  override fun copyTo(path: AbsolutePath): Directory
}

// TODO These extension functions are not good enough. Replace with a more comprehensive, specializable API.
fun Directory.getOrCreateFile(name: PathComponent): File =
  this.getFile(name) ?: this.createFile(name)

fun Directory.getFile(name: PathComponent): File? =
  this.files.firstOrNull { it.name == name }

fun Directory.getOrCreateSubdirectory(name: PathComponent): Directory =
  this.getSubdirectory(name) ?: this.createSubdirectory(name)

fun Directory.getSubdirectory(name: PathComponent): Directory? =
  this.subdirectories.firstOrNull { it.name == name }
