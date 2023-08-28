package omnia.io.filesystem.virtual

import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileNotFoundException
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsIgnoringCase
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasMessageThat
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isNotNull
import omnia.util.test.fluent.isSameAs
import omnia.util.test.fluent.isTrue

class VirtualFileSystemTest {

  val underTest = VirtualFileSystem()

  @Test
  fun rootDirectory_isCreated() {
    assertThat(underTest.rootDirectory.fullName).isEqualTo("/")
    assertThat(underTest.rootDirectory.name).isEqualTo("")
    assertThat(underTest.isDirectory("/")).isTrue()
  }

  @Test
  fun workingDirectory_default_isRootDirectory() {
    assertThat(underTest.workingDirectory).isSameAs(underTest.rootDirectory)
  }

  @Test
  fun workingDirectory_whenSpecified_createsParentDirectories() {
    val underTest = VirtualFileSystem("/working/directory/path")

    assertThat(underTest.workingDirectory.fullName).isEqualTo("/working/directory/path")
    assertThat(underTest.isDirectory("/working")).isTrue()
    assertThat(underTest.isDirectory("/working/directory")).isTrue()
    assertThat(underTest.isDirectory("/working/directory/path")).isTrue()
  }

  @Test
  fun createDirectory_returnsCreatedEmptyDirectory() {
    assertThat(underTest.createDirectory("/directory"))
      .andThat(Directory::name) { it.isEqualTo("directory") }
      .andThat(Directory::fullName) { it.isEqualTo("/directory") }
      .andThat(Directory::files) { it.isEmpty() }
      .andThat(Directory::subdirectories) { it.isEmpty() }
  }

  @Test
  fun createDirectory_withoutCreatingParentDirectories_fails() {
    assertThat { underTest.createDirectory("/some/directory") }
      .failsWith(FileNotFoundException::class)
      .hasMessageThat {
        it.isNotNull()
          .contains("/some")
      }
  }

  @Test
  fun createFile_returnsCreatedEmptyFile() {
    assertThat(underTest.createFile("/file"))
      .andThat(File::name) { it.isEqualTo("file") }
      .andThat(File::fullName) { it.isEqualTo("/file") }
      .andThat { file ->
        file.readLines().test().assertNoValues().assertComplete()
      }
  }

  @Test
  fun createFile_withoutCreatingParentDirectories_fails() {
    assertThat { underTest.createFile("/some/file") }
      .failsWith(FileNotFoundException::class)
      .hasMessageThat {
        it.isNotNull()
          .containsIgnoringCase("directory")
          .contains("/some")
      }
  }

  // TODO add more tests
}
