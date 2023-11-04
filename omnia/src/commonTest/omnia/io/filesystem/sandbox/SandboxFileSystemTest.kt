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
  val baseRootDirectory = baseFileSystem.createDirectory("/sandbox".asAbsolutePath())
  val baseWorkingDirectory = baseFileSystem.createDirectory("/sandbox/working".asAbsolutePath())
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
  fun createFileInSandbox_createsFileInBase() {
    underTest.createFile("/file".asAbsolutePath())
    assertThat(baseFileSystem.isFile(baseRootDirectory.fullPath + "file"))
      .isTrue()
  }

  @Test
  fun createDirectoryInSandbox_createsDirectoryInBase() {
    underTest.createDirectory("/directory".asAbsolutePath())
    assertThat(baseFileSystem.isDirectory(baseRootDirectory.fullPath + "directory"))
      .isTrue()
  }

  @Test
  fun createFileInBase_createsFileInSandbox() {
    baseFileSystem.createFile(baseWorkingDirectory.fullPath + "file")
    assertThat(underTest.isFile("/working/file".asAbsolutePath()))
  }

  @Test
  fun createDirectoryInBase_createsDirectoryInSandbox() {
    baseFileSystem.createDirectory(baseWorkingDirectory.fullPath + "directory")
    assertThat(underTest.isDirectory("/working/directory".asAbsolutePath()))
  }

  @Test
  fun getObjectInSandbox_whenIsFile_getsFileInBase() {
    baseFileSystem.getDirectory("/sandbox/working".asAbsolutePath())
      .createSubdirectory("subdirectory".asPathComponent())
      .createFile("file".asPathComponent())

    assertThat(underTest.getObjectAt("/working/subdirectory/file".asAbsolutePath()))
      .isA(SandboxFile::class)
      .andThat({ it.name.name }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/file") }
  }

  @Test
  fun getObjectInSandbox_whenIsDirectory_getsDirectoryInBase() {
    baseFileSystem.createDirectory("/sandbox/working/subdirectory".asAbsolutePath())
      .createSubdirectory("directory".asPathComponent())

    assertThat(underTest.getObjectAt("/working/subdirectory/directory".asAbsolutePath()))
      .isA(SandboxDirectory::class)
      .andThat({ it.name.name }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/directory") }
  }

  @Test
  fun getFileInSandbox_getsFileInBase() {
    baseFileSystem.getDirectory("/sandbox/working".asAbsolutePath())
      .createSubdirectory("subdirectory".asPathComponent())
      .createFile("file".asPathComponent())

    assertThat(underTest.getFile("/working/subdirectory/file".asAbsolutePath()))
      .andThat({ it.name.name }) { it.isEqualTo("file") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/file") }
  }

  @Test
  fun getDirectoryInSandbox_getsDirectoryInBase() {
    baseFileSystem.createDirectory("/sandbox/working/subdirectory".asAbsolutePath())
      .createSubdirectory("directory".asPathComponent())

    assertThat(underTest.getDirectory("/working/subdirectory/directory".asAbsolutePath()))
      .andThat({ it.name.name }) { it.isEqualTo("directory") }
      .andThat({ it.fullPath.toString() }) { it.isEqualTo("/working/subdirectory/directory") }
  }
}
