package omnia.io.filesystem

import omnia.io.IOException

class FileAlreadyExistsException: IOException {

  constructor(file: FileSystemObject): super(file.fullPath.toString())

  constructor(file: FileSystemObject, message: String): super(msg(file, message))

  constructor(file: FileSystemObject, message: String, cause: Throwable): super(msg(file, message), cause)

  constructor(file: FileSystemObject, cause: Throwable): super(file.fullPath.toString(), cause)

  constructor(path: AbsolutePath, cause: Throwable): super(path.toString(), cause)

  companion object {
    fun msg(file: FileSystemObject, message: String) =
      message + ":\n" + file.fullPath.toString()
  }
}
