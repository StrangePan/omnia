package omnia.io.filesystem.sandbox

import kotlin.test.Test
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent
import omnia.io.filesystem.virtual.VirtualFileSystem
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.fails
import omnia.util.test.fluent.isA
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isTrue

class SandboxFileSystemTest {

  val baseFileSystem = VirtualFileSystem()
  val baseRootDirectory = baseFileSystem.createDirectoryAt("/sandbox".asAbsolutePath())
  val baseWorkingDirectory = baseFileSystem.createDirectoryAt("/sandbox/working".asAbsolutePath())
  val underTest = SandboxFileSystem(baseFileSystem, baseRootDirectory, baseWorkingDirectory)

  @Test
  fun rootDirectory_hasAppropriatePaths() {
    assertThat(underTest.rootDirectory)
      .andThat({{ it.name.name }}) { it.fails() }
      .andThat({ it.fullPath }) { it.isEqualTo("/".asAbsolutePath()) }
  }

  @Test
  fun workingDirectory_hasAppropriatePaths() {
    assertThat(underTest.workingDirectory)
      .andThat({ it.name.name }) { it.isEqualTo("working") }
      .andThat({ it.fullPath }) { it.isEqualTo("/working".asAbsolutePath()) }
  }

  @Test
  fun createFileAt_inSandbox_createsFileInBase() {
    underTest.createFileAt("/file".asAbsolutePath())
    assertThat(baseFileSystem.fileExistsAt(baseRootDirectory.fullPath + "file"))
      .isTrue()
  }

  @Test
  fun createDirectoryAt_inSandbox_createsDirectoryInBase() {
    underTest.createDirectoryAt("/directory".asAbsolutePath())
    assertThat(baseFileSystem.directoryExistsAt(baseRootDirectory.fullPath + "directory"))
      .isTrue()
  }

  @Test
  fun createFileAt_inBase_createsFileInSandbox() {
    baseFileSystem.createFileAt(baseWorkingDirectory.fullPath + "file")
    assertThat(underTest.fileExistsAt("/working/file".asAbsolutePath()))
  }

  @Test
  fun createDirectoryAt_inBase_createsDirectoryInSandbox() {
    baseFileSystem.createDirectoryAt(baseWorkingDirectory.fullPath + "directory")
    assertThat(underTest.directoryExistsAt("/working/directory".asAbsolutePath()))
  }

  @Test
  fun getObjectAt_inSandbox_whenIsFile_getsFileInBase() {
    baseFileSystem.getDirectoryAt("/sandbox/working".asAbsolutePath())
      .createSubdirectory("subdirectory".asPathComponent())
      .createFile("file".asPathComponent())

    assertThat(underTest.getObjectAt("/working/subdirectory/file".asAbsolutePath()))
      .isA(SandboxFile::class)
      .andThat({ it.name.name }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/file") }
  }

  @Test
  fun getObjectAt_inSandbox_whenIsDirectory_getsDirectoryInBase() {
    baseFileSystem.createDirectoryAt("/sandbox/working/subdirectory".asAbsolutePath())
      .createSubdirectory("directory".asPathComponent())

    assertThat(underTest.getObjectAt("/working/subdirectory/directory".asAbsolutePath()))
      .isA(SandboxDirectory::class)
      .andThat({ it.name.name }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/directory") }
  }

  @Test
  fun getFileAt_inSandbox_getsFileInBase() {
    baseFileSystem.getDirectoryAt("/sandbox/working".asAbsolutePath())
      .createSubdirectory("subdirectory".asPathComponent())
      .createFile("file".asPathComponent())

    assertThat(underTest.getFileAt("/working/subdirectory/file".asAbsolutePath()))
      .andThat({ it.name.name }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/file") }
  }

  @Test
  fun getDirectoryAt_inSandbox_getsDirectoryInBase() {
    baseFileSystem.createDirectoryAt("/sandbox/working/subdirectory".asAbsolutePath())
      .createSubdirectory("directory".asPathComponent())

    assertThat(underTest.getDirectoryAt("/working/subdirectory/directory".asAbsolutePath()))
      .andThat({ it.name.name }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/directory") }
  }
}
