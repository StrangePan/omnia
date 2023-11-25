package omnia.io.filesystem.virtual

import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.test.completable.assertComplete
import com.badoo.reaktive.test.completable.test
import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent
import omnia.io.filesystem.getFile
import omnia.io.filesystem.getOrCreateFile
import omnia.io.filesystem.getOrCreateSubdirectory
import omnia.io.filesystem.getSubdirectory
import omnia.io.filesystem.virtual.VirtualFileSystem.OnAfterCreateDirectory
import omnia.io.filesystem.virtual.VirtualFileSystem.OnBeforeCreateDirectory
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isNotNull
import omnia.util.test.fluent.isNull
import omnia.util.test.fluent.isTrue

class VirtualDirectoryTest {

  val fileSystem = VirtualFileSystem()
  val parentDirectory = fileSystem.createDirectoryAt("/parent".asAbsolutePath())
  val underTest = fileSystem.createDirectoryAt("/parent/directory".asAbsolutePath())

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
  fun contents_whenEmpty_returnsNothing() {
    assertThat(underTest.contents).isEmpty()
  }

  @Test
  fun contents_returnsAllFiles() {
    val firstFile = underTest.createFile("first_file".asPathComponent())
    val secondFile = underTest.createFile("second_file".asPathComponent())
    val thirdFile = underTest.createFile("third_file".asPathComponent())
    val firstSubdirectory = underTest.createSubdirectory("first_directory".asPathComponent())
    val secondSubdirectory = underTest.createSubdirectory("second_directory".asPathComponent())
    val thirdSubdirectory = underTest.createSubdirectory("third_directory".asPathComponent())

    assertThat(underTest.contents.toImmutableSet())
      .containsExactly(firstFile, secondFile, thirdFile, firstSubdirectory, secondSubdirectory, thirdSubdirectory)
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
    underTest.createSubdirectory("first_directory".asPathComponent())
    underTest.createSubdirectory("second_directory".asPathComponent())
    underTest.createSubdirectory("third_directory".asPathComponent())

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
    underTest.createFile("first_file".asPathComponent())
    underTest.createFile("second_file".asPathComponent())
    underTest.createFile("third_file".asPathComponent())
    val firstSubdirectory = underTest.createSubdirectory("first_directory".asPathComponent())
    val secondSubdirectory = underTest.createSubdirectory("second_directory".asPathComponent())
    val thirdSubdirectory = underTest.createSubdirectory("third_directory".asPathComponent())

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

  @Test
  fun delete_deletes() {
    val originalPath = underTest.fullPath
    underTest.delete()

    assertThat(fileSystem.directoryExistsAt(originalPath)).isFalse()
    assertThat { fileSystem.getDirectoryAt(originalPath) }.failsWith(FileNotFoundException::class)
    assertThat(parentDirectory.subdirectories).isEmpty()
  }

  @Test
  fun delete_deletesFiles() {
    val subFile = underTest.createFile("file".asPathComponent()).fullPath
    val subDirectory = underTest.createSubdirectory("subdirectory".asPathComponent()).fullPath

    underTest.delete()

    assertThat{ fileSystem.getFileAt(subFile) }.failsWith(FileNotFoundException::class)
    assertThat{ fileSystem.getDirectoryAt(subDirectory) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_thenCreateSubdirectory_fails() {
    underTest.delete()
    assertThat { underTest.createSubdirectory("fail".asPathComponent()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_thenCreateFile_fails() {
    underTest.delete()
    assertThat { underTest.createFile(("fail".asPathComponent())) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_thenMove_fails() {
    underTest.delete()
    assertThat { underTest.moveTo("/another_location".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_thencopyTo_fails() {
    underTest.delete()
    assertThat { underTest.moveTo("/another_copy".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun move_moves() {
    val newPath = parentDirectory.fullPath + "renamed".asPathComponent()
    val originalPath = underTest.fullPath

    val subDirectory = underTest.createSubdirectory("subdirectory".asPathComponent())
    val subFile = subDirectory.createFile("file".asPathComponent())

    underTest.moveTo(newPath)

    assertThat(underTest.fullPath).isEqualTo(newPath)
    assertThat { fileSystem.getDirectoryAt(originalPath) }.failsWith(FileNotFoundException::class)
    assertThat(fileSystem.getDirectoryAt(newPath)).isEqualTo(underTest)
    assertThat(newPath.contains(subDirectory.fullPath)).isTrue()
    assertThat(fileSystem.directoryExistsAt(newPath + "subdirectory"))
    assertThat(subDirectory.fullPath.contains(subFile.fullPath))
    assertThat(fileSystem.fileExistsAt(newPath + "subdirectory/file"))
    assertThat(underTest.subdirectories).contains(subDirectory)
    assertThat(subDirectory.files).contains(subFile)
  }

  @Test
  fun move_whenDirectoryAlreadyExists_fails() {
    val existingPath = "/existing".asAbsolutePath()
    val originalPath = underTest.fullPath

    fileSystem.createDirectoryAt(existingPath)

    assertThat { underTest.moveTo(existingPath) }.failsWith(FileAlreadyExistsException::class)
    assertThat(underTest.fullPath).isEqualTo(originalPath)
  }

  @Test
  fun move_whenFileAlreadyExists_fails() {
    val existingPath = "/existing".asAbsolutePath()
    val originalPath = underTest.fullPath

    fileSystem.createFileAt(existingPath)

    assertThat { underTest.moveTo(existingPath) }.failsWith(FileAlreadyExistsException::class)
    assertThat(underTest.fullPath).isEqualTo(originalPath)
  }
  @Test
  fun copyTo_copies() {
    val newPath = parentDirectory.fullPath + "copy".asPathComponent()
    val originalPath = underTest.fullPath

    val subDirectory = underTest.createSubdirectory("subdirectory".asPathComponent())
    val subFile = subDirectory.createFile("file".asPathComponent())
    val subFileContents = ImmutableList.of("some", "test", "lines")
    subFile.clearAndWriteLines(subFileContents.asObservable()).test().assertComplete()

    val copy = underTest.copyTo(newPath)

    assertThat(fileSystem.directoryExistsAt(originalPath))
    assertThat(underTest.fullPath).isEqualTo(originalPath)
    assertThat(subDirectory.fullPath).isEqualTo(originalPath + "subdirectory")
    assertThat(subFile.fullPath).isEqualTo(originalPath + "subdirectory/file")

    assertThat(fileSystem.directoryExistsAt(newPath))
    assertThat(copy.fullPath).isEqualTo(newPath)
    assertThat(copy.subdirectories.toImmutableSet())
      .hasCount(1)
      .andThat { it.first() }
      .andThat(VirtualDirectory::fullPath) { it.isEqualTo(newPath + "subdirectory") }

   assertThat(fileSystem.getDirectoryAt(newPath + "subdirectory"))
     .andThat { it.files.toImmutableSet() }
     .hasCount(1)
     .andThat { it.first() }
     .andThat(VirtualFile::fullPath) { it.isEqualTo(newPath + "subdirectory/file") }
     .actual
     .readLines()
     .test()
     .assertValues(subFileContents.toKotlinList())
     .assertComplete()
  }

  @Test
  fun copyTo_whenDirectoryAlreadyExists_fails() {
    val existingPath = "/existing".asAbsolutePath()

    fileSystem.createDirectoryAt(existingPath)

    assertThat { underTest.copyTo(existingPath) }.failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun copyTo_whenFileAlreadyExists_fails() {
    val existingPath = "/existing".asAbsolutePath()

    fileSystem.createFileAt(existingPath)

    assertThat { underTest.copyTo(existingPath) }.failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun copyTo_notifiesListener() {
    val newPath = parentDirectory.fullPath + "copy".asPathComponent()

    // TODO replace with a mocking library
    var onBeforeCreateDirectoryInvocations = 0
    var onAfterCreateDirectoryInvocations = 0
    fileSystem.setListener { event ->
      when (event) {
        is OnBeforeCreateDirectory -> {
          ++onBeforeCreateDirectoryInvocations
          assertThat(onAfterCreateDirectoryInvocations).isEqualTo(0)
          assertThat(event.path).isEqualTo(newPath)
          assertThat(fileSystem.directoryExistsAt(event.path)).isFalse()
        }
        is OnAfterCreateDirectory -> {
          ++onAfterCreateDirectoryInvocations
          assertThat(onBeforeCreateDirectoryInvocations).isEqualTo(1)
          assertThat(event.path).isEqualTo(newPath)
          assertThat(fileSystem.directoryExistsAt(event.path)).isTrue()
        }
        else -> throw AssertionError("Unexpected event $event")
      }
    }

    underTest.copyTo(newPath)

    assertThat(onBeforeCreateDirectoryInvocations).isEqualTo(1)
    assertThat(onAfterCreateDirectoryInvocations).isEqualTo(1)
  }
}
