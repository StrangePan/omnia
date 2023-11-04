package omnia.io.filesystem.sandbox

import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.test.completable.assertComplete
import com.badoo.reaktive.test.completable.test
import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent
import omnia.io.filesystem.virtual.VirtualFileSystem
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.containsExactly
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.hasCount
import omnia.util.test.fluent.isEmpty
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isNull
import omnia.util.test.fluent.isTrue

class SandboxDirectoryTest {

  val baseFileSystem = VirtualFileSystem()
  val baseRootDirectory = baseFileSystem.createDirectory("/sandbox".asAbsolutePath())
  val sandboxFileSystem = SandboxFileSystem(baseFileSystem, baseRootDirectory, baseRootDirectory)

  @Test
  fun parentDirectory_whenRoot_isNull() {
    assertThat(sandboxFileSystem.rootDirectory.parentDirectory).isNull()
  }

  @Test
  fun createDirectory_hasExpectedName_isEmpty() {
    assertThat(sandboxFileSystem.createDirectory("/directory".asAbsolutePath()))
      .andThat({ it.name.name }) { it.isEqualTo("directory") }
      .andThat(SandboxDirectory::fullPath) { it.isEqualTo("/directory".asAbsolutePath()) }
      .andThat(SandboxDirectory::files) { it.isEmpty() }
      .andThat(SandboxDirectory::subdirectories) { it.isEmpty() }
      .andThat({ it.parentDirectories.map(SandboxDirectory::fullPath).toImmutableList() }) {
        it.containsExactly("/".asAbsolutePath())
      }
  }

  @Test
  fun createFile_createsExpectedFile() {
    assertThat(sandboxFileSystem.rootDirectory.createFile("file".asPathComponent()))
      .andThat(SandboxFile::name) { it.isEqualTo("file".asPathComponent()) }
      .andThat(SandboxFile::fullPath) { it.isEqualTo("/file".asAbsolutePath()) }
      .andThat({ it.directory.fullPath }) { it.isEqualTo(sandboxFileSystem.rootDirectory.fullPath) }
      .actual.readLines().test().assertNoValues().assertComplete()
    assertThat(baseFileSystem.isFile("/sandbox/file".asAbsolutePath()))
  }

  @Test
  fun createFile_inAllParentDirectories_onlyAffectsSandboxedDirectories() {
    val directory0 = sandboxFileSystem.rootDirectory
    val directory1 = directory0.createSubdirectory("directory1".asPathComponent())
    val directory2 = directory1.createSubdirectory("directory2".asPathComponent())
    val directory3 = directory2.createSubdirectory("directory3".asPathComponent())

    directory3.parentDirectories
      .forEachIndexed { index, directory ->
        directory.createFile(("file$index").asPathComponent())
      }

    assertThat(directory3.files).isEmpty()
    assertThat(sandboxFileSystem.isFile("/directory1/directory2/file0".asAbsolutePath())).isTrue()
    assertThat(baseFileSystem.isFile("/sandbox/directory1/directory2/file0".asAbsolutePath())).isTrue()
    assertThat(sandboxFileSystem.isFile("/directory1/file1".asAbsolutePath())).isTrue()
    assertThat(baseFileSystem.isFile("/sandbox/directory1/file1".asAbsolutePath())).isTrue()
    assertThat(sandboxFileSystem.isFile("/file2".asAbsolutePath())).isTrue()
    assertThat(baseFileSystem.isFile("/sandbox/file2".asAbsolutePath())).isTrue()
    assertThat(baseFileSystem.rootDirectory.files).isEmpty()
  }

  @Test
  fun createSubdirectory_createsExpectedSubdirectory() {
    assertThat(sandboxFileSystem.rootDirectory.createSubdirectory("subdirectory".asPathComponent()))
      .andThat({ it.name.name }) { it.isEqualTo("subdirectory") }
      .andThat(SandboxDirectory::fullPath) { it.isEqualTo("/subdirectory".asAbsolutePath()) }
      .andThat(SandboxDirectory::files) { it.isEmpty() }
      .andThat(SandboxDirectory::subdirectories) { it.isEmpty() }
  }

  @Test
  fun moveTo_whenAlreadyExists_fails() {
    val underTest = sandboxFileSystem.createDirectory("/undertest".asAbsolutePath())

    sandboxFileSystem.createDirectory("/existing".asAbsolutePath())

    assertThat { underTest.moveTo("/existing".asAbsolutePath()) }.failsWith(FileAlreadyExistsException::class)
    assertThat(sandboxFileSystem.isDirectory("/undertest".asAbsolutePath())).isTrue()
  }

  @Test
  fun moveTo_whenAlreadyDeleted_fails() {
    val underTest = sandboxFileSystem.createDirectory("/undertest".asAbsolutePath())

    underTest.delete()

    assertThat { underTest.moveTo("/newlocation".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun moveTo_movesContents() {
    val underTest = sandboxFileSystem.rootDirectory.createSubdirectory("undertest".asPathComponent())
    underTest.createSubdirectory("subdirectory".asPathComponent())
    underTest.createFile("file".asPathComponent())
      .clearAndWriteLines(observableOf("line1", "line2", "line3"))
      .test()
      .assertComplete()

    underTest.moveTo("/newlocation".asAbsolutePath())

    assertThat(underTest.fullPath).isEqualTo("/newlocation".asAbsolutePath())
    assertThat(underTest.name).isEqualTo("newlocation".asPathComponent())
    assertThat(underTest.subdirectories.toImmutableList())
      .hasCount(1)
      .andThat { it.first().fullPath }
      .isEqualTo("/newlocation/subdirectory".asAbsolutePath())
    assertThat(underTest.files.toImmutableList())
      .hasCount(1)
      .andThat { it.first() }
      .andThat(SandboxFile::fullPath) { it.isEqualTo("/newlocation/file".asAbsolutePath()) }
      .actual
      .readLines()
      .test()
      .assertValues("line1", "line2", "line3")
      .assertComplete()
    assertThat { sandboxFileSystem.getDirectory("/undertest".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun copyTo_whenAlreadyExists_fails() {
    val underTest = sandboxFileSystem.createDirectory("/undertest".asAbsolutePath())

    sandboxFileSystem.createDirectory("/existing".asAbsolutePath())

    assertThat { underTest.copyTo("/existing".asAbsolutePath()) }.failsWith(FileAlreadyExistsException::class)
    assertThat(sandboxFileSystem.isDirectory("/undertest".asAbsolutePath())).isTrue()
  }

  @Test
  fun copyTo_whenAlreadyDeleted_fails() {
    val underTest = sandboxFileSystem.createDirectory("/undertest".asAbsolutePath())

    underTest.delete()

    assertThat { underTest.copyTo("/newlocation".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun copyTo_copyContents() {
    val underTest = sandboxFileSystem.rootDirectory.createSubdirectory("undertest".asPathComponent())
    underTest.createSubdirectory("subdirectory".asPathComponent())
    underTest.createFile("file".asPathComponent())
      .clearAndWriteLines(observableOf("line1", "line2", "line3"))
      .test()
      .assertComplete()

    val underTestCopy = underTest.copyTo("/newlocation".asAbsolutePath())

    assertThat(underTest.fullPath).isEqualTo("/undertest".asAbsolutePath())
    assertThat(underTest.name).isEqualTo("undertest".asPathComponent())
    assertThat(underTest.subdirectories.toImmutableList())
      .hasCount(1)
      .andThat { it.first().fullPath }
      .isEqualTo("/undertest/subdirectory".asAbsolutePath())
    assertThat(underTest.files.toImmutableList())
      .hasCount(1)
      .andThat { it.first() }
      .andThat(SandboxFile::fullPath) { it.isEqualTo("/undertest/file".asAbsolutePath()) }
      .actual
      .readLines()
      .test()
      .assertValues("line1", "line2", "line3")
      .assertComplete()

    assertThat(underTestCopy.fullPath).isEqualTo("/newlocation".asAbsolutePath())
    assertThat(underTestCopy.name).isEqualTo("newlocation".asPathComponent())
    assertThat(underTestCopy.subdirectories.toImmutableList())
      .hasCount(1)
      .andThat { it.first().fullPath }
      .isEqualTo("/newlocation/subdirectory".asAbsolutePath())
    assertThat(underTestCopy.files.toImmutableList())
      .hasCount(1)
      .andThat { it.first() }
      .andThat(SandboxFile::fullPath) { it.isEqualTo("/newlocation/file".asAbsolutePath()) }
      .actual
      .readLines()
      .test()
      .assertValues("line1", "line2", "line3")
      .assertComplete()
  }

  @Test
  fun delete_whenAlreadyDeleted_fails() {
    val underTest = sandboxFileSystem.createDirectory("/undertest".asAbsolutePath())

    underTest.delete()

    assertThat { underTest.delete() }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_deletesContents() {
    val underTest = sandboxFileSystem.rootDirectory.createSubdirectory("undertest".asPathComponent())
    underTest.createSubdirectory("subdirectory".asPathComponent())
    underTest.createFile("file".asPathComponent())
      .clearAndWriteLines(observableOf("line1", "line2", "line3"))
      .test()
      .assertComplete()

    underTest.delete()

    assertThat { sandboxFileSystem.getDirectory("/undertest".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
    assertThat { sandboxFileSystem.getDirectory("/undertest/subdirectory".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
    assertThat { sandboxFileSystem.getFile("/undertest/file".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }
}
