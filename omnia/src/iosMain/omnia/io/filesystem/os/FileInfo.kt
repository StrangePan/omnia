package omnia.io.filesystem.os

import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import omnia.io.filesystem.AbsolutePath
import platform.Foundation.NSFileManager

data class FileInfo(val exists: Boolean, val isDirectory: Boolean)

@OptIn(ExperimentalForeignApi::class)
fun getFileInfo(path: AbsolutePath): FileInfo {
  return memScoped {
    val isDirectory = alloc<BooleanVar>()
    val exists =
      NSFileManager.defaultManager.fileExistsAtPath(path.toString(), isDirectory = isDirectory.ptr)
    return@memScoped FileInfo(exists, isDirectory.value)
  }
}
