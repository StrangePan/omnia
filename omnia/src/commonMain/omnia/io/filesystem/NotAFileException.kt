package omnia.io.filesystem

import omnia.io.IOException

class NotAFileException: IOException {

  constructor(message: String): super(message)

  constructor(message: String, cause: Throwable): super(message, cause)

  constructor(cause: Throwable): super(cause)
}
