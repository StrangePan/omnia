package omnia.io.filesystem.resources

import kotlin.test.Test
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.NotADirectoryException
import omnia.io.filesystem.NotAFileException
import omnia.io.filesystem.asAbsolutePath
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isA
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isTrue

class ResourceFileSystemTest {

  val underTest = ResourceFileSystem.instance
  val resourceDirectory = "/io/filesystem/resources".asAbsolutePath()
  val existingFile = resourceDirectory + "existing"
  val missingFile = resourceDirectory + "missing"

  @Test
  fun directoryExists_whenExists_isTrue() {
    assertThat(underTest.directoryExistsAt(resourceDirectory)).isTrue()
  }

  @Test
  fun directoryExists_whenMissing_isFalse() {
    assertThat(underTest.directoryExistsAt(missingFile)).isFalse()
  }

  @Test
  fun directoryExists_whenFile_isFalse() {
    assertThat(underTest.directoryExistsAt(existingFile)).isFalse()
  }

  @Test
  fun fileExists_whenExists_isTrue() {
    assertThat(underTest.fileExistsAt(existingFile)).isTrue()
  }

  @Test
  fun fileExists_whenMissing_isFalse() {
    assertThat(underTest.fileExistsAt(missingFile)).isFalse()
  }

  @Test
  fun fileExists_whenDirectory_isFalse() {
    assertThat(underTest.fileExistsAt(resourceDirectory)).isFalse()
  }

  @Test
  fun getDirectory_whenExists_succeeds() {
    assertThat(underTest.getDirectoryAt(resourceDirectory)).isA(Directory::class)
  }

  @Test
  fun getDirectory_whenMissing_fails() {
    assertThat { underTest.getDirectoryAt(missingFile) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun getDirectory_whenFile_fails() {
    assertThat { underTest.getDirectoryAt(existingFile) }.failsWith(NotADirectoryException::class)
  }

  @Test
  fun getFile_whenExists_succeeds() {
    assertThat(underTest.getFileAt(existingFile)).isA(File::class)
  }

  @Test
  fun getFile_whenMissing_fails() {
    assertThat { underTest.getFileAt(missingFile) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFile_whenDirectory_fails() {
    assertThat { underTest.getFileAt(resourceDirectory) }.failsWith(NotAFileException::class)
  }
}
