package omnia.data.structure.immutable

import omnia.data.structure.mutable.ArrayList
import omnia.data.structure.mutable.MutableList

abstract class AbstractBuilder<E : Any, B : AbstractBuilder<E, B, R>, R> {

  val elements: MutableList<E> = ArrayList.create()

  fun add(element: E): B {
    elements.add(element)
    return self
  }

  fun addAll(elements: Iterable<E>): B {
    for (element in elements) {
      this.elements.add(element)
    }
    return self
  }

  @SafeVarargs
  fun addAll(vararg elements: E): B {
    for (element in elements) {
      this.elements.add(element)
    }
    return self
  }

  fun remove(element: E): B {
    elements.remove(element)
    return self
  }

  fun removeAll(elements: Iterable<E>): B {
    for (element in elements) {
      this.elements.remove(element)
    }
    return self
  }

  abstract fun build(): R

  protected abstract val self: B
}