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
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent
import omnia.io.filesystem.getFile
import omnia.io.filesystem.getOrCreateFile
import omnia.io.filesystem.getOrCreateSubdirectory
import omnia.io.filesystem.getSubdirectory
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
  val parentDirectory = fileSystem.createDirectory("/parent".asAbsolutePath())
  val underTest = fileSystem.createDirectory("/parent/directory".asAbsolutePath())

  @Test
  fun parentDirectory_returnsParent() {
    assertThat(underTest.parentDirectory)
      .isNotNull()
      .andThat({ it.name.toString() }) { it.isEqualTo("parent") }
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
    val firstFile = underTest.createFile("first".asPathComponent())
    val secondFile = underTest.createFile("second".asPathComponent())
    val thirdFile = underTest.createFile("third".asPathComponent())

    assertThat(underTest.files.toImmutableSet()).containsExactly(firstFile, secondFile, thirdFile)
  }

  @Test
  fun files_whenRoot_returnsAllFiles() {
    val underTest = fileSystem.rootDirectory

    val firstFile = underTest.createFile("first".asPathComponent())
    val secondFile = underTest.createFile("second".asPathComponent())
    val thirdFile = underTest.createFile("third".asPathComponent())

    assertThat(underTest.files.toImmutableSet()).containsExactly(firstFile, secondFile, thirdFile)
  }

  @Test
  fun subdirectories_whenEmpty_returnsNothing() {
    assertThat(underTest.subdirectories).isEmpty()
  }

  @Test
  fun subdirectories_returnsAllSubdirectories() {
    val firstSubdirectory = underTest.createSubdirectory("first".asPathComponent())
    val secondSubdirectory = underTest.createSubdirectory("second".asPathComponent())
    val thirdSubdirectory = underTest.createSubdirectory("third".asPathComponent())

    assertThat(underTest.subdirectories.toImmutableSet())
      .containsExactly(firstSubdirectory, secondSubdirectory, thirdSubdirectory)
  }

  @Test
  fun createFile_returnsCreatedFile() {
    assertThat(underTest.createFile("file".asPathComponent()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/parent/directory/file") }
      .andThat(File::directory) { it.isEqualTo(underTest) }
      .andThat(File::readLines) { it.actual.test().assertNoValues().assertComplete() }
  }

  @Test
  fun createFile_whenRoot_returnsCreatedFile() {
    val underTest = fileSystem.rootDirectory

    assertThat(underTest.createFile("file".asPathComponent()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/file") }
      .andThat(File::directory) { it.isEqualTo(underTest) }
      .andThat(File::readLines) { it.actual.test().assertNoValues().assertComplete() }
  }

  @Test
  fun createFile_whenFileAlreadyExists_fails() {
    underTest.createFile("file".asPathComponent())
    assertThat { underTest.createFile("file".asPathComponent()) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createFile_whenSubdirectoryAlreadyExists_fails() {
    underTest.createSubdirectory("object".asPathComponent())
    assertThat { underTest.createFile("object".asPathComponent()) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createSubdirectory_returnsCreatedDirectory() {
    assertThat(underTest.createSubdirectory("subdirectory".asPathComponent()))
      .andThat({ it.name.toString() }) { it.isEqualTo("subdirectory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/parent/directory/subdirectory") }
      .andThat(Directory::files) { it.isEmpty() }
      .andThat(Directory::subdirectories) { it.isEmpty() }
      .andThat(Directory::parentDirectory) { it.isNotNull().isEqualTo(underTest) }
  }

  @Test
  fun createSubdirectory_whenRoot_returnsCreatedDirectory() {
    val underTest = fileSystem.rootDirectory

    assertThat(underTest.createSubdirectory("subdirectory".asPathComponent()))
      .andThat({ it.name.toString() }) { it.isEqualTo("subdirectory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/subdirectory") }
      .andThat(Directory::files) { it.isEmpty() }
      .andThat(Directory::subdirectories) { it.isEmpty() }
      .andThat(Directory::parentDirectory) { it.isNotNull().isEqualTo(underTest) }
  }

  @Test
  fun createSubdirectory_whenSubdirectoryAlreadyExists_fails() {
    underTest.createSubdirectory("subdirectory".asPathComponent())
    assertThat { underTest.createSubdirectory("subdirectory".asPathComponent()) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun createSubdirectory_whenFileAlreadyExists_fails() {
    underTest.createFile("object".asPathComponent())
    assertThat { underTest.createSubdirectory("object".asPathComponent()) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun getOrCreateFile_whenNotExists_returnsCreatedDirectory() {
    assertThat(underTest.getOrCreateFile("file".asPathComponent()))
      .andThat({ it.name.toString() }) { it.isEqualTo("file") }
      .andThat(File::directory) { it.isEqualTo(underTest) }
  }

  @Test
  fun getOrCreateFile_whenExists_returnsExistingFile() {
    val file = underTest.createFile("file".asPathComponent())
    assertThat(underTest.getOrCreateFile("file".asPathComponent()))
      .isEqualTo(file)
  }

  @Test
  fun getOrCreateFile_whenIsDirectory_fails() {
    underTest.createSubdirectory("object".asPathComponent())
    assertThat { underTest.getOrCreateFile("object".asPathComponent()) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun getFile_whenExists_returnsFile() {
    val file = underTest.createFile("file".asPathComponent())
    assertThat(underTest.getFile("file".asPathComponent()))
      .isNotNull()
      .isEqualTo(file)
  }

  @Test
  fun getFile_whenNotExists_returnsNull() {
    assertThat(underTest.getFile("file".asPathComponent())).isNull()
  }

  @Test
  fun getFile_whenIsSubdirectory_returnsNull() {
    underTest.createSubdirectory("object".asPathComponent())
    assertThat(underTest.getFile("object".asPathComponent())).isNull()
  }

  @Test
  fun getOrCreateSubdirectory_whenNotExists_returnsCreatedDirectory() {
    assertThat(underTest.getOrCreateSubdirectory("subdirectory".asPathComponent()))
      .andThat({ it.name.toString() }) { it.isEqualTo("subdirectory") }
      .andThat(Directory::parentDirectory) { it.isNotNull().isEqualTo(underTest) }
  }

  @Test
  fun getOrCreateSubdirectory_whenExists_returnsExistingSubdirectory() {
    val subdirectory = underTest.createSubdirectory("subdirectory".asPathComponent())
    assertThat(underTest.getOrCreateSubdirectory("subdirectory".asPathComponent()))
      .isEqualTo(subdirectory)
  }

  @Test
  fun getOrCreateSubdirectory_whenIsFile_fails() {
    underTest.createFile("object".asPathComponent())
    assertThat { underTest.getOrCreateSubdirectory("object".asPathComponent()) }
      .failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun getSubdirectory_whenExists_returnsSubdirectory() {
    val subdirectory = underTest.createSubdirectory("subdirectory".asPathComponent())
    assertThat(underTest.getSubdirectory("subdirectory".asPathComponent()))
      .isNotNull()
      .isEqualTo(subdirectory)
  }

  @Test
  fun getSubdirectory_whenNotExists_returnsNull() {
    assertThat(underTest.getSubdirectory("subdirectory".asPathComponent())).isNull()
  }

  @Test
  fun getSubdirectory_whenIsFile_returnsNull() {
    underTest.createFile("object".asPathComponent())
    assertThat(underTest.getSubdirectory("object".asPathComponent())).isNull()
  }
}
