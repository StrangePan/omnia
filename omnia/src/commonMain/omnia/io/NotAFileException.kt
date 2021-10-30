package omnia.io

class NotAFileException: IOException {

  constructor(message: String): super(message)

  constructor(message: String, cause: Throwable): super(message, cause)

  constructor(cause: Throwable): super(cause)
}