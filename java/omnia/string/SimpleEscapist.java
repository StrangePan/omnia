package omnia.string;

import java.util.Objects;
import java.util.Optional;
import omnia.data.structure.BidirectionalMap;
import omnia.data.structure.mutable.HashBidirectionalMap;
import omnia.data.structure.mutable.MutableBidirectionalMap;

public final class SimpleEscapist implements Escapist {

  private final Character escapeCharacter;
  private final BidirectionalMap<Character, Character> replacements;

  private SimpleEscapist(Builder builder) {
    if (builder.escapeCharacter == null) {
      throw new IllegalArgumentException("Escape character not set on builder");
    }
    escapeCharacter = builder.escapeCharacter;

    // Copy builder map (still-mutable) into new one
    MutableBidirectionalMap<Character, Character> escapeMap =
        HashBidirectionalMap.copyOf(builder.replacements);

    // Add escape entry for escape character if it isn't already present
    if (!escapeMap.valueOf(escapeCharacter).isPresent()) {
      escapeMap.putMapping(escapeCharacter, escapeCharacter);
    }

    this.replacements = escapeMap;
  }

  @Override
  public String escape(String source) {
    Objects.requireNonNull(source);
    if (source.length() == 0) {
      return source;
    }

    StringBuilder result = new StringBuilder();
    int lastSpecial = -1;
    for (int i = 0; i < source.length(); i++) {
      char c = source.charAt(i);

      // check if current character needs to be replaced
      Optional<Character> replacement = replacements.valueOf(c);
      if (replacement.isPresent()) {

        // add current sequence of non-special characters
        if (lastSpecial + 1 < i) {
          result.append(source.substring(lastSpecial + 1, i));
        }

        // append escape character and special character replacement
        result.append(escapeCharacter).append(replacement.get());
        lastSpecial = i;
      }
    }

    // if we reach the end of the string, be sure to append the last non-special characters
    if (lastSpecial + 1 < source.length()) {
      result.append(source.substring(lastSpecial + 1, source.length()));
    }

    return result.toString();
  }

  @Override
  public String unescape(String source) {
    Objects.requireNonNull(source);
    if (source.length() == 0) {
      return source;
    }

    BidirectionalMap<Character, Character> replacements = this.replacements.inverse();
    StringBuilder result = new StringBuilder();
    int lastSpecial = -1;
    boolean escaped = false;

    for (int i = 0; i < source.length(); i++) {
      char c = source.charAt(i);

      if (escaped) {

        // see if escaped character has replacement mapping. and use it if it exists
        result.append(replacements.valueOf(c).orElse(c));
        escaped = false;
      } else if (escapeCharacter.equals(c)) {

        // when encountering the escape character, append current sequence of non-special characters
        if (lastSpecial + 1 < i) {
          result.append(source.substring(lastSpecial + 1, i));

          // automatically advance lastSpecial past the next character (which we treat as a special)
          lastSpecial = i + 1;
        }
        escaped = true;
      }
    }

    // if we reach the end of the string, be sure to append the last non-special characters
    if (lastSpecial + 1 < source.length()) {
      result.append(source.substring(lastSpecial + 1, source.length()));
    }

    return result.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final MutableBidirectionalMap<Character, Character> replacements = new HashBidirectionalMap<>();
    private Character escapeCharacter = null;

    public Builder escapeCharacter(Character character) {
      if (character.equals('\0')) {
        throw new IllegalArgumentException("Escape character cannot be \"\\0\"");
      }
      this.escapeCharacter = character;
      return this;
    }

    public Builder addEscapedCharacter(char character) {
      this.replacements.putMapping(character, character);
      return this;
    }

    public Builder addEscapedReplacement(char original, char replacement) {
      this.replacements.putMapping(original, replacement);
      return this;
    }

    public SimpleEscapist build() {
      return new SimpleEscapist(this);
    }
  }
}
