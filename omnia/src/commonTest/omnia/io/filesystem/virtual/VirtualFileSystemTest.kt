package omnia.io.filesystem.virtual

import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.asAbsolutePath
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsIgnoringCase
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasMessageThat
import omnia.util.test.fluent.isA
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
    assertThat(underTest.rootDirectory.fullPath.toString()).isEqualTo("/")
    assertThat { underTest.rootDirectory.name }.failsWith(Throwable::class)
    assertThat(underTest.directoryExistsAt("/".asAbsolutePath())).isTrue()
  }

  @Test
  fun workingDirectory_default_isRootDirectory() {
    assertThat(underTest.workingDirectory).isSameAs(underTest.rootDirectory)
  }

  @Test
  fun workingDirectoryAt_whenSpecified_createsParentDirectories() {
    val underTest = VirtualFileSystem("/working/directory/path".asAbsolutePath())

    assertThat(underTest.workingDirectory.fullPath.toString()).isEqualTo("/working/directory/path")
    assertThat(underTest.directoryExistsAt("/working".asAbsolutePath())).isTrue()
    assertThat(underTest.directoryExistsAt("/working/directory".asAbsolutePath())).isTrue()
    assertThat(underTest.directoryExistsAt("/working/directory/path".asAbsolutePath())).isTrue()
  }

  @Test
  fun createDirectoryAt_returnsCreatedEmptyDirectory() {
    assertThat(underTest.createDirectoryAt("/directory".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/directory") }
      .andThat(Directory::files) { it.isEmpty() }
      .andThat(Directory::subdirectories) { it.isEmpty() }
  }

  @Test
  fun createDirectoryAt_inSubdirectory_returnsCreatedDirectory() {
    underTest.createDirectoryAt("/some".asAbsolutePath())
    assertThat(underTest.createDirectoryAt("/some/directory".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/some/directory") }
  }

  @Test
  fun createDirectoryAt_withoutCreatingParentDirectories_fails() {
    assertThat { underTest.createDirectoryAt("/some/directory".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
      .hasMessageThat {
        it.isNotNull()
          .contains("/some")
      }
  }

  @Test
  fun createDirectoryAt_withSamePathAsExistingFile_fails() {
    val path = "/object".asAbsolutePath()
    underTest.createFileAt(path)
    assertThat { underTest.createDirectoryAt(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createDirectoryAt_withSamePathAsExistingDirectory_fails() {
    val path = "/directory".asAbsolutePath()
    underTest.createDirectoryAt(path)
    assertThat { underTest.createDirectoryAt(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFileAt_returnsCreatedEmptyFile() {
    assertThat(underTest.createFileAt("/file".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/file") }
      .andThat(File::readLines) { it.actual.test().assertNoValues().assertComplete() }
  }

  @Test
  fun createFileAt_inSubdirectory_returnsCreatedFile() {
    underTest.createDirectoryAt("/some".asAbsolutePath())
    assertThat(underTest.createFileAt("/some/file".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/some/file") }
  }

  @Test
  fun createFileAt_withoutCreatingParentDirectories_fails() {
    assertThat { underTest.createFileAt("/some/file".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
      .hasMessageThat {
        it.isNotNull()
          .containsIgnoringCase("directory")
          .contains("/some")
      }
  }

  @Test
  fun createFileAt_withSamePathAsExistingDirectory_fails() {
    val path = "/object".asAbsolutePath()
    underTest.createDirectoryAt(path)
    assertThat { underTest.createDirectoryAt(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFileAt_withSamePathAsExistingFile_fails() {
    val path = "/file".asAbsolutePath()
    underTest.createFileAt(path)
    assertThat { underTest.createFileAt(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun objectExistsAt_whenNotExists_isFalse() {
    assertThat(underTest.objectExistsAt("/object".asAbsolutePath())).isFalse()
  }

  @Test
  fun objectExistsAt_whenExistsAndIsDirectory_isTrue() {
    underTest.createDirectoryAt("/directory".asAbsolutePath())
    assertThat(underTest.objectExistsAt("/directory".asAbsolutePath())).isTrue()
  }

  @Test
  fun objectExistsAt_whenExistsAndIsFile_isTrue() {
    underTest.createFileAt("/file".asAbsolutePath())
    assertThat(underTest.objectExistsAt("/file".asAbsolutePath())).isTrue()
  }

  @Test
  fun directoryExistsAt_whenNotExists_isFalse() {
    assertThat(underTest.directoryExistsAt("/directory".asAbsolutePath())).isFalse()
  }

  @Test
  fun directoryExistsAt_whenExistsAndIsDirectory_isTrue() {
    underTest.createDirectoryAt("/directory".asAbsolutePath())
    assertThat(underTest.directoryExistsAt("/directory".asAbsolutePath())).isTrue()
  }

  @Test
  fun directoryExistsAt_whenIsFile_isFalse() {
    underTest.createFileAt("/object".asAbsolutePath())
    assertThat(underTest.directoryExistsAt("/object".asAbsolutePath())).isFalse()
  }

  @Test
  fun fileExistsAt_whenNotExists_isFalse() {
    assertThat(underTest.fileExistsAt("/file".asAbsolutePath())).isFalse()
  }

  @Test
  fun fileExistsAt_whenExistsAndIsDirectory_isTrue() {
    underTest.createFileAt("/file".asAbsolutePath())
    assertThat(underTest.fileExistsAt("/file".asAbsolutePath())).isTrue()
  }

  @Test
  fun fileExistsAt_whenIsDirectory_isFalse() {
    underTest.createDirectoryAt("/object".asAbsolutePath())
    assertThat(underTest.fileExistsAt("/object".asAbsolutePath())).isFalse()
  }

  @Test
  fun getObjectAt_whenExistsAndIsDirectory_returnsObject() {
    underTest.createDirectoryAt("/directory".asAbsolutePath())
    assertThat(underTest.getObjectAt("/directory".asAbsolutePath()))
      .isA(VirtualDirectory::class)
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/directory") }
  }

  @Test
  fun getObjectAt_whenExistsAndIsFile_returnsObject() {
    underTest.createFileAt("/file".asAbsolutePath())
    assertThat(underTest.getObjectAt("/file".asAbsolutePath()))
      .isA(VirtualFile::class)
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/file") }
  }

  @Test
  fun getObjectAt_whenNotExists_fails() {
    assertThat { underTest.getObjectAt("/object".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getDirectoryAt_whenExists_returnsDirectory() {
    underTest.createDirectoryAt("/directory".asAbsolutePath())
    assertThat(underTest.getDirectoryAt("/directory".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/directory") }
  }

  @Test
  fun getDirectoryAt_whenNotExists_fails() {
    assertThat { underTest.getDirectoryAt("/directory".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getDirectoryAt_whenIsFile_fails() {
    underTest.createFileAt("/object".asAbsolutePath())
    assertThat { underTest.getDirectoryAt("/object".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFileAt_whenExists_returnsFile() {
    underTest.createFileAt("/file".asAbsolutePath())
    assertThat(underTest.getFileAt("/file".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/file") }
  }

  @Test
  fun getFileAt_whenNotExists_fails() {
    assertThat { underTest.getFileAt("/file".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFileAt_whenIsDirectory_fails() {
    underTest.createDirectoryAt("/object".asAbsolutePath())
    assertThat { underTest.getFileAt("/object".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }
}
