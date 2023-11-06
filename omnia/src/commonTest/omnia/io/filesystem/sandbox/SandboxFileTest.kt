package omnia.io.filesystem.sandbox

import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.test.completable.assertComplete
import com.badoo.reaktive.test.completable.test
import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent
import omnia.io.filesystem.virtual.VirtualFileSystem
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse

class SandboxFileTest {

  val baseFileSystem = VirtualFileSystem()
  val baseRootDirectory = baseFileSystem.createDirectoryAt("/sandbox".asAbsolutePath())
  val sandboxFileSystem = SandboxFileSystem(baseFileSystem, baseRootDirectory, baseRootDirectory)
  val underTest = sandboxFileSystem.createFileAt("/file".asAbsolutePath())

  @Test
  fun name_isExpectedName() {
    assertThat(underTest.name).isEqualTo("file".asPathComponent())
  }

  @Test
  fun fullPath_isExpectedPath() {
    assertThat(underTest.fullPath).isEqualTo("/file".asAbsolutePath())
  }

  @Test
  fun directory_isParentDirectory() {
    assertThat(underTest.directory.fullPath).isEqualTo("/".asAbsolutePath())
  }

  @Test
  fun readLines_isEmpty() {
    underTest.readLines().test().assertComplete().assertNoValues()
  }

  @Test
  fun writeLines_thenReadLines_returnsSameValues_alsoWroteToBaseFile() {
    val lines = listOf(
      "this was a triumph",
      "i'm making a note here: huge success",
      "it's hard to overstate my satisfaction",
    )

    underTest.clearAndWriteLines(lines.asObservable()).test().assertComplete()

    underTest.readLines().test().assertComplete().assertValues(lines)

    baseFileSystem.getFileAt("/sandbox/file".asAbsolutePath()).readLines().test().assertComplete().assertValues(lines)
  }

  @Test
  fun readLines_returnsLinesFromBaseFile() {
    val lines = listOf(
      "Aperture Science",
      "We do what we must because we can",
      "For the good of all of us",
      "Except the ones who are dead",
    )

    baseFileSystem.getFileAt("/sandbox/file".asAbsolutePath())
      .clearAndWriteLines(lines.asObservable()).test()
      .assertComplete()

    underTest.readLines().test().assertComplete().assertValues(lines)
  }

  @Test
  fun moveTo_whenAlreadyExists_fails() {
    sandboxFileSystem.createFileAt("/existing".asAbsolutePath())

    assertThat { underTest.moveTo("/existing".asAbsolutePath()) }.failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun moveTo_whenAlreadyDeleted_fails() {
    underTest.delete()

    assertThat { underTest.moveTo("/newlocation".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun moveTo_moves() {
    val contents = listOf("first", "second", "third")

    underTest.clearAndWriteLines(contents.asObservable()).test().assertComplete()

    underTest.moveTo("/newlocation".asAbsolutePath())
    assertThat(underTest.fullPath).isEqualTo("/newlocation".asAbsolutePath())
    assertThat(underTest.name).isEqualTo("newlocation".asPathComponent())
    underTest.readLines().test().assertValues(contents).assertComplete()
  }

  @Test
  fun copyTo_whenAlreadyExists_fails() {
    sandboxFileSystem.createFileAt("/existing".asAbsolutePath())

    assertThat { underTest.copyTo("/existing".asAbsolutePath()) }.failsWith(FileAlreadyExistsException::class)
  }

  @Test
  fun copyTo_whenAlreadyDeleted_fails() {
    underTest.delete()

    assertThat { underTest.copyTo("/newlocation".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun copyTo_copies() {
    val contents = listOf("first", "second", "third")

    underTest.clearAndWriteLines(contents.asObservable()).test().assertComplete()

    val copy = underTest.copyTo("/newlocation".asAbsolutePath())

    assertThat(copy.fullPath).isEqualTo("/newlocation".asAbsolutePath())
    assertThat(copy.name).isEqualTo("newlocation".asPathComponent())
    copy.readLines().test().assertValues(contents).assertComplete()
    assertThat(underTest.fullPath).isEqualTo("/file".asAbsolutePath())
    assertThat(underTest.name).isEqualTo("file".asPathComponent())
    underTest.readLines().test().assertValues(contents).assertComplete()
  }

  @Test
  fun delete_whenAlreadyDeleted_fails() {
    underTest.delete()

    assertThat { underTest.delete() }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_deletes() {
    underTest.delete()

    assertThat(sandboxFileSystem.fileExistsAt(underTest.fullPath)).isFalse()
    assertThat(baseFileSystem.fileExistsAt(sandboxFileSystem.toBasePath(underTest.fullPath))).isFalse()
  }
}
