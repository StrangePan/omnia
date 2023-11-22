package omnia.io.filesystem

import omnia.io.IOException

class FileAlreadyExistsException: IOException {

  val path: AbsolutePath

  constructor(file: FileSystemObject): super(file.fullPath.toString()) {
    this.path = file.fullPath
  }

  constructor(file: FileSystemObject, message: String): super(msg(file, message)) {
    this.path = file.fullPath
  }

  constructor(file: FileSystemObject, message: String, cause: Throwable): super(msg(file, message), cause) {
    this.path = file.fullPath
  }

  constructor(file: FileSystemObject, cause: Throwable): super(file.fullPath.toString(), cause) {
    this.path = file.fullPath
  }

  constructor(path: AbsolutePath, cause: Throwable): super(path.toString(), cause) {
    this.path = path
  }

  companion object {
    fun msg(file: FileSystemObject, message: String) =
      message + ":\n" + file.fullPath.toString()
  }
}
