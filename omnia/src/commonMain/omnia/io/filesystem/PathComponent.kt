package omnia.io.filesystem

/** Immutable wrapper that automatically validates components of a path and checks for illegal names or characters. */
// TODO tests!
data class PathComponent internal constructor (val name: String) {

  init {
    illegalNames.forEach { illegalName ->
      if (name == illegalName) {
        throw PathParseException("\"$illegalName\" is an illegal path component name.")
      }
    }
    illegalCharacters.forEach { illegalCharacter ->
      if (name.contains(illegalCharacter)) {
        throw PathParseException("File paths cannot contain '$illegalCharacter'.")
      }
    }
  }

  companion object {
    private val illegalNames = listOf(".", "..", "")
    private val illegalCharacters = listOf("/")
  }

  override fun toString() = name
}
