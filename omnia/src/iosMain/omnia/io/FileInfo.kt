package omnia.io

import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSFileManager

data class FileInfo(val exists: Boolean, val isDirectory: Boolean)

fun getFileInfo(path: String): FileInfo {
  return memScoped {
    val isDirectory = alloc<BooleanVar>()
    val exists =
      NSFileManager.defaultManager.fileExistsAtPath(path, isDirectory = isDirectory.ptr)
    return@memScoped FileInfo(exists, isDirectory.value)
  }
}