package omnia.io.filesystem

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable

/** An interface for reading from and writing to filesystem files.  */
interface File: FileSystemObject {

  /** The [Directory] containing this file. */
  val directory: Directory

  /**
   * Returns a [Completable] that, when first subscribed to, clears this file's contents, subscribes to the
   * provided observable, writes the values it emits to the file followed by a newline after each value.
   */
  fun clearAndWriteLines(lines: Observable<String>): Completable

  /**
   * Returns an [Observable] that, when first subscribed to, opens the underlying file and emits each line
   * contained within until the end of the file.
   */
  fun readLines(): Observable<String>

  override fun copyTo(path: AbsolutePath): File
}
