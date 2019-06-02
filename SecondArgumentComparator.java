import java.util.Comparator;

public class SecondArgumentComparator<K, V extends Comparable<? super V>> implements Comparator<Pair<K, V>> {
	
	public int compare(Pair<K, V> p1, Pair<K, V> p2) {
		return p1.b.compareTo(p2.b);
	}
}
