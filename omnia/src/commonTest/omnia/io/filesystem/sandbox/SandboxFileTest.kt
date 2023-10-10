package omnia.io.filesystem.sandbox

import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.test.completable.assertComplete
import com.badoo.reaktive.test.completable.test
import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent
import omnia.io.filesystem.virtual.VirtualFileSystem
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.isEqualTo

class SandboxFileTest {

  val baseFileSystem = VirtualFileSystem()
  val baseRootDirectory = baseFileSystem.createDirectory("/sandbox".asAbsolutePath())
  val sandboxFileSystem = SandboxFileSystem(baseFileSystem, baseRootDirectory, baseRootDirectory)
  val underTest = sandboxFileSystem.createFile("/file".asAbsolutePath())

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

    baseFileSystem.getFile("/sandbox/file".asAbsolutePath()).readLines().test().assertComplete().assertValues(lines)
  }

  @Test
  fun readLines_returnsLinesFromBaseFile() {
    val lines = listOf(
      "Aperture Science",
      "We do what we must because we can",
      "For the good of all of us",
      "Except the ones who are dead",
    )

    baseFileSystem.getFile("/sandbox/file".asAbsolutePath())
      .clearAndWriteLines(lines.asObservable()).test()
      .assertComplete()

    underTest.readLines().test().assertComplete().assertValues(lines)
  }

  // TODO tests for move, copy, delete
}
