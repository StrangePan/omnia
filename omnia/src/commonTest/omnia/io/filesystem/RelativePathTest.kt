package omnia.io.filesystem

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo

class RelativePathTest {

  @Test
  fun parse_withNoParentDirectoryComponents_producesExpectedPath() {
    assertThat(RelativePath.parse("l/m/a/o"))
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(0) }
      .andThat({ it.components.map(PathComponent::name) }) { it.containsExactly("l", "m", "a", "o") }
  }

  @Test
  fun parse_withParentDirectoryComponents_atFront_producesExpectedPath() {
    assertThat(RelativePath.parse("../../hey"))
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(2) }
      .andThat({ it.components.map(PathComponent::name) }) { it.containsExactly("hey") }
  }

  @Test
  fun parse_withParentDirectoryComponents_inMiddle_producesExpectedPath() {
    assertThat(RelativePath.parse("../../a/../../1/2/3"))
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(3) }
      .andThat({ it.components.map(PathComponent::name) }) { it.containsExactly("1", "2", "3") }
  }

  @Test
  fun parse_withCurrentDirectoryComponents_producesExpectedPath() {
    assertThat(RelativePath.parse("./some/path/./with/dots"))
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(0) }
      .andThat({ it.components.map(PathComponent::name) }) { it.containsExactly("some", "path", "with", "dots") }
  }

  @Test
  fun parse_withEmptyString_producesEmptyPath() {
    assertThat(RelativePath.parse(""))
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(0) }
      .andThat({ it.components }) { it.isEmpty() }
  }

  @Test
  fun construct_withNoComponents_producesEmptyPath() {
    assertThat(RelativePath())
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(0) }
      .andThat(RelativePath::components) { it.isEmpty() }
  }

  @Test
  fun construct_withComponents_producesExpectedPath() {
    assertThat(RelativePath(2, "some", "path"))
      .andThat(RelativePath::trimmedParents) { it.isEqualTo(2) }
      .andThat({ it.components.map(PathComponent::name) }) { it.containsExactly("some", "path") }
  }

  @Test
  fun plus_emptyRelativePath_returnsExpectedPath() {
    assertThat(RelativePath(3, "some", "path") + RelativePath())
      .isEqualTo(RelativePath.parse("../../../some/path"))
  }

  @Test
  fun plus_string_combinesPaths() {
    assertThat(RelativePath.parse("../some/path") + "../merged/path")
      .isEqualTo(RelativePath.parse("../some/merged/path"))
  }

  @Test
  fun plus_relativePath_combinesPaths() {
    assertThat(RelativePath.parse("some/path") + RelativePath.parse("../merged/path"))
      .isEqualTo(RelativePath.parse("some/merged/path"))
  }

  @Test
  fun plus_withManyParentComponents_combinesTrimmedParentParts() {
    assertThat(RelativePath.parse("../../../yo") + RelativePath.parse("../../dude"))
      .isEqualTo(RelativePath.parse("../../../../dude"))
  }

  @Test
  fun toString_producesSimplifiedString() {
    assertThat(RelativePath.parse("./../this/./is/a/./pretty/../simplified/relative/string/../../path").toString())
      .isEqualTo("../this/is/a/simplified/path")
  }
}
