package omnia.platform.swift

import platform.Foundation.NSError

class WrappedNSError(val error: NSError, message: String?): Throwable(message) {

  constructor(error: NSError) : this(error, null)

  override val message: String get() =
    "WrappedNSError: ${error.localizedDescription}${super.message?.let { "\n" + it } ?: "" }"
}
