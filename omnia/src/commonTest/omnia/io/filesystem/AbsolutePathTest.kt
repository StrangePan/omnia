package omnia.io.filesystem

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isTrue

class AbsolutePathTest {

  @Test
  fun parse_withoutLeadingSlash_fails() {
    assertThat { AbsolutePath.parse("some/path") }.failsWith(PathParseException::class)
  }

  @Test
  fun parse_withTrailingSlash_fails() {
    assertThat { AbsolutePath.parse("/some/path/") }.failsWith(PathParseException::class)
  }

  @Test
  fun parse_empty_isRoot() {
    assertThat(AbsolutePath.parse("/").isRoot).isTrue()
  }

  @Test
  fun parse_empty_hasNoComponents() {
    assertThat(AbsolutePath.parse("/").components).isEmpty()
  }

  @Test
  fun parse_nonEmpty_isNotRoot() {
    assertThat(AbsolutePath.parse("/abc").isRoot).isFalse()
  }

  @Test
  fun parse_nonEmpty_matchesComponents() {
    assertThat(AbsolutePath.parse("/first/second/third").components.map(PathComponent::name))
      .containsExactly("first", "second", "third")
  }

  @Test
  fun parse_withCurrentDirectoryComponents_trimsRedundantComponents() {
    assertThat(AbsolutePath.parse("/./some/path/.that/./has/dots").components.map(PathComponent::name))
      .containsExactly("some", "path", ".that", "has", "dots")
  }

  @Test
  fun parse_withParentDirectoryComponents_trimsRedundantComponents() {
    assertThat(AbsolutePath.parse("/some/../some/redundant/../path/that/navigates/../goes/up").components.map(PathComponent::name))
      .containsExactly("some", "path", "that", "goes", "up")
  }

  @Test
  fun parse_withTooManyParentDirectoryComponents_fails() {
    assertThat { AbsolutePath.parse("/some/path/with/../../../too/../../many/parentDirectoryComponents") }
      .failsWith(PathParseException::class)
  }

  @Test
  fun construct_withCurrentDirectoryComponent_fails() {
    assertThat { AbsolutePath(".", "some", "path") }.failsWith(PathParseException::class)
  }

  @Test
  fun construct_withParentDirectoryComponent_fails() {
    assertThat { AbsolutePath("..", "some", "path") }.failsWith(PathParseException::class)
  }

  @Test
  fun construct_withDirectoryDelimiterComponent_fails() {
    assertThat { AbsolutePath("some", "/", "path") }.failsWith(PathParseException::class)
  }

  @Test
  fun construct_withComponents_isNotRoot() {
    assertThat(AbsolutePath("some", "path").isRoot).isFalse()
  }

  @Test
  fun construct_withComponents_matchesComponents() {
    assertThat(AbsolutePath("some", "path").components.map(PathComponent::name))
      .containsExactly("some", "path")
  }

  @Test
  fun plus_relativePath_producesExpectedPath() {
    assertThat(AbsolutePath("some", "path") + RelativePath("with", "extra", "components"))
      .isEqualTo(AbsolutePath.parse("/some/path/with/extra/components"))
  }

  @Test
  fun plus_relativePath_withParentDirectoryComponents_producesExpectedPath() {
    assertThat(AbsolutePath.parse("/some/absolute/path") + RelativePath.parse("../../concatenated/path"))
      .isEqualTo(AbsolutePath.parse("/some/concatenated/path"))
  }

  @Test
  fun plus_relativePath_withTooManyParentDirectoryComponents_fails() {
    assertThat { AbsolutePath.parse("/some/path") + RelativePath(3) }
      .failsWith(IllegalArgumentException::class)
  }

  @Test
  fun toString_producesSimplifiedPath() {
    assertThat(AbsolutePath.parse("/some/absolute/../path/./that/has/been/../../is/simplified").toString())
      .isEqualTo("/some/path/that/is/simplified")
  }
}
