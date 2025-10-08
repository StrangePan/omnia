package omnia.io.filesystem

class FileCreationException: FileSystemObjectCreationException {

  constructor(path: AbsolutePath): this(path, "Failed to create file at")

  constructor(path: AbsolutePath, cause: Throwable): this(path, "Failed to create file at", cause)

  constructor(path: AbsolutePath, message: String): super(path, message)

  constructor(path: AbsolutePath, message: String, cause: Throwable): super(path, message, cause)
}
