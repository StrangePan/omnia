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
    assertThat(underTest.isDirectory("/".asAbsolutePath())).isTrue()
  }

  @Test
  fun workingDirectory_default_isRootDirectory() {
    assertThat(underTest.workingDirectory).isSameAs(underTest.rootDirectory)
  }

  @Test
  fun workingDirectory_whenSpecified_createsParentDirectories() {
    val underTest = VirtualFileSystem("/working/directory/path".asAbsolutePath())

    assertThat(underTest.workingDirectory.fullPath.toString()).isEqualTo("/working/directory/path")
    assertThat(underTest.isDirectory("/working".asAbsolutePath())).isTrue()
    assertThat(underTest.isDirectory("/working/directory".asAbsolutePath())).isTrue()
    assertThat(underTest.isDirectory("/working/directory/path".asAbsolutePath())).isTrue()
  }

  @Test
  fun createDirectory_returnsCreatedEmptyDirectory() {
    assertThat(underTest.createDirectory("/directory".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/directory") }
      .andThat(Directory::files) { it.isEmpty() }
      .andThat(Directory::subdirectories) { it.isEmpty() }
  }

  @Test
  fun createDirectory_inSubdirectory_returnsCreatedDirectory() {
    underTest.createDirectory("/some".asAbsolutePath())
    assertThat(underTest.createDirectory("/some/directory".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/some/directory") }
  }

  @Test
  fun createDirectory_withoutCreatingParentDirectories_fails() {
    assertThat { underTest.createDirectory("/some/directory".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
      .hasMessageThat {
        it.isNotNull()
          .contains("/some")
      }
  }

  @Test
  fun createDirectory_withSamePathAsExistingFile_fails() {
    val path = "/object".asAbsolutePath()
    underTest.createFile(path)
    assertThat { underTest.createDirectory(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createDirectory_withSamePathAsExistingDirectory_fails() {
    val path = "/directory".asAbsolutePath()
    underTest.createDirectory(path)
    assertThat { underTest.createDirectory(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_returnsCreatedEmptyFile() {
    assertThat(underTest.createFile("/file".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/file") }
      .andThat(File::readLines) { it.actual.test().assertNoValues().assertComplete() }
  }

  @Test
  fun createFile_inSubdirectory_returnsCreatedFile() {
    underTest.createDirectory("/some".asAbsolutePath())
    assertThat(underTest.createFile("/some/file".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/some/file") }
  }

  @Test
  fun createFile_withoutCreatingParentDirectories_fails() {
    assertThat { underTest.createFile("/some/file".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
      .hasMessageThat {
        it.isNotNull()
          .containsIgnoringCase("directory")
          .contains("/some")
      }
  }

  @Test
  fun createFile_withSamePathAsExistingDirectory_fails() {
    val path = "/object".asAbsolutePath()
    underTest.createDirectory(path)
    assertThat { underTest.createDirectory(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_withSamePathAsExistingFile_fails() {
    val path = "/file".asAbsolutePath()
    underTest.createFile(path)
    assertThat { underTest.createFile(path) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun objectExistsAt_whenNotExists_isFalse() {
    assertThat(underTest.objectExistsAt("/object".asAbsolutePath())).isFalse()
  }

  @Test
  fun objectExistsAt_whenExistsAndIsDirectory_isTrue() {
    underTest.createDirectory("/directory".asAbsolutePath())
    assertThat(underTest.objectExistsAt("/directory".asAbsolutePath())).isTrue()
  }

  @Test
  fun objectExistsAt_whenExistsAndIsFile_isTrue() {
    underTest.createFile("/file".asAbsolutePath())
    assertThat(underTest.objectExistsAt("/file".asAbsolutePath())).isTrue()
  }

  @Test
  fun isDirectory_whenNotExists_isFalse() {
    assertThat(underTest.isDirectory("/directory".asAbsolutePath())).isFalse()
  }

  @Test
  fun isDirectory_whenExistsAndIsDirectory_isTrue() {
    underTest.createDirectory("/directory".asAbsolutePath())
    assertThat(underTest.isDirectory("/directory".asAbsolutePath())).isTrue()
  }

  @Test
  fun isDirectory_whenIsFile_isFalse() {
    underTest.createFile("/object".asAbsolutePath())
    assertThat(underTest.isDirectory("/object".asAbsolutePath())).isFalse()
  }

  @Test
  fun isFile_whenNotExists_isFalse() {
    assertThat(underTest.isFile("/file".asAbsolutePath())).isFalse()
  }

  @Test
  fun isFile_whenExistsAndIsDirectory_isTrue() {
    underTest.createFile("/file".asAbsolutePath())
    assertThat(underTest.isFile("/file".asAbsolutePath())).isTrue()
  }

  @Test
  fun isFile_whenIsDirectory_isFalse() {
    underTest.createDirectory("/object".asAbsolutePath())
    assertThat(underTest.isFile("/object".asAbsolutePath())).isFalse()
  }

  @Test
  fun getObjectAt_whenExistsAndIsDirectory_returnsObject() {
    underTest.createDirectory("/directory".asAbsolutePath())
    assertThat(underTest.getObjectAt("/directory".asAbsolutePath()))
      .isA(VirtualDirectory::class)
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/directory") }
  }

  @Test
  fun getObjectAt_whenExistsAndIsFile_returnsObject() {
    underTest.createFile("/file".asAbsolutePath())
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
  fun getDirectory_whenExists_returnsDirectory() {
    underTest.createDirectory("/directory".asAbsolutePath())
    assertThat(underTest.getDirectory("/directory".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/directory") }
  }

  @Test
  fun getDirectory_whenNotExists_fails() {
    assertThat { underTest.getDirectory("/directory".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getDirectory_whenIsFile_fails() {
    underTest.createFile("/object".asAbsolutePath())
    assertThat { underTest.getDirectory("/object".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFile_whenExists_returnsFile() {
    underTest.createFile("/file".asAbsolutePath())
    assertThat(underTest.getFile("/file".asAbsolutePath()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/file") }
  }

  @Test
  fun getFile_whenNotExists_fails() {
    assertThat { underTest.getFile("/file".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun getFile_whenIsDirectory_fails() {
    underTest.createDirectory("/object".asAbsolutePath())
    assertThat { underTest.getFile("/object".asAbsolutePath()) }
      .failsWith(FileNotFoundException::class)
  }
}
