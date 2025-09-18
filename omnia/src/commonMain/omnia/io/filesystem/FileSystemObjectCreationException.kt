package omnia.io.filesystem

import omnia.io.IOException

open class FileSystemObjectCreationException: IOException {

  val path: AbsolutePath

  constructor(path: AbsolutePath, message: String): super(msg(path, message)) {
    this.path = path
  }

  constructor(path: AbsolutePath, message: String, cause: Throwable): super(msg(path, message), cause) {
    this.path = path
  }

  companion object Companion {
    fun msg(path: AbsolutePath, message: String) = "$message:\n $path"
  }
}
