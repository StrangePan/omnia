package omnia.io

class FileNotFoundException: IOException {

  constructor(message: String): super(message)

  constructor(message: String, cause: Throwable): super(message, cause)

  constructor(cause: Throwable): super(cause)
}