package omnia.io.filesystem

class PathParseException: RuntimeException {

  constructor(message: String, cause: Throwable): super(message, cause)
  constructor(message: String): super(message)
}
