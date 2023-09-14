package omnia.io.filesystem

import kotlin.test.Test
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEqualTo

class PathComponentTest {

  @Test
  fun pathComponent_containsName() {
    assertThat(PathComponent("component").name).isEqualTo("component")
  }

  @Test
  fun pathComponent_thisDirectory_fails() {
    assertThat { PathComponent(".") }.failsWith(PathParseException::class)
  }

  @Test
  fun pathComponent_parentDirectory_fails() {
    assertThat { PathComponent("..") }.failsWith(PathParseException::class)
  }

  @Test
  fun pathComponent_withDirectoryDelimiter_fails() {
    assertThat { PathComponent("as/df") }.failsWith(PathParseException::class)
  }
}
