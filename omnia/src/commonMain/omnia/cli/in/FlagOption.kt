package omnia.cli.`in`

class FlagOption(
  longName: String,
  shortName: String? = null,
  description: String,
  repeatable: Parameter.Repeatable
) : Option(longName, shortName, description, false, repeatable)