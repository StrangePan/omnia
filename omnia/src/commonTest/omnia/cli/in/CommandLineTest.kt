package omnia.cli.`in`

import kotlin.test.Test
import kotlin.test.assertFailsWith
import omnia.cli.`in`.Parameter.Repeatable.NOT_REPEATABLE
import omnia.cli.`in`.Parameter.Repeatable.REPEATABLE
import omnia.data.structure.immutable.ImmutableList
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.containsExactlyElementsIn
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isNotNull
import omnia.util.test.fluent.isTrue

class CommandLineTest {

  companion object {
    private val NON_REPEATABLE_FLAG = FlagOption("flag", "f", "", NOT_REPEATABLE)
    private val REPEATABLE_FLAG = FlagOption("rflag", "r", "", REPEATABLE)
    private val NON_REPEATABLE_STRING_OPTION = StringOption("string", "s", "", NOT_REPEATABLE)
    private val REPEATABLE_STRING_OPTION = StringOption("rstring", "t", "", REPEATABLE)
  }

  @Test
  fun parse_withUnknownOption_throwsParserException() {
    assertFailsWith(ParserException::class) {
      CommandLine.parse(
        ImmutableList.of("--unknown"),
        Options()
      )
    }
  }

  @Test
  fun parse_withParameters_returnsParameters() {
    val arguments = ImmutableList.of("first", "second", "third")

    assertThat(CommandLine.parse(arguments, Options()).argList).containsExactlyElementsIn(arguments)
  }

  @Test
  fun parse_withNoParameters_returnsNoParameters() {
    assertThat(CommandLine.parse(ImmutableList.empty(), Options()).argList).isEmpty()
  }

  @Test
  fun parse_withFlag_withLongName_setsFlag() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("--${NON_REPEATABLE_FLAG.longName}"),
        Options().addOption(NON_REPEATABLE_FLAG))

    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(NON_REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
  }

  @Test
  fun parse_withFlag_withShortName_setsFlag() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("-${NON_REPEATABLE_FLAG.shortName}"),
        Options().addOption(NON_REPEATABLE_FLAG))

    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(NON_REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
  }

  @Test
  fun parse_withFlag_withLongName_withValue_interpretsValueAsArgument() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("--${NON_REPEATABLE_FLAG.longName}", "arg"),
        Options().addOption(NON_REPEATABLE_FLAG))

    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(NON_REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
    assertThat(underTest.argList).containsExactly("arg")
  }

  @Test
  fun parse_withFlag_withShortName_withValue_interpretsValueAsArgument() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("-${NON_REPEATABLE_FLAG.shortName}", "arg"),
        Options().addOption(NON_REPEATABLE_FLAG))

    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(NON_REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(NON_REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
    assertThat(underTest.argList).containsExactly("arg")
  }

  @Test
  fun parse_withNonRepeatableFlag_repeated_throwsParserException() {
    assertFailsWith(ParserException::class) {
      CommandLine.parse(
        ImmutableList.of("-${NON_REPEATABLE_FLAG.shortName}", "--${NON_REPEATABLE_FLAG.longName}"),
        Options().addOption(NON_REPEATABLE_FLAG))
    }
  }

  @Test
  fun parse_withRepeatableFlag_setsFlag() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("-${REPEATABLE_FLAG.shortName}", "--${REPEATABLE_FLAG.longName}"),
        Options().addOption(REPEATABLE_FLAG))

    assertThat(underTest.hasOption(REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
  }

  @Test
  fun parse_withRepeatableFlag_withLongName_withValue_interpretsValueAsArgument() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of(
          "--${REPEATABLE_FLAG.longName}",
          REPEATABLE_FLAG.shortName!!,
          "--${REPEATABLE_FLAG.longName}",
          REPEATABLE_FLAG.longName),
        Options().addOption(REPEATABLE_FLAG))

    assertThat(underTest.hasOption(REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
    assertThat(underTest.argList)
      .containsExactly(REPEATABLE_FLAG.shortName!!, REPEATABLE_FLAG.longName)
  }

  @Test
  fun parse_withRepeatableFlag_withShortName_withValue_interpretsValueAsArgument() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of(
          "-${REPEATABLE_FLAG.shortName}",
          REPEATABLE_FLAG.shortName!!, // to test that the - is required
          "-${REPEATABLE_FLAG.shortName}",
          REPEATABLE_FLAG.longName), // to test that the -- is required
        Options().addOption(REPEATABLE_FLAG))

    assertThat(underTest.hasOption(REPEATABLE_FLAG.longName)).isTrue()
    assertThat(underTest.hasOption(REPEATABLE_FLAG.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(REPEATABLE_FLAG.longName)).isNotNull().isEmpty()
    assertThat(underTest.argList)
      .containsExactly(REPEATABLE_FLAG.shortName!!, REPEATABLE_FLAG.longName)
  }

  @Test
  fun parse_withStringOption_withoutValue_throwsParserException() {
    assertFailsWith(ParserException::class) {
      CommandLine.parse(
        ImmutableList.of("--${NON_REPEATABLE_STRING_OPTION.longName}"),
        Options().addOption(NON_REPEATABLE_STRING_OPTION))
    }
  }

  @Test
  fun parse_withStringOption_repeated_throwsParserException() {
    assertFailsWith(ParserException::class) {
      CommandLine.parse(
        ImmutableList.of(
          "--${NON_REPEATABLE_STRING_OPTION.longName}",
          "val",
          "-${NON_REPEATABLE_STRING_OPTION.shortName}",
          "val"),
        Options().addOption(NON_REPEATABLE_STRING_OPTION))
    }
  }

  @Test
  fun parse_withStringOption_returnsValue() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("--${NON_REPEATABLE_STRING_OPTION.longName}", "value"),
        Options().addOption(NON_REPEATABLE_STRING_OPTION))

    assertThat(underTest.hasOption(NON_REPEATABLE_STRING_OPTION.longName)).isTrue()
    assertThat(underTest.hasOption(NON_REPEATABLE_STRING_OPTION.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(NON_REPEATABLE_STRING_OPTION.longName))
      .isNotNull()
      .containsExactly("value")
  }

  @Test
  fun parse_withStringOption_withEqualsSyntax_returnsValue() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of("--${NON_REPEATABLE_STRING_OPTION.longName}=value", "arg"),
        Options().addOption(NON_REPEATABLE_STRING_OPTION))

    assertThat(underTest.hasOption(NON_REPEATABLE_STRING_OPTION.longName)).isTrue()
    assertThat(underTest.hasOption(NON_REPEATABLE_STRING_OPTION.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(NON_REPEATABLE_STRING_OPTION.longName))
      .isNotNull()
      .containsExactly("value")
    assertThat(underTest.argList).containsExactly("arg")
  }

  @Test
  fun parse_withRepeatableStringOption_withoutValue_throwsParserException() {
    assertFailsWith(ParserException::class) {
      CommandLine.parse(
        ImmutableList.of(
          "--${REPEATABLE_STRING_OPTION.longName}",
          "val",
          "-${REPEATABLE_STRING_OPTION.shortName}"),
        Options().addOption(REPEATABLE_STRING_OPTION))
    }
  }

  @Test
  fun parse_withRepeatableStringOption_returnsValues() {
    val underTest =
      CommandLine.parse(
        ImmutableList.of(
          "--${REPEATABLE_STRING_OPTION.longName}",
          "value1",
          "-${REPEATABLE_STRING_OPTION.shortName}",
          "value2",
          "--${REPEATABLE_STRING_OPTION.longName}=value3"),
        Options().addOption(REPEATABLE_STRING_OPTION))

    assertThat(underTest.hasOption(REPEATABLE_STRING_OPTION.longName)).isTrue()
    assertThat(underTest.hasOption(REPEATABLE_STRING_OPTION.shortName!!)).isTrue()
    assertThat(underTest.getOptionValues(REPEATABLE_STRING_OPTION.longName))
      .isNotNull()
      .containsExactly("value1", "value2", "value3")
    assertThat(underTest.getOptionValues(REPEATABLE_STRING_OPTION.shortName!!))
      .isNotNull()
      .containsExactly("value1", "value2", "value3")
  }
}