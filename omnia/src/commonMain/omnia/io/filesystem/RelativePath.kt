package omnia.io.filesystem

import kotlin.math.max
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList

/**
 * Immutable representation of the location of a file system object (file or directory) wthin a file system relative to
 * another path.
 *
 * - Directories are delimited with '/'
 * - '.' is interpreted as the current directory, redundant, and thus not allowed
 * - '..' is interpreted as the parent directory, and thus can only appear that beginning of the relative path
 *
 * @property trimmedParents The number of parent directories this path excludes.
 * @property components The child components (directories) of this path, applied after the [trimmedParents] number of
 * parents are trimmed.
 */
data class RelativePath(val trimmedParents: Int = 0, val components: ImmutableList<PathComponent> = ImmutableList.empty()) {

  /**
   * @throws PathParseException if [components] contains an illegal component.
   */
  constructor(vararg components: String): this(components.asIterable());

  /**
   * @throws IllegalArgumentException if [trimmedParents] is negative.
   * @throws PathParseException if [components] contains an illegal component.
   */
  constructor(trimmedParents: Int, vararg components: String):
    this(trimmedParents, components.asIterable())

  /**
   * @throws PathParseException if [components] contains an illegal component.
   */
  constructor(components: Iterable<String>): this(0, components)

  /**
   * @throws IllegalArgumentException if [trimmedParents] is negative.
   * @throws PathParseException if [components] contains an illegal component.
   */
  constructor(trimmedParents: Int, components: Iterable<String>):
    this(trimmedParents, components.map(::PathComponent).toImmutableList())

  init {
    require(trimmedParents >= 0)
  }

  /**
   * Concatenates [other] into this [RelativePath], including [trimmedParents].
   *
   * The right operand's [trimmedParents] will remove [components] from the left operator. If any [trimmedParents]
   * remain from the right operator, then the remainder is added with the left operator's [trimmedParents]. Otherwise,
   * any untrimmed [components] from the left operator are concatenated before the right operator's [components].
   *
   * This operation is NOT commutative, meaning that swapping the left operand and the right operand will produce a
   * different results. In other words, `A + B != B + A`.
   */
  operator fun plus(other: RelativePath): RelativePath =
    RelativePath(
      this.trimmedParents + max(0, other.trimmedParents - this.components.count),
      ImmutableList.builder<PathComponent>()
        .addAll(this.components.take(max(0, this.components.count - other.trimmedParents)))
        .addAll(other.components)
        .build()
    )

  /** Parses [string] as a relative path and concatenates it to the end of this path. */
  operator fun plus(string: String): RelativePath =
    this + parse(string)

  override fun toString(): String =
    buildString(trimmedParents * 3 + components.sumOf { it.name.length } + components.count) { ->
      repeat(trimmedParents) {
        this.append("../")
      }
      components.forEach {
        this.append(it).append('/')
      }
      if (trimmedParents > 0 || components.isPopulated) {
        this.deleteAt(this.length - 1)
      }
    }

  companion object {
    /**
     * Parses a string that represents a relative file path. Relative file paths must not begin or end with '/'. Parsed
     * paths are automatically simplified to remove redundant '..' and '.' components.
     *
     * @throws [PathParseException] if the path is malformed or contains illegal components
     */
    fun parse(string: String): RelativePath {
      if (string.isEmpty()) {
        return RelativePath(0, ImmutableList.empty<PathComponent>())
      }
      if (string.first() == '/') {
        throw PathParseException("Relative path cannot begin with a '/'")
      }
      if (string.last() == '/') {
        throw PathParseException("Relative path cannot end with a '/'")
      }

      val components = string.split('/')
      var numTrimmedParents = components.indexOfFirst { it != ".." }

      val parsedComponents = ImmutableList.builder<PathComponent>()

      components.drop(numTrimmedParents).filter { it != "." }.forEach { component ->
        if (component == "..") {
          if (parsedComponents.count == 0) {
            numTrimmedParents++
          } else {
            parsedComponents.removeAt(parsedComponents.count - 1)
          }
        } else {
          parsedComponents.add(
            try {
              PathComponent(component)
            } catch (e: PathParseException) {
              throw PathParseException("Unable to parse relative path: \"$string\". ${e.message}", e)
            })
        }
      }

      return RelativePath(numTrimmedParents, parsedComponents.build())
    }
  }
}

fun String.asRelativePath() =
  RelativePath.parse(this)
