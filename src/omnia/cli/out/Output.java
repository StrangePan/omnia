package omnia.cli.out;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static omnia.data.stream.Collectors.toList;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;
import omnia.data.structure.mutable.ArrayList;
import omnia.data.structure.mutable.MutableList;

public final class Output {
  private static final Output EMPTY = new Output(ImmutableList.empty());

  private final List<Span<?>> spans;

  private Output(List<Span<?>> spans) {
    this.spans = ImmutableList.copyOf(spans);
  }

  public String toString() {
    return renderForTerminal();
  }

  public String renderForTerminal() {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < spans.count(); i++) {
      Span<?> span = spans.itemAt(i);
      if (i > 0
          && spans.itemAt(i - 1) instanceof InlineSpan
          && endsInNewLine(((InlineSpan) spans.itemAt(i - 1)).text)
          && span instanceof LineSpan) {
        output.append('\n');
      }
      output.append(span.render());
    }
    return output.toString();
  }

  private static boolean endsInNewLine(String string) {
    return string.charAt(string.length() - 1) == '\n';
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Output just(String message) {
    return message.isEmpty()
        ? empty()
        : new Output(ImmutableList.of(new InlineSpan(message, Formatting.EMPTY)));
  }

  public static Output empty() {
    return EMPTY;
  }

  public static final class Builder {
    private final MutableList<Span<?>> spans = ArrayList.create();
    private Optional<Color16> color = Optional.empty();
    private Optional<Color16> background = Optional.empty();
    private Optional<Boolean> bold = Optional.empty();
    private Optional<Boolean> dim = Optional.empty();
    private Optional<Boolean> underlined = Optional.empty();
    private Optional<Boolean> blinking = Optional.empty();
    private Optional<Boolean> inverted = Optional.empty();
    private Optional<Boolean> hidden = Optional.empty();

    public Builder append(String string) {
      if (!string.isEmpty()) {
        spans.add(new InlineSpan(string, buildFormatting()));
      }
      return this;
    }

    public Builder append(Output output) {
      Formatting base = buildFormatting();
      output.spans.stream()
          .map(span -> span.mergeFormatting(base))
          .forEachOrdered(spans::add);
      return this;
    }

    public Builder appendLine() {
      spans.add(new LineSpan(ImmutableList.empty(), 0));
      return this;
    }

    public Builder appendLine(String string) {
      return appendLine(string, 0);
    }

    public Builder appendLine(String string, int indentation) {
      requireNonNegative(indentation);
      spans.add(
          new LineSpan(ImmutableList.of(new InlineSpan(string, buildFormatting())), indentation));
      return this;
    }

    public Builder appendLine(Output output) {
      return appendLine(output, 0);
    }

    public Builder appendLine(Output output, int indentation) {
      requireNonNegative(indentation);
      // scan through the spans, group together inline spans into line spans, and apply additional
      // indentation to line spans. Oh, and merge formatting.
      ImmutableList.Builder<InlineSpan> groupedInlineSpans = null;
      Formatting baseFormatting = buildFormatting();
      for (Span<?> span : output.spans) {
        if (span instanceof InlineSpan) {
          if (groupedInlineSpans == null) {
            groupedInlineSpans = ImmutableList.builder();
          }
          groupedInlineSpans.add(((InlineSpan) span).mergeFormatting(baseFormatting));
        } else if (span instanceof LineSpan) {
          if (groupedInlineSpans != null) {
            this.spans.add(new LineSpan(groupedInlineSpans.build(), indentation));
            groupedInlineSpans = null;
          }
          this.spans.add(
              new LineSpan(
                  ((LineSpan) span).spans.stream()
                      .map(mergeFormatting(baseFormatting))
                      .collect(toList()),
                  ((LineSpan) span).indentation + indentation));
        }
      }
      if (groupedInlineSpans != null) {
        this.spans.add(new LineSpan(groupedInlineSpans.build(), indentation));
      }
      return this;
    }

    private static <T extends Span<T>> Function<T, T> mergeFormatting(Formatting base) {
      return span -> span.mergeFormatting(base);
    }

    private static void requireNonNegative(int indentation) {
      if (indentation < 0) {
        throw new IllegalArgumentException("indentation cannot be negative: " + indentation);
      }
    }

    public Builder color(Color16 color16) {
      color = Optional.of(color16);
      return this;
    }

    public Builder defaultColor() {
      color = Optional.empty();
      return this;
    }

    public Builder background(Color16 color16) {
      background = Optional.of(color16);
      return this;
    }

    public Builder defaultBackground() {
      background = Optional.empty();
      return this;
    }

    public Builder bold() {
      bold = Optional.of(true);
      return this;
    }

    public Builder notBold() {
      bold = Optional.of(false);
      return this;
    }

    public Builder defaultBold() {
      bold = Optional.empty();
      return this;
    }

    public Builder dim() {
      dim = Optional.of(true);
      return this;
    }

    public Builder notDim() {
      dim = Optional.of(false);
      return this;
    }

    public Builder defaultDim() {
      dim = Optional.empty();
      return this;
    }

    public Builder underlined() {
      underlined = Optional.of(true);
      return this;
    }

    public Builder notUnderlined() {
      underlined = Optional.of(false);
      return this;
    }

    public Builder defaultUnderline() {
      underlined = Optional.empty();
      return this;
    }

    public Builder blinking() {
      blinking = Optional.of(true);
      return this;
    }

    public Builder notBlinking() {
      blinking = Optional.of(false);
      return this;
    }

    public Builder defaultBlinking() {
      blinking = Optional.empty();
      return this;
    }

    public Builder inverted() {
      inverted = Optional.of(true);
      return this;
    }

    public Builder notInverted() {
      inverted = Optional.of(false);
      return this;
    }

    public Builder defaultInverted() {
      inverted = Optional.empty();
      return this;
    }

    public Builder hidden() {
      hidden = Optional.of(true);
      return this;
    }

    public Builder notHidden() {
      hidden = Optional.of(false);
      return this;
    }

    public Builder defaultHidden() {
      hidden = Optional.empty();
      return this;
    }

    public Builder defaultFormatting() {
      return defaultColor()
          .defaultBackground()
          .defaultBold()
          .defaultDim()
          .defaultUnderline()
          .defaultBlinking()
          .defaultInverted()
          .defaultHidden();
    }

    private Formatting buildFormatting() {
      return new Formatting(color, background, bold, dim, underlined, blinking, inverted, hidden);
    }

    public Output build() {
      return new Output(spans);
    }
  }

  private interface Span<T extends Span<T>> {

    StringBuilder render();

    T mergeFormatting(Formatting base);
  }

  private static class InlineSpan implements Span<InlineSpan> {
    private final String text;
    private final Formatting formatting;

    private InlineSpan(String text, Formatting formatting) {
      this.text = text;
      this.formatting = formatting;
    }

    @Override
    public StringBuilder render() {
      return formatting.render().append(text);
    }

    @Override
    public InlineSpan mergeFormatting(Formatting base) {
      return new InlineSpan(text, base.apply(formatting));
    }
  }

  private static class LineSpan implements Span<LineSpan> {
    private final List<InlineSpan> spans;
    private final int indentation;

    private LineSpan(List<InlineSpan> spans, int indentation) {
      this.spans = ImmutableList.copyOf(spans);
      this.indentation = indentation;
    }

    @Override
    public StringBuilder render() {
      if (!spans.isPopulated()) {
        return new StringBuilder("\n");
      }
      String indentation = " ".repeat(this.indentation);
      return new StringBuilder(indentation)
          .append(spans.stream()
              .map(Span::render)
              .map(Object::toString)
              .map(rendering -> rendering.replaceAll("\\n", "\n" + indentation))
              .<StringBuilder>collect(
                  StringBuilder::new, StringBuilder::append, StringBuilder::append))
          .append("\n");
    }

    @Override
    public LineSpan mergeFormatting(Formatting base) {
      return new LineSpan(
          spans.stream().map(inlineSpan -> inlineSpan.mergeFormatting(base)).collect(toList()),
          indentation);
    }
  }

  private static final class Formatting {
    private static final Formatting EMPTY =
        new Formatting(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    private final Optional<Color16> color;
    private final Optional<Color16> background;
    private final Optional<Boolean> bold;
    private final Optional<Boolean> dim;
    private final Optional<Boolean> underlined;
    private final Optional<Boolean> blinking;
    private final Optional<Boolean> inverted;
    private final Optional<Boolean> hidden;

    private Formatting(
        Optional<Color16> color,
        Optional<Color16> background,
        Optional<Boolean> bold,
        Optional<Boolean> dim,
        Optional<Boolean> underlined,
        Optional<Boolean> blinking,
        Optional<Boolean> inverted,
        Optional<Boolean> hidden) {
      this.color = requireNonNull(color);
      this.background = requireNonNull(background);
      this.bold = requireNonNull(bold);
      this.dim = requireNonNull(dim);
      this.underlined = requireNonNull(underlined);
      this.blinking = requireNonNull(blinking);
      this.inverted = requireNonNull(inverted);
      this.hidden = requireNonNull(hidden);
    }

    Formatting apply(Formatting other) {
      return new Formatting(
          other.color.or(() -> this.color),
          other.background.or(() -> this.background),
          other.bold.or(() -> this.bold),
          other.dim.or(() -> this.dim),
          other.underlined.or(() -> this.underlined),
          other.blinking.or(() -> this.blinking),
          other.inverted.or(() -> this.inverted),
          other.hidden.or(() -> this.hidden));
    }

    StringBuilder render() {
      return new StringBuilder("\033[")
          .append(
              Stream.<Optional<String>>builder()
                  .add(Optional.of("0"))
                  .add(color.map(Color16::foregroundCode))
                  .add(background.map(Color16::backgroundCode))
                  .add(bold.map(u -> "1"))
                  .add(dim.map(u -> "2"))
                  .add(underlined.map(u -> "4"))
                  .add(blinking.map(u -> "5"))
                  .add(inverted.map(u -> "7"))
                  .add(hidden.map(u -> "8"))
                  .build()
                  .flatMap(Optional::stream)
                  .collect(joining(";")))
          .append("m");
    }
  }

  public enum Color16 {
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

    private String foreground;
    private String background;

    Color16(String foreground, String background) {
      this.foreground = foreground;
      this.background = background;
    }

    private String foregroundCode() {
      return foreground;
    }

    private String backgroundCode() {
      return background;
    }
  }
}
