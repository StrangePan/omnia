package omnia.io

actual open class IOException : RuntimeException {

  actual constructor(message: String): super(message)

  actual constructor(message: String, cause: Throwable): super(message, cause)

  actual constructor(cause: Throwable): super(cause)
}