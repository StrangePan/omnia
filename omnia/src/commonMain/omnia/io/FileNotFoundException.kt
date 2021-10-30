package omnia.io

expect class FileNotFoundException: IOException {

  constructor(message: String)

  constructor(message: String, cause: Throwable)

  constructor(cause: Throwable)
}