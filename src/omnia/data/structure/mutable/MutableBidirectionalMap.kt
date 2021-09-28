package omnia.data.structure.mutable

import omnia.data.structure.BidirectionalMap

interface MutableBidirectionalMap<E : Any> : MutableMap<E, E>, BidirectionalMap<E>