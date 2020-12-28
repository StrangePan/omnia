package omnia.data.structure.mutable

import omnia.data.structure.Collection
import java.util.OptionalInt
import java.util.stream.Collectors
import java.util.stream.Stream

open class MaskingList<E>(private val kotlinList: kotlin.collections.MutableList<E>) : MutableList<E> {
  override fun insertAt(index: Int, item: E) {
    kotlinList.add(index, item)
  }

  override fun removeAt(index: Int): E {
    return kotlinList.removeAt(index)
  }

  override fun replaceAt(index: Int, item: E): E {
    return kotlinList.set(index, item)
  }

  override fun itemAt(index: Int): E {
    return kotlinList.get(index)
  }

  override fun indexOf(item: Any?): OptionalInt {
    val index: Int = kotlinList.indexOf(item)
    return if (index < 0) OptionalInt.empty() else OptionalInt.of(index)
  }

  override fun add(item: E) {
    kotlinList.add(item)
  }

  override fun addAll(items: Collection<out E>) {
    kotlinList.addAll(items.stream().collect(Collectors.toList()))
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return kotlinList.remove(item)
  }

  override fun clear() {
    kotlinList.clear()
  }

  override fun iterator(): MutableIterator<E> {
    return kotlinList.iterator()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return kotlinList.contains(item)
  }

  override val isPopulated: Boolean
    get() = kotlinList.isNotEmpty()

  override fun count(): Int {
    return kotlinList.size
  }

  override fun stream(): Stream<E> {
    return kotlinList.stream()
  }
}