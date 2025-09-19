package omnia.data.structure

/** A composite data structure that is both a list and a unique set. */
interface ListSet<T: Any>: List<T>, Set<T>
