package omnia.io.filesystem.virtual

import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsIgnoringCase
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasMessageThat
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
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
  fun createDirectory_inSubdirectory_returnsCreatedDirectory() {
    underTest.createDirectory("/some")
    assertThat(underTest.createDirectory("/some/directory"))
      .andThat(Directory::name) { it.isEqualTo("directory") }
      .andThat(Directory::fullName) { it.isEqualTo("/some/directory") }
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
  fun createDirectory_withoutLeadingSlash_fails() {
    assertThat { underTest.createDirectory("directory") }
      .failsWith(IllegalArgumentException::class)
  }

  @Test
  fun createDirectory_withTrailingSlash_fails() {
    assertThat { underTest.createDirectory("/directory/") }
      .failsWith(IllegalArgumentException::class)
  }

  @Test
  fun createDirectory_withSamePathAsExistingFile_fails() {
    underTest.createFile("/object")
    assertThat { underTest.createDirectory("/object") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createDirectory_withSamePathAsExistingDirectory_fails() {
    underTest.createDirectory("/directory")
    assertThat { underTest.createDirectory("/directory") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_returnsCreatedEmptyFile() {
    assertThat(underTest.createFile("/file"))
      .andThat(File::name) { it.isEqualTo("file") }
      .andThat(File::fullName) { it.isEqualTo("/file") }
      .andThat(File::readLines) { it.actual.test().assertNoValues().assertComplete() }
  }

  @Test
  fun createFile_inSubdirectory_returnsCreatedFile() {
    underTest.createDirectory("/some")
    assertThat(underTest.createFile("/some/file"))
      .andThat(File::name) { it.isEqualTo("file") }
      .andThat(File::fullName) { it.isEqualTo("/some/file") }
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

  @Test
  fun createFile_withoutLeadingSlash_fails() {
    assertThat { underTest.createFile("file") }
      .failsWith(IllegalArgumentException::class)
  }

  @Test
  fun createFile_withTrailingSlash_fails() {
    assertThat { underTest.createFile("/file/") }
      .failsWith(IllegalArgumentException::class)
  }

  @Test
  fun createFile_withSamePathAsExistingDirectory_fails() {
    underTest.createDirectory("/object")
    assertThat { underTest.createDirectory("/object") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_withSamePathAsExistingFile_fails() {
    underTest.createFile("/file")
    assertThat { underTest.createFile("/file") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun isDirectory_whenNotExists_isFalse() {
    assertThat(underTest.isDirectory("/directory")).isFalse()
  }

  @Test
  fun isDirectory_whenExistsAndIsDirectory_isTrue() {
    underTest.createDirectory("/directory")
    assertThat(underTest.isDirectory("/directory")).isTrue()
  }

  @Test
  fun isDirectory_whenIsFile_isFalse() {
    underTest.createFile("/object")
    assertThat(underTest.isDirectory("/object")).isFalse()
  }

  @Test
  fun isFile_whenNotExists_isFalse() {
    assertThat(underTest.isFile("/file")).isFalse()
  }

  @Test
  fun isFile_whenExistsAndIsDirectory_isTrue() {
    underTest.createFile("/file")
    assertThat(underTest.isFile("/file")).isTrue()
  }

  @Test
  fun isFile_whenIsDirectory_isFalse() {
    underTest.createDirectory("/object")
    assertThat(underTest.isFile("/object")).isFalse()
  }

  @Test
  fun getDirectory_whenExists_returnsDirectory() {
    underTest.createDirectory("/directory")
    assertThat(underTest.getDirectory("/directory"))
      .andThat(Directory::name) { it.isEqualTo("directory") }
      .andThat(Directory::fullName) { it.isEqualTo("/directory") }
  }

  @Test
  fun getDirectory_whenNotExists_fails() {
    assertThat { underTest.getDirectory("/directory") }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getDirectory_whenIsFile_fails() {
    underTest.createFile("/object")
    assertThat { underTest.getDirectory("/object") }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFile_whenExists_returnsFile() {
    underTest.createFile("/file")
    assertThat(underTest.getFile("/file"))
      .andThat(File::name) { it.isEqualTo("file") }
      .andThat(File::fullName) { it.isEqualTo("/file") }
  }

  @Test
  fun getFile_whenNotExists_fails() {
    assertThat { underTest.getFile("/file") }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFile_whenIsDirectory_fails() {
    underTest.createDirectory("/object")
    assertThat { underTest.getFile("/object") }
      .failsWith(FileNotFoundException::class)
  }
}
