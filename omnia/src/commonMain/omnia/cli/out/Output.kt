package omnia.cli.out

import omnia.data.structure.List
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
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
    return render(Renderable::render)
  }

  /**
   * Renders the contents of this Output for the terminal minus any color codes and formatting
   * codes.
   */
  fun renderWithoutCodes(): String {
    return render(Renderable::renderWithoutCodes)
  }

  private fun render(propagation: (Renderable) -> StringBuilder): String {
    val output = StringBuilder()
    for (i in 0 until spans.count) {
      val span = spans.itemAt(i)
      if (i > 0
        && spans.itemAt(i - 1) is InlineSpan
        && endsInNewLine((spans.itemAt(i - 1) as InlineSpan).text)
        && span is LineSpan
      ) {
        output.append(lineSeparator())
      }
      output.append(propagation(span))
    }
    return output.toString()
  }

  class Builder {
    private val spans: MutableList<Span<*>> = ArrayList.create()
    private var color: Color16? = null
    private var background: Color16? = null
    private var bold: Boolean? = null
    private var dim: Boolean? = null
    private var underlined: Boolean? = null
    private var blinking: Boolean? = null
    private var inverted: Boolean? = null
    private var hidden: Boolean? = null
    fun append(string: String): Builder {
      if (string.isNotEmpty()) {
        spans.add(InlineSpan(string, buildFormatting()))
      }
      return this
    }

    fun append(output: Output): Builder {
      val base = buildFormatting()
      output.spans
          .map { it.mergeFormatting(base) }
          .forEach(spans::add)
      return this
    }

    fun appendLine(): Builder {
      spans.add(EMPTY_LINE_SPAN)
      return this
    }

    fun appendLine(string: String, indentation: Int = 0): Builder {
      requireNonNegative(indentation)
      spans.add(LineSpan(ImmutableList.of(InlineSpan(string, buildFormatting())), indentation))
      return this
    }

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
                  span.spans.map { it.mergeFormatting(baseFormatting) }.toImmutableList(),
                  span.indentation + indentation)
          )
        }
      }
      if (groupedInlineSpans != null) {
        spans.add(LineSpan(groupedInlineSpans.build(), indentation))
      }
      return this
    }

    fun color(color16: Color16): Builder {
      color = color16
      return this
    }

    fun defaultColor(): Builder {
      color = null
      return this
    }

    fun background(color16: Color16): Builder {
      background = color16
      return this
    }

    fun defaultBackground(): Builder {
      background = null
      return this
    }

    fun bold(): Builder {
      bold = true
      return this
    }

    fun notBold(): Builder {
      bold = false
      return this
    }

    fun defaultBold(): Builder {
      bold = null
      return this
    }

    fun dim(): Builder {
      dim = true
      return this
    }

    fun notDim(): Builder {
      dim = false
      return this
    }

    fun defaultDim(): Builder {
      dim = null
      return this
    }

    fun underlined(): Builder {
      underlined = true
      return this
    }

    fun notUnderlined(): Builder {
      underlined = false
      return this
    }

    fun defaultUnderline(): Builder {
      underlined = null
      return this
    }

    fun blinking(): Builder {
      blinking = true
      return this
    }

    fun notBlinking(): Builder {
      blinking = false
      return this
    }

    fun defaultBlinking(): Builder {
      blinking = null
      return this
    }

    fun inverted(): Builder {
      inverted = true
      return this
    }

    fun notInverted(): Builder {
      inverted = false
      return this
    }

    fun defaultInverted(): Builder {
      inverted = null
      return this
    }

    fun hidden(): Builder {
      hidden = true
      return this
    }

    fun notHidden(): Builder {
      hidden = false
      return this
    }

    fun defaultHidden(): Builder {
      hidden = null
      return this
    }

    fun defaultFormatting(): Builder {
      return defaultColor()
        .defaultBackground()
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
      return render(Renderable::render)
    }

    override fun renderWithoutCodes(): StringBuilder {
      return render(Renderable::renderWithoutCodes)
    }

    private fun render(propagation: (Renderable) -> StringBuilder): StringBuilder {
      if (!spans.isPopulated) {
        return StringBuilder(lineSeparator())
      }
      val indentation = " ".repeat(indentation)
      return StringBuilder(indentation)
        .append(
            spans
                .map(propagation)
                .map(Any::toString)
                .map {
                  it.replace(
                    Regex.escape(lineSeparator()).toRegex(),
                    lineSeparator() + indentation)
                }
                .fold(StringBuilder(), StringBuilder::append))
        .append(lineSeparator())
    }

    override fun mergeFormatting(base: Formatting): LineSpan {
      return LineSpan(spans.map { it.mergeFormatting(base) }.toImmutableList(), indentation)
    }

    init {
      this.spans = ImmutableList.copyOf(spans)
      this.indentation = indentation
    }
  }

  private class Formatting(
      private val color: Color16?,
      private val background: Color16?,
      private val bold: Boolean?,
      private val dim: Boolean?,
      private val underlined: Boolean?,
      private val blinking: Boolean?,
      private val inverted: Boolean?,
      private val hidden: Boolean?,
  ) : Renderable {

    fun apply(other: Formatting): Formatting {
      return Formatting(
          other.color?:color,
          other.background?:background,
          other.bold?:bold,
          other.dim?:dim,
          other.underlined?:underlined,
          other.blinking?:blinking,
          other.inverted?:inverted,
          other.hidden?:hidden)
    }

    override fun render(): StringBuilder {
      return StringBuilder(RENDER_CODE_PREFIX)
        .append(
          listOfNotNull(
            "0",
            color?.foregroundCode(),
            background?.backgroundCode(),
            bold?.let { "1" },
            dim?.let { "2" },
            underlined?.let { "4" },
            blinking?.let { "5" },
            inverted?.let { "7" },
            hidden?.let { "8" })
            .joinToString(RENDER_CODE_DELIMITER))
          .append(RENDER_CODE_SUFFIX)
    }

    override fun renderWithoutCodes(): StringBuilder {
      return StringBuilder()
    }

    companion object {
      val EMPTY: Formatting = Formatting(null, null, null, null, null, null, null, null)
      const val RENDER_CODE_PREFIX = "\u001b["
      const val RENDER_CODE_DELIMITER = ";"
      const val RENDER_CODE_SUFFIX = "m"
    }
  }

  enum class Color16(private val foreground: String, private val background: String) {
    DEFAULT("39", "49"),
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
      return string.endsWith(lineSeparator()) || string.endsWith("\n") || string.endsWith("\r")
    }

    fun builder(): Builder {
      return Builder()
    }

    fun just(message: String): Output {
      return if (message.isEmpty()) empty() else Output(
          ImmutableList.of(InlineSpan(message, Formatting.EMPTY))
      )
    }

    fun justLine(message: String): Output {
      return if (message.isEmpty()) empty() else Output(
          ImmutableList.of(LineSpan(ImmutableList.of(InlineSpan(message, Formatting.EMPTY)), 0))
      )
    }

    fun justNewline(): Output {
      return EMPTY_LINE
    }

    fun empty(): Output {
      return EMPTY
    }

    /**
     * Strip terminal color codes embedded from the given string and return the same string with all
     * formatting codes removed.
     *
     * Uses Regex to strip console formatting codescd out of the provided string.
     */
    fun stripCodes(text: String): String {
      val renderCodeRegex =
        Regex.fromLiteral(
          Regex.escape(Formatting.RENDER_CODE_PREFIX)
              + "(\\d+"
              + Regex.escape(Formatting.RENDER_CODE_DELIMITER)
              + ")*"
              + Regex.escape(Formatting.RENDER_CODE_SUFFIX))
      return text.replace(renderCodeRegex, "")
    }
  }

  init {
    this.spans = ImmutableList.copyOf(spans)
  }
}