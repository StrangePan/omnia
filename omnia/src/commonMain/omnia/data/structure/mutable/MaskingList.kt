package omnia.data.structure.mutable

open class MaskingList<E : Any>(private val backingList: kotlin.collections.MutableList<E>) :
  MutableList<E> {

  override fun insertAt(index: Int, item: E) {
    backingList.add(index, item)
  }

  override fun removeAt(index: Int): E {
    return backingList.removeAt(index)
  }

  override fun replaceAt(index: Int, item: E): E {
    return backingList.set(index, item)
  }

  override fun itemAt(index: Int): E {
    return backingList[index]
  }

  override fun indexOf(item: Any?): Int? {
    val index: Int = backingList.indexOf(item)
    return if (index < 0) null else index
  }

  override fun add(item: E) {
    backingList.add(item)
  }

  override fun addAll(items: Iterable<E>) {
    backingList.addAll(items)
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return backingList.remove(item)
  }

  override fun clear() {
    backingList.clear()
  }

  override fun iterator(): MutableIterator<E> {
    return backingList.iterator()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return backingList.contains(item)
  }

  override val isPopulated: Boolean
    get() = backingList.isNotEmpty()

  override fun count(): Int {
    return backingList.size
  }
}