package omnia.cli.out;

import static java.util.stream.Collectors.joining;

import java.util.Optional;
import java.util.stream.Stream;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;
import omnia.data.structure.mutable.ArrayList;
import omnia.data.structure.mutable.MutableList;

public final class Output {
  private final List<Span> spans;

  private Output(List<Span> spans) {
    this.spans = ImmutableList.copyOf(spans);
  }

  public String render() {
    return spans.stream()
        .flatMap(
            span ->
                Stream.<String>builder()
                    .add(span.formatting.render())
                    .add(span.text)
                    .build())
        .collect(joining());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final MutableList<Span> spans = ArrayList.create();
    private Optional<Color16> color;
    private Optional<Color16> background;
    private Optional<Boolean> bold;
    private Optional<Boolean> dim;
    private Optional<Boolean> underlined;
    private Optional<Boolean> blinking;
    private Optional<Boolean> inverted;
    private Optional<Boolean> hidden;

    public Builder append(String string) {
      if (!string.isEmpty()) {
        spans.add(new Span(string, buildFormatting()));
      }
      return this;
    }

    public Builder append(Output output) {
      output.spans.stream()
          .map(span -> new Span(span.text, buildFormatting().apply(span.formatting)))
          .forEachOrdered(spans::add);
      return this;
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

  private static class Span {
    private final String text;
    private final Formatting formatting;

    private Span(String text, Formatting formatting) {
      this.text = text;
      this.formatting = formatting;
    }
  }

  private static final class Formatting {
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
      this.color = color;
      this.background = background;
      this.bold = bold;
      this.dim = dim;
      this.underlined = underlined;
      this.blinking = blinking;
      this.inverted = inverted;
      this.hidden = hidden;
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

    String render() {
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
          .append('m')
          .toString();
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
