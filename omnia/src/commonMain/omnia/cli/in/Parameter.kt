package omnia.cli.`in`

abstract class Parameter protected constructor(
  val description: String, private val repeatable: Repeatable
) {

  val isRepeatable get() = repeatable == Repeatable.REPEATABLE

  enum class Repeatable {
    REPEATABLE, NOT_REPEATABLE
  }
}