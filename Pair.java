import java.util.Objects;

public class Pair<A, B> {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public String toString() {
        return "<" + a.toString() + "," + b.toString() + ">";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> p = (Pair<?, ?>)o;
        return a.equals(p.a) && b.equals(p.b);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(a.hashCode(), b.hashCode());
    }
}