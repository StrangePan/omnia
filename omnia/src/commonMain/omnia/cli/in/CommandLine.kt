package omnia.cli.`in`

import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.immutable.ImmutableMap.Companion.toImmutableMap
import omnia.data.structure.mutable.MutableMap
import omnia.data.structure.mutable.HashMap
import omnia.data.structure.mutable.MutableList
import omnia.data.structure.mutable.ArrayList
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

class CommandLine(
  val argList: ImmutableList<String>,
  private val options: ImmutableMap<String, ImmutableList<String>>
) {
  companion object {

    private const val SHORT_OPTION_PATTERN = "^-(\\w)$"
    private const val LONG_OPTION_PATTERN = "^--(\\w(?:\\w|-)*)$"
    private const val OPTION_ARG_PAIR_PATTERN = "^--(\\w(?:\\w|-)*)=(.*)$"

    @Throws(ParserException::class)
    fun parse(cliArgs: List<out String>, options: Options): CommandLine {

      val parsedOptions: MutableMap<Option, MutableList<String>> = HashMap.create()
      val parsedArgs: MutableList<String> = ArrayList.create()
      var pendingOption: Couple<Option, String>? = null

      parsingLoop@ for (arg in cliArgs) {
        if (pendingOption != null) {
          parsedOptions.valueOf(pendingOption.first)!!.add(arg)
          pendingOption = null
          continue@parsingLoop
        }

        val matchResult =
          Regex("$SHORT_OPTION_PATTERN|$LONG_OPTION_PATTERN|$OPTION_ARG_PAIR_PATTERN")
            .matchEntire(arg)

        if (matchResult != null) {
          val optionName = matchResult.groupValues[1]
            .ifEmpty { matchResult.groupValues[2] }
            .ifEmpty { matchResult.groupValues[3] }
          val option = options.options.valueOf(optionName)
            ?: throw ParserException("Unrecognized option '$optionName'")

          if (!option.isRepeatable && parsedOptions.keys.contains(option)) {
            throw ParserException("Option '$optionName' specified more than once")
          }

          parsedOptions.putMappingIfAbsent(option, ArrayList.Companion::create)
          val optionValue = matchResult.groupValues[4]
          if (optionValue.isNotEmpty()) {
            if (!option.expectsArgument) {
              throw ParserException("Option '$optionName' expects no arguments")
            }
            parsedOptions.valueOf(option)!!.add(optionValue)
          } else if (option.expectsArgument) {
            pendingOption = Tuple.of(option, optionName)
          }

          continue@parsingLoop
        }

        // is just a regular arg
        parsedArgs.add(arg)
      }

      if (pendingOption != null) {
        throw ParserException("No argument provided for option '${pendingOption.second}'")
      }

      return CommandLine(
        ImmutableList.copyOf(parsedArgs),
        parsedOptions.entries
          .map { entry -> Tuple.of(entry.key, entry.value.toImmutableList()) }
          .flatMap { couple ->
            listOfNotNull(
              couple.mapFirst(Option::longName),
              couple.first.shortName?.let { couple.mapFirst { option -> option.shortName!! }})
          }
          .toImmutableMap())
    }
  }

  fun hasOption(option: String) = options.keys.contains(option)

  fun getOptionValues(option: String) = options.valueOf(option)
}