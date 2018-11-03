package omnia.data.structure.mutable;

import omnia.data.structure.BidirectionalMap;

public interface MutableBidirectionalMap<K, V> extends MutableMap<K, V>, BidirectionalMap<K, V> {

  @Override MutableBidirectionalMap<V, K> inverse();
}
