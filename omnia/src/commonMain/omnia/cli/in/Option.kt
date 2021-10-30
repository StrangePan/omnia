package omnia.cli.`in`

abstract class Option protected constructor(
    val longName: String,
    val shortName: String? = null,
    val description: String,
    val expectsArgument: Boolean,
    private val repeatable: Parameter.Repeatable,
    val parameterRepresentation: String? = null) {

  val isRepeatable = repeatable == Parameter.Repeatable.REPEATABLE
}