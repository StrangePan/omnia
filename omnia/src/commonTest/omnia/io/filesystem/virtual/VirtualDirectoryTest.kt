package omnia.io.filesystem.virtual

import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isNotNull
import omnia.util.test.fluent.isNull

class VirtualDirectoryTest {

  val fileSystem = VirtualFileSystem()
  val parentDirectory = fileSystem.createDirectory("/parent")
  val underTest = fileSystem.createDirectory("/parent/directory")

  @Test
  fun parentDirectory_returnsParent() {
    assertThat(underTest.parentDirectory)
      .isNotNull()
      .andThat(Directory::name) { it.isEqualTo("parent") }
  }

  @Test
  fun parentDirectory_whenRoot_returnsNull() {
    assertThat(fileSystem.rootDirectory.parentDirectory).isNull()
  }

  @Test
  fun parentDirectories_returnsAllParents() {
    assertThat(underTest.parentDirectories.toImmutableList())
      .isEqualTo(ImmutableList.of(parentDirectory, fileSystem.rootDirectory))
  }

  @Test
  fun files_whenEmpty_returnsNothing() {
    assertThat(underTest.files).isEmpty()
  }

  @Test
  fun files_returnsAllFiles() {
    val firstFile = underTest.createFile("first")
    val secondFile = underTest.createFile("second")
    val thirdFile = underTest.createFile("third")

    assertThat(underTest.files.toImmutableSet()).containsExactly(firstFile, secondFile, thirdFile)
  }

  @Test
  fun subdirectories_whenEmpty_returnsNothing() {
    assertThat(underTest.subdirectories).isEmpty()
  }

  @Test
  fun subdirectories_returnsAllSubdirectories() {
    val firstSubdirectory = underTest.createSubdirectory("first")
    val secondSubdirectory = underTest.createSubdirectory("second")
    val thirdSubdirectory = underTest.createSubdirectory("third")

    assertThat(underTest.subdirectories.toImmutableSet())
      .containsExactly(firstSubdirectory, secondSubdirectory, thirdSubdirectory)
  }

  @Test
  fun createFile_returnsCreatedFile() {
    assertThat(underTest.createFile("file"))
      .andThat(File::name) { it.isEqualTo("file") }
      .andThat(File::fullName) { it.isEqualTo("/parent/directory/file") }
      .andThat(File::directory) { it.isEqualTo(underTest) }
      .andThat(File::readLines) { it.actual.test().assertNoValues().assertComplete() }
  }

  @Test
  fun createFile_whenFileAlreadyExists_fails() {
    underTest.createFile("file")
    assertThat { underTest.createFile("file") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_whenSubdirectoryAlreadyExists_fails() {
    underTest.createSubdirectory("object")
    assertThat { underTest.createFile("object") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_specifyingSubdirectory_whenNotExists_fails() {
    assertThat { underTest.createFile("subdirectory/file") }
      .failsWith(FileNotFoundException::class)
  }

  @Test
  fun createSubdirectory_returnsCreatedDirectory() {
    assertThat(underTest.createSubdirectory("subdirectory"))
      .andThat(Directory::name) { it.isEqualTo("subdirectory") }
      .andThat(Directory::fullName) { it.isEqualTo("/parent/directory/subdirectory") }
      .andThat(Directory::files) { it.isEmpty() }
      .andThat(Directory::subdirectories) { it.isEmpty() }
      .andThat(Directory::parentDirectory) { it.isNotNull().isEqualTo(underTest) }
  }

  @Test
  fun createSubdirectory_whenSubdirectoryAlreadyExists_fails() {
    underTest.createSubdirectory("subdirectory")
    assertThat { underTest.createSubdirectory("subdirectory") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createSubdirectory_whenFileAlreadyExists_fails() {
    underTest.createFile("object")
    assertThat { underTest.createSubdirectory("object") }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createSubdirectory_specifyingSubdirectory_whenNotExists_fails() {
    assertThat { underTest.createFile("subdirectory/subdirectory") }
      .failsWith(FileNotFoundException::class)
  }
}
