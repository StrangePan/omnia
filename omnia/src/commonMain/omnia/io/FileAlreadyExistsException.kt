package omnia.io

class FileAlreadyExistsException: IOException {

  constructor(file: FileSystemObject): super(file.fullName)

  constructor(file: FileSystemObject, message: String): super(msg(file, message))

  constructor(file: FileSystemObject, message: String, cause: Throwable): super(msg(file, message), cause)

  constructor(file: FileSystemObject, cause: Throwable): super(file.fullName, cause)

  companion object {
    fun msg(file: FileSystemObject, message: String) = message + ":\n" + file.fullName
  }
}