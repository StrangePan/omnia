package omnia.platform.swift

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun <ErrorType: NSError, ReturnType> invokeWithErrorPointer(block: (CPointer<ObjCObjectVar<ErrorType?>>) -> ReturnType): ReturnType {
  memScoped {
    val error = alloc<ObjCObjectVar<ErrorType?>>()
    val result = block(error.ptr)
    if (error.value != null) {
      throw WrappedNSError(error.value!!)
    }
    return result
  }
}
