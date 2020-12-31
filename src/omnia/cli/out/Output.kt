package omnia.cli.out

import java.util.Optional
import java.util.function.Function
import java.util.regex.Pattern
import java.util.stream.Stream
import omnia.data.stream.Collectors
import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.mutable.ArrayList
import omnia.data.structure.mutable.MutableList

class Output private constructor(spans: List<Span<*>>) {

  private val spans: List<Span<*>>
  val isPopulated: Boolean
    get() = spans.isPopulated

  override fun toString(): String {
    return renderWithoutCodes()
  }

  /**
   * Renders the contents of this Output for the terminal, complete with formatting and color codes.
   */
  fun render(): String {
    return render { obj: Renderable -> obj.render() }
  }

  /**
   * Renders the contents of this Output for the terminal minus any color codes and formatting
   * codes.
   */
  fun renderWithoutCodes(): String {
    return render { obj: Renderable -> obj.renderWithoutCodes() }
  }

  private fun render(propagation: Function<in Renderable, out StringBuilder>): String {
    val output = StringBuilder()
    for (i in 0 until spans.count()) {
      val span = spans.itemAt(i)
      if (i > 0 && spans.itemAt(i - 1) is InlineSpan && endsInNewLine((spans.itemAt(i - 1) as InlineSpan).text) && span is LineSpan) {
        output.append(System.lineSeparator())
      }
      output.append(propagation.apply(span))
    }
    return output.toString()
  }

  class Builder {

    private val spans: MutableList<Span<*>> = ArrayList.create()
    private var color: Optional<Color16> = Optional.empty()
    private var background: Optional<Color16> = Optional.empty()
    private var bold: Optional<Boolean> = Optional.empty()
    private var dim: Optional<Boolean> = Optional.empty()
    private var underlined: Optional<Boolean> = Optional.empty()
    private var blinking: Optional<Boolean> = Optional.empty()
    private var inverted: Optional<Boolean> = Optional.empty()
    private var hidden: Optional<Boolean> = Optional.empty()
    fun append(string: String): Builder {
      if (string.isNotEmpty()) {
        spans.add(InlineSpan(string, buildFormatting()))
      }
      return this
    }

    fun append(output: Output): Builder {
      val base = buildFormatting()
      output.spans.stream()
          .map { span: Span<*> -> span.mergeFormatting(base) }
          .forEachOrdered { element -> spans.add(element) }
      return this
    }

    fun appendLine(): Builder {
      spans.add(EMPTY_LINE_SPAN)
      return this
    }

    @JvmOverloads
    fun appendLine(string: String, indentation: Int = 0): Builder {
      requireNonNegative(indentation)
      spans.add(LineSpan(ImmutableList.of(InlineSpan(string, buildFormatting())), indentation))
      return this
    }

    @JvmOverloads
    fun appendLine(output: Output, indentation: Int = 0): Builder {
      requireNonNegative(indentation)
      // scan through the spans, group together inline spans into line spans, and apply additional
      // indentation to line spans. Oh, and merge formatting.
      var groupedInlineSpans: ImmutableList.Builder<InlineSpan>? = null
      val baseFormatting = buildFormatting()
      for (span in output.spans) {
        if (span is InlineSpan) {
          if (groupedInlineSpans == null) {
            groupedInlineSpans = ImmutableList.builder()
          }
          groupedInlineSpans.add(span.mergeFormatting(baseFormatting))
        } else if (span is LineSpan) {
          if (groupedInlineSpans != null) {
            spans.add(LineSpan(groupedInlineSpans.build(), indentation))
            groupedInlineSpans = null
          }
          spans.add(
              LineSpan(
                  span.spans.stream()
                      .map(mergeFormatting(baseFormatting))
                      .collect(Collectors.toList()), span.indentation + indentation
              )
          )
        }
      }
      if (groupedInlineSpans != null) {
        spans.add(LineSpan(groupedInlineSpans.build(), indentation))
      }
      return this
    }

    fun color(color16: Color16): Builder {
      color = Optional.of(color16)
      return this
    }

    fun defaultColor(): Builder {
      color = Optional.empty()
      return this
    }

    fun background(color16: Color16): Builder {
      background = Optional.of(color16)
      return this
    }

    fun defaultBackground(): Builder {
      background = Optional.empty()
      return this
    }

    fun bold(): Builder {
      bold = Optional.of(true)
      return this
    }

    fun notBold(): Builder {
      bold = Optional.of(false)
      return this
    }

    fun defaultBold(): Builder {
      bold = Optional.empty()
      return this
    }

    fun dim(): Builder {
      dim = Optional.of(true)
      return this
    }

    fun notDim(): Builder {
      dim = Optional.of(false)
      return this
    }

    fun defaultDim(): Builder {
      dim = Optional.empty()
      return this
    }

    fun underlined(): Builder {
      underlined = Optional.of(true)
      return this
    }

    fun notUnderlined(): Builder {
      underlined = Optional.of(false)
      return this
    }

    fun defaultUnderline(): Builder {
      underlined = Optional.empty()
      return this
    }

    fun blinking(): Builder {
      blinking = Optional.of(true)
      return this
    }

    fun notBlinking(): Builder {
      blinking = Optional.of(false)
      return this
    }

    fun defaultBlinking(): Builder {
      blinking = Optional.empty()
      return this
    }

    fun inverted(): Builder {
      inverted = Optional.of(true)
      return this
    }

    fun notInverted(): Builder {
      inverted = Optional.of(false)
      return this
    }

    fun defaultInverted(): Builder {
      inverted = Optional.empty()
      return this
    }

    fun hidden(): Builder {
      hidden = Optional.of(true)
      return this
    }

    fun notHidden(): Builder {
      hidden = Optional.of(false)
      return this
    }

    fun defaultHidden(): Builder {
      hidden = Optional.empty()
      return this
    }

    fun defaultFormatting(): Builder {
      return defaultColor().defaultBackground()
          .defaultBold()
          .defaultDim()
          .defaultUnderline()
          .defaultBlinking()
          .defaultInverted()
          .defaultHidden()
    }

    private fun buildFormatting(): Formatting {
      return Formatting(color, background, bold, dim, underlined, blinking, inverted, hidden)
    }

    fun build(): Output {
      return Output(spans)
    }

    companion object {

      private fun <T : Span<T>> mergeFormatting(base: Formatting): Function<T, T> {
        return Function { span: T -> span.mergeFormatting(base) }
      }

      private fun requireNonNegative(indentation: Int) {
        require(indentation >= 0) { "indentation cannot be negative: $indentation" }
      }
    }
  }

  private interface Renderable {

    fun render(): StringBuilder
    fun renderWithoutCodes(): StringBuilder
  }

  private interface Span<T : Span<T>> : Renderable {

    fun mergeFormatting(base: Formatting): T
  }

  private class InlineSpan(
      val text: String,
      private val formatting: Formatting,
  ) : Span<InlineSpan> {

    override fun render(): StringBuilder {
      return formatting.render().append(text)
    }

    override fun renderWithoutCodes(): StringBuilder {
      return formatting.renderWithoutCodes().append(text)
    }

    override fun mergeFormatting(base: Formatting): InlineSpan {
      return InlineSpan(text, base.apply(formatting))
    }
  }

  private class LineSpan(spans: List<InlineSpan>, indentation: Int) : Span<LineSpan> {

    val spans: List<InlineSpan>
    val indentation: Int
    override fun render(): StringBuilder {
      return render { obj: Renderable -> obj.render() }
    }

    override fun renderWithoutCodes(): StringBuilder {
      return render { obj: Renderable -> obj.renderWithoutCodes() }
    }

    private fun render(propagation: Function<in Renderable, out StringBuilder>): StringBuilder {
      if (!spans.isPopulated) {
        return StringBuilder(System.lineSeparator())
      }
      val indentation = " ".repeat(indentation)
      return StringBuilder(indentation).append(spans.stream()
          .map(propagation)
          .map { obj: Any -> obj.toString() }
          .map { rendering: String ->
            rendering.replace(
                Pattern.quote(System.lineSeparator())
                    .toRegex(),
                System.lineSeparator() + indentation
            )
          }
          .collect({ StringBuilder() },
              { obj: StringBuilder, str: String ->
                obj.append(str)
              }) { obj: StringBuilder, s: StringBuilder ->
            obj.append(s)
          }).append(System.lineSeparator())
    }

    override fun mergeFormatting(base: Formatting): LineSpan {
      return LineSpan(spans.stream()
          .map { inlineSpan: InlineSpan -> inlineSpan.mergeFormatting(base) }
          .collect(Collectors.toList()), indentation)
    }

    init {
      this.spans = ImmutableList.copyOf(spans)
      this.indentation = indentation
    }
  }

  private class Formatting(
      private val color: Optional<Color16>,
      private val background: Optional<Color16>,
      private val bold: Optional<Boolean>,
      private val dim: Optional<Boolean>,
      private val underlined: Optional<Boolean>,
      private val blinking: Optional<Boolean>,
      private val inverted: Optional<Boolean>,
      private val hidden: Optional<Boolean>,
  ) : Renderable {

    fun apply(other: Formatting): Formatting {
      return Formatting(other.color.or { color },
          other.background.or { background },
          other.bold.or { bold },
          other.dim.or { dim },
          other.underlined.or { underlined },
          other.blinking.or { blinking },
          other.inverted.or { inverted },
          other.hidden.or { hidden })
    }

    override fun render(): StringBuilder {
      return StringBuilder("\u001b[").append(Stream.builder<Optional<String>>()
          .add(Optional.of("0"))
          .add(color.map { obj: Color16 -> obj.foregroundCode() })
          .add(background.map { obj: Color16 -> obj.backgroundCode() })
          .add(bold.map { "1" })
          .add(dim.map { "2" })
          .add(underlined.map { "4" })
          .add(blinking.map { "5" })
          .add(inverted.map { "7" })
          .add(hidden.map { "8" })
          .build()
          .flatMap { obj: Optional<String> -> obj.stream() }
          .collect(java.util.stream.Collectors.joining(";")))
          .append("m")
    }

    override fun renderWithoutCodes(): StringBuilder {
      return StringBuilder()
    }

    companion object {

      val EMPTY: Formatting = Formatting(
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty()
      )
    }
  }

  enum class Color16(private val foreground: String, private val background: String) {
    DEFAULT(
        "39",
        "49"
    ),
    BLACK("30", "40"),
    RED("31", "41"),
    GREEN("32", "42"),
    YELLOW("33", "43"),
    BLUE("34", "44"),
    MAGENTA("35", "45"),
    CYAN("36", "46"),
    LIGHT_GRAY("37", "47"),
    DARK_GRAY("90", "100"),
    LIGHT_RED("91", "101"),
    LIGHT_GREEN("92", "102"),
    LIGHT_YELLOW("93", "103"),
    LIGHT_BLUE("94", "104"),
    LIGHT_MAGENTA("95", "105"),
    LIGHT_CYAN("96", "106"),
    WHITE("97", "107");

    fun foregroundCode(): String {
      return foreground
    }

    fun backgroundCode(): String {
      return background
    }
  }

  companion object {

    private val EMPTY_LINE_SPAN: LineSpan = LineSpan(ImmutableList.empty(), 0)
    private val EMPTY: Output = Output(ImmutableList.empty())
    private val EMPTY_LINE: Output = Output(ImmutableList.of(EMPTY_LINE_SPAN))
    private fun endsInNewLine(string: String): Boolean {
      return string.endsWith(System.lineSeparator()) || string.endsWith("\n") || string.endsWith("\r")
    }

    @JvmStatic
    fun builder(): Builder {
      return Builder()
    }

    @JvmStatic
    fun just(message: String): Output {
      return if (message.isEmpty()) empty() else Output(
          ImmutableList.of(InlineSpan(message, Formatting.EMPTY))
      )
    }

    @JvmStatic
    fun justLine(message: String): Output {
      return if (message.isEmpty()) empty() else Output(
          ImmutableList.of(
              LineSpan(
                  ImmutableList.of(
                      InlineSpan(message, Formatting.EMPTY)
                  ), 0
              )
          )
      )
    }

    @JvmStatic
    fun justNewline(): Output {
      return EMPTY_LINE
    }

    @JvmStatic
    fun empty(): Output {
      return EMPTY
    }
  }

  init {
    this.spans = ImmutableList.copyOf(spans)
  }
}