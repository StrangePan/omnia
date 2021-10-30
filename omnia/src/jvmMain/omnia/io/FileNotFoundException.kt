package omnia.io

actual class FileNotFoundException : IOException {

  actual constructor(message: String): super(message)

  actual constructor(message: String, cause: Throwable): super(message, cause)

  actual constructor(cause: Throwable): super(cause)
}