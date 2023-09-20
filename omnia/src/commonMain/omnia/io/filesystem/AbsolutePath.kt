package omnia.io.filesystem

import omnia.data.cache.Memoized.Companion.memoize
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList

/**
 * Immutable representation of the location of a file system object (file or directory) within a file system.
 *
 * - Directories are delimited with '/'
 * - '.' is interpreted as the current directory, redundant, and thus not allowed
 * - '..' is interpreted as the parent directory, and thus cannot appear in an absolute path
 *
 * @property components The components (directories) of this path.
 */
data class AbsolutePath(val components: ImmutableList<PathComponent> = ImmutableList.empty()) {

  /** @throws PathParseException if [components] contains an illegal component. */
  constructor(vararg components: String): this(components.asIterable())

  /** @throws PathParseException if [components] contains an illegal component. */
  constructor(components: Iterable<String>): this(components.map(::PathComponent).toImmutableList())

  /** True if this path points to the file system's root directory (i.e. has no components and is empty). */
  val isRoot = !components.isPopulated

  /**
   * Concatenates [other] into this [AbsolutePath]. If [other] specifies any [RelativePath.trimmedParents], then that
   * number of components are erased from this relative path. If [other] would erase more components than are contained
   * in this AbsolutePath, then an [IllegalArgumentException] is thrown.
   *
   * The right operand's [RelativePath.trimmedParents] will remove [components] from the left operator. If the right
   * operator would remove more components than are contained in the left operator, an [IllegalArgumentException] will
   * be thrown. Afterward, any remaining [components] from the left operator are concatenated before the right
   * operator's [RelativePath.components].
   *
   * This operation is NOT commutative, meaning that swapping the left operand and the right operand will produce a
   * different results. In other words, `A + B != B + A`.
   *
   * @throws IllegalArgumentException if [other]'s [RelativePath.trimmedParents] is greater than the number of
   * [components] contained within this [AbsolutePath].
   */
  operator fun plus(other: RelativePath): AbsolutePath {
    require(other.trimmedParents <= this.components.count) {
      "Cannot combine paths: relative path would access outside of the absolute path's root directory. \"$this\" + \"$other\"."
    }
    return AbsolutePath(
      ImmutableList.builder<PathComponent>()
        .addAll(this.components.take(this.components.count - other.trimmedParents))
        .addAll(other.components)
        .build())
  }

  operator fun plus(string: String): AbsolutePath =
    this + RelativePath.parse(string)

  operator fun plus(component: PathComponent): AbsolutePath =
    this + RelativePath(0, ImmutableList.of(component))

  operator fun minus(components: Int): AbsolutePath =
    this + RelativePath(components)

  fun contains(other: AbsolutePath) =
    this.components.count <= other.components.count &&
      this.components.zip(other.components).all { it.first == it.second }

  private val memoizedToString =
    memoize { "/" + components.joinToString(separator = "/") }

  override fun toString(): String =
    memoizedToString.value

  companion object {
    fun parse(string: String): AbsolutePath {
      if (string == "/") {
        return AbsolutePath()
      }
      if (string.first() != '/') {
        throw PathParseException("Absolute path must begin with a '/'")
      }
      if (string.last() == '/') {
        throw PathParseException("Absolute path cannot end with a '/'")
      }

      val components = string.split('/')
      val parsedComponents = ImmutableList.builder<PathComponent>()

      components.drop(1).filter { it != "." }.forEach { component ->
        if (component == "..") {
          if (parsedComponents.count == 0) {
            throw PathParseException(
              "Unable to parse absolute path: Too many \"..\" components, would access outside the file system.")
          } else {
            parsedComponents.removeAt(parsedComponents.count - 1)
          }
        } else {
          parsedComponents.add(
            try {
              PathComponent(component)
            } catch (e: PathParseException) {
              throw PathParseException("Unable to parse absolute path: \"$string\". ${e.message}", e)
            })
        }
      }

      return AbsolutePath(parsedComponents.build())
    }
  }
}

fun String.asAbsolutePath() =
  AbsolutePath.parse(this)
