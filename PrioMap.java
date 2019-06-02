public interface PrioMap<K, V extends Comparable<? super V>> {
    void put(K k, V v);
    V get(K k);
    Pair<K, V> peek();
    Pair<K, V> poll();
}