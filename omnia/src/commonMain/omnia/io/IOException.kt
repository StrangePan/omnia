package omnia.io

expect open class IOException: RuntimeException {

  constructor(message: String)

  constructor(message: String, cause: Throwable)

  constructor(cause: Throwable)
}