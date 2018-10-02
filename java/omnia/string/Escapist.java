package omnia.string;

import java.util.Objects;
import omnia.data.structure.Collection;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableSet;

public final class Escapist {

  private final char escapeChar;
  private final Set<Character> specialChars;

  public Escapist(char escapeChar, Collection<Character> specialChars) {
    if (escapeChar == '\0') {
      throw new IllegalArgumentException("Escape character cannot be \\0");
    }
    this.escapeChar = escapeChar;
    this.specialChars = ImmutableSet.<Character>builder()
        .addAll(specialChars)
        .add(escapeChar)
        .build();
  }

  public String escape(String source) {
    Objects.requireNonNull(source);
    if (source.length() == 0) {
      return source;
    }

    StringBuilder result = new StringBuilder();
    int i = 0;
    for (int j = 0; j < source.length(); j++) {
      if (source.charAt(j) == escapeChar) {
        result.append(source.subSequence(i, j));
        i = j = j + 1;
      }
    }
    if (i == source.length()) {
      i = source.length() - 1;
    }
    if (i < 0) {
      return "";
    }
    return result.append(source.subSequence(i, source.length())).toString();
  }

  public String unescape(String source) {
    Objects.requireNonNull(source);
    if (source.length() == 0) {
      return source;
    }

    StringBuilder result = new StringBuilder();
    int i = 0;
    for (int j = 0; j < source.length(); j++) {
      if (specialChars.contains(source.charAt(j))) {
        result.append(source.subSequence(i, j - 1)).append(escapeChar);
        i = j;
      }
    }
    return result.append(source.subSequence(i, source.length())).toString();
  }
}
