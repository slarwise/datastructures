import java.util.List;
import java.util.LinkedList;
import java.lang.Comparable;

public class Vertex implements Comparable<Vertex>, Cloneable {
	
	private String label;
	private List<Pair<String, Integer>> adjList;	
	private int dist;
	private Vertex path;
	
	public Vertex(String label) {
		this.label = label;
		this.adjList = new LinkedList<Pair<String, Integer>>();
		this.path = null;
	}

	@Override
	public Vertex clone() {
		try {
			Vertex result = (Vertex) super.clone();
//			result.adjList = new LinkedList<Pair<String, Integer>>();
//			for (Pair<String, Integer> neighborPairTemp : adjList) {
//				Pair<String, Integer> neighborPair = new Pair<>(
//				neighborPairTemp.a, new Integer(neighborPairTemp.b));
//				result.adjList.add(neighborPair);
//			}
			return result;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
	
	@Override
	public int compareTo(Vertex o) {
		return this.dist - o.dist;
	}
	
	@Override
	public boolean equals (Object otherObject) {
		if (this == otherObject) {
			return true;
		}
		if (otherObject == null) {
			return false;
		}
		if (otherObject.getClass() != this.getClass()) {
			return false;
		}
		Vertex other = (Vertex) otherObject;
		return label.equals(other.getLabel());
	}
	
	public List<Pair<String, Integer>> getAdjList() {
		return adjList;
	}
	
	public int getDist() {
		return dist;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Vertex getPath() {
		return path;
	}
	
	public int hashCode() {
		return 17 * label.hashCode();
	}
	
	public void setAdjList(List<Pair<String, Integer>> adjList) {
		this.adjList = adjList;
	}
	
	public void setDist(int dist) {
		this.dist = dist;
	}
	
	public void setPath(Vertex path) {
		this.path = path;
	}
	
	public String toString() {
//		String pathLabel;
//		if (path == null) {
//			pathLabel = null;
//		} else {
//			pathLabel = path.getLabel();
//		}
//		String str = "Vertex " + label + ", adjList: " + adjList + ", dist: " + dist + ", path: " + pathLabel;
		String pathLabel;
		if (path == null) {
			pathLabel = null;
		} else {
			pathLabel = path.getLabel();
		}
		String str = "<" + label + ", path: " + pathLabel + ">";
		return str;
	}
	
}
