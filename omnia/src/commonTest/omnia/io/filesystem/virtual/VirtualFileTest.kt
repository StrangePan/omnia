package omnia.io.filesystem.virtual

import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.observable.observableOfEmpty
import com.badoo.reaktive.observable.observableOfError
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.test.base.assertError
import com.badoo.reaktive.test.completable.assertComplete
import com.badoo.reaktive.test.completable.assertNotComplete
import com.badoo.reaktive.test.completable.test
import com.badoo.reaktive.test.observable.assertComplete
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValues
import com.badoo.reaktive.test.observable.test
import kotlin.test.Test
import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.asAbsolutePath
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.failsWith
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isFalse
import omnia.util.test.fluent.isNotEqualTo

class VirtualFileTest {

  val fileSystem = VirtualFileSystem()
  val underTest = fileSystem.createFile("/file".asAbsolutePath())

  @Test
  fun directory_returnsParentDirectory() {
    assertThat(underTest.directory)
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/") }
      .andThat(Directory::files) { it.contains(underTest) }
  }

  @Test
  fun readLines_whenNew_isEmpty() {
    underTest.readLines()
      .test()
      .assertNoValues()
      .assertComplete()
  }

  @Test
  fun clearAndWriteLines_completesWhenInputCompletes() {
    val input = BehaviorSubject<String>("first")

    val completable = underTest.clearAndWriteLines(input).test()

    completable.assertNotComplete()
    input.onNext("second")
    completable.assertNotComplete()
    input.onNext("")
    completable.assertNotComplete()
    input.onComplete()
    completable.assertComplete()
  }

  @Test
  fun clearAndWriteLines_thenErrors_emitsError() {
    val error = RuntimeException()
    val input = observableOfError<String>(error)

    underTest.clearAndWriteLines(input).test().assertError(error)
  }

  @Test
  fun clearAndWriteLines_thenReadLines_emitsWrittenLines() {
    val input = ImmutableList.of(
      "There once was a man from peru",
      "Who dreamed he was eating his shoe",
      "He woke with a fright",
      "In the middle of the night",
      "To find that his dream had come true")

    underTest.clearAndWriteLines(input.asObservable()).test().assertComplete()

    underTest.readLines().test().assertValues(input.toKotlinList())
  }

  @Test
  fun delete_deletes() {
    val originalPath = underTest.fullPath
    underTest.delete()

    assertThat(fileSystem.isFile(originalPath)).isFalse()
    assertThat { fileSystem.getFile(originalPath) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_thenOverwriteContents_fails() {
    underTest.delete()
    underTest.clearAndWriteLines(observableOfEmpty()).test().assertError { it is FileNotFoundException }
  }

  @Test
  fun delete_thenMove_fails() {
    underTest.delete()
    assertThat { underTest.moveTo("/file2".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }

  @Test
  fun delete_thenCopy_fails() {
    underTest.delete()
    assertThat { underTest.copyTo("/file2".asAbsolutePath()) }.failsWith(FileNotFoundException::class)
  }
  @Test
  fun move_moves() {
    val input = ImmutableList.of(
      "There once was a man from Vienna",
      "Who slipped on a peel of banana",
      "And with a loud crack",
      "He fell on his back",
      "And said 'Oh my, isn't this a dilemma!'")
    underTest.clearAndWriteLines(input.asObservable()).test().assertComplete()

    val newPath = "/file2".asAbsolutePath()
    val originalPath = underTest.fullPath

    underTest.moveTo(newPath)

    assertThat(underTest.fullPath).isEqualTo(newPath)
    assertThat(fileSystem.getFile(newPath))
      .isEqualTo(underTest)
      .actual
      .readLines()
      .test()
      .assertValues(input.toKotlinList())
    assertThat(fileSystem.isFile(originalPath)).isFalse()
  }

  @Test
  fun move_whenAlreadyExists_throwsException() {
    val originalPath = underTest.fullPath
    val existingPath = "/file2".asAbsolutePath()
    val existingFile = fileSystem.createFile(existingPath)

    assertThat { underTest.moveTo(existingPath) }
      .failsWith(FileAlreadyExistsException::class)

    assertThat(fileSystem.getFile(originalPath)).isEqualTo(underTest)
    assertThat(fileSystem.getFile(existingPath)).isEqualTo(existingFile)
  }

  @Test
  fun copy_copies() {
    val input = ImmutableList.of("Ok I'm out of limericks.")
    underTest.clearAndWriteLines(input.asObservable()).test().assertComplete()

    val newPath = "/file2".asAbsolutePath()
    val originalPath = underTest.fullPath

    val newFile = underTest.copyTo(newPath)

    assertThat(underTest.fullPath).isEqualTo(originalPath)
    assertThat(fileSystem.getFile(newPath))
      .isEqualTo(newFile)
      .isNotEqualTo(underTest)
      .andThat(VirtualFile::fullPath) { it.isEqualTo(newPath) }
      .actual
      .readLines()
      .test()
      .assertValues(input.toKotlinList())
    assertThat(fileSystem.getFile(originalPath)).isEqualTo(underTest)
      .andThat(VirtualFile::fullPath) { it.isEqualTo(originalPath) }

    // ensure that their contents are completely disconnected
    val input2 = ImmutableList.of("Yeah, all empty.")
    underTest.clearAndWriteLines(input2.asObservable()).test().assertComplete()

    newFile.readLines().test().assertValues(input.toKotlinList())
  }

  @Test
  fun copy_whenAlreadyExists_throwsExceptions() {
    val originalPath = underTest.fullPath
    val existingPath = "/file2".asAbsolutePath()
    val existingFile = fileSystem.createFile(existingPath)

    assertThat { underTest.copyTo(existingPath) }
      .failsWith(FileAlreadyExistsException::class)

    assertThat(fileSystem.getFile(originalPath)).isEqualTo(underTest)
    assertThat(fileSystem.getFile(existingPath)).isEqualTo(existingFile)
  }

}