import java.util.Map;
import java.util.HashMap;

/**
 * An implementation of a PrioMap. It consists of key-value pairs stored in a
 * priority queue and a map. The priority queue decides the priority by
 * comparing the values of the key-value pairs. A "lower" value gives higher 
 * priority. It allows for fast look-up of the value of any key and fast 
 * look-up of the pair with the highest priority, both operations take O(1)
 * time. To put a new pair onto the PrioMap takes O(logn) time where n is the 
 * current number of pairs in the PrioMap. To return and remove the pair with
 * the highest priority, i.e. to poll, also takes O(logn) time.
 * 
 * @param <K> the key of the pair.
 * @param <V> the value of the pair.
 */

public class APrioMap<K, V extends Comparable<? super V>> implements PrioMap<K, V> {

	/** The priority queue of the PrioMap. */
	private PrioQueue<Pair<K, V>> prioQueue;
	
	/** The map of the PrioMap.*/
	private Map<K, V> map;
	
	/**
	 * Creates an empty PrioMap. The priority is based on the value in the
	 * pair and the ordering is specified by SecondArgumentComparator which
	 * gives higher priority to a "lower" value.
	 */
	public APrioMap() {
		prioQueue = new BinHeapWithIndexTracker<>(
				new SecondArgumentComparator<K, V>());
		map = new HashMap<>();
	}
	
	/**
	 * Implements get of PrioMap. Returns the value of key k.
	 * 
	 * @param k the key of the value to be returned.
	 * @return the value of key k.
	 */
	@Override
	public V get(K k) {
		return map.get(k);
	}
	
	/**
	 * Implements put of PrioMap. Adds the key-value pair <k, v> to the
	 * PrioMap. If the key k already has a value, this value will be replaced
	 * by the new value v.
	 * 
	 * @param k the key of the key-value pair to be added.
	 * @param v the value of the key-value pair to be added.
	 */
	@Override
	public void put(K k, V v) {
		Pair<K, V> pair = new Pair<>(k, v);
		if (!map.containsKey(k)) {
			prioQueue.add(pair);
		} else {
			Pair<K, V> oldPair = new Pair<>(k, map.get(k));
			prioQueue.remove(oldPair);
			prioQueue.add(pair);
		}
		map.put(k, v);
	}

	/**
	 * Implements peek of PrioMap. Returns pair with the highest priority in
	 * the PrioMap. Returns null if the PrioMap is empty.
	 * 
	 * @return the pair with the highest priority in the PrioMap. Returns null
	 * if the PrioMap is empty.
	 */
	@Override
	public Pair<K, V> peek() {
		return prioQueue.peek();
	}

	/**
	 * Implements poll of PrioMap. Returns and removes the pair with the
	 * highest priority in the PrioMap. Returns null if the PrioMap is empty.
	 * 
	 * @return the pair with the highest priority in the PrioMap. Returns null
	 * if the PrioMap is empty.
	 */
	@Override
	public Pair<K, V> poll() {
		if (prioQueue.peek() == null) {
			return null;
		}
		Pair<K, V> pair = prioQueue.poll();
		map.remove(pair.a);
		return pair;
	}

	/**
	 * Overrides toString of Object. Returns a string consisting of the array
	 * of the PrioQueue, the indexMapping of the PrioQueue and the map of
	 * PrioMap.
	 * 
	 * @return string consisting of the array of the PrioQueue, the 
	 * indexMapping of the PrioQueue and the map of PrioMap.
	 */
	@Override
	public String toString() {
		String str = "PrioQueue: [";
		str += prioQueue;
		str += "\n Map: " + map;
		return str;
	}
}
