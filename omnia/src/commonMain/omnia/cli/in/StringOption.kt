package omnia.cli.`in`

open class StringOption(
    longName: String,
    shortName: String?,
    description: String,
    repeatable: Parameter.Repeatable,
    semanticDescription: String? = null)
  : Option(longName, shortName, description, true, repeatable, semanticDescription)