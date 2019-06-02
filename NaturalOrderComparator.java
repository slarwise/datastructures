import java.util.Comparator;

// from java 8 there is static method Comparator.naturalOrder(), but for java 7 this is needed

public class NaturalOrderComparator<T extends Comparable<? super T>> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }
}
