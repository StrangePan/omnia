package omnia.io.filesystem.virtual

import com.badoo.reaktive.observable.asObservable
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
import omnia.io.filesystem.asAbsolutePath
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.contains
import omnia.util.test.fluent.isEqualTo

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
}
