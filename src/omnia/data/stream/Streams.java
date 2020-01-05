package omnia.data.stream;

import java.util.stream.Stream;
public final class Streams {

  private Streams() {}

  @SafeVarargs
  public static <T> Stream<T> concat(Stream<T>... streams) {
    if (streams == null || streams.length == 0) {
      return Stream.empty();
    }
    if (streams.length == 1) {
      return streams[0];
    }
    Stream<T> concatenatedStream = streams[0];
    for (int i = 1; i < streams.length; i++) {
      concatenatedStream = Stream.concat(concatenatedStream, streams[i]);
    }
    return concatenatedStream;
  }
}
