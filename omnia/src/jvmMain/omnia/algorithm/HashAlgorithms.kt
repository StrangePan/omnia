package omnia.algorithm

class HashAlgorithms {
  companion object {
    fun hash(vararg objects: Any?): Int {
      return objects.fold(7) { hash, obj -> hash * 31 + (obj?.hashCode() ?: 0) }
    }
  }
}