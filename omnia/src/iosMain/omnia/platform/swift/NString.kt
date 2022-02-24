package omnia.platform.swift

import platform.Foundation.NSString

@Suppress("CAST_NEVER_SUCCEEDS")
fun String.asNSString() = (this as NSString)