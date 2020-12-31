package omnia.data.structure.mutable

import omnia.data.structure.SortedSet

/** A mutable set data structure that maintains a deterministic order of its contents.  */
interface MutableSortedSet<E> : MutableSet<E>, SortedSet<E>