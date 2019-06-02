import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Graph {
	
	/** Map representing the graph. Each node has its label as a key and the
	 * value is a list of pairs, each pair is a neighbor and the distance to
	 * this neighbor.
	 */
	private Map<String, List<Pair<String, Integer>>> neighbors;
	
	/** Largest possible int representing infinity. */
	private final static int INFINITY = 2147483647;
	
	/**
	 * Creates an empty graph.
	 */
    public Graph() {
    	neighbors = new HashMap<>();
    }
    
    /**
     * Adds an edge between node1 and node2 with the weight dist. If there
     * already is an edge between node1 and node2 it is replaced.
     * 
     * @pre node1 and node2 must exist in the graph and dist must be non-
     * negative.
     * @param node1 one of the nodes the edge connects.
     * @param node2 one of the nodes the edge connects.
     * @param dist the weight of the edge between node1 and node2. Can be
     * interpreted as the distance between node1 and node2.
     */
    public void addEdge(String node1, String node2, int dist) {
    	// Remove a potentially existing edge between node1 and node2.
    	if (removeNeighbor(node1, node2) != null) {
    		removeNeighbor(node2, node1);
    	}
    	addNeighbor(node1, node2, dist);
    	addNeighbor(node2, node1, dist);
    }
    
    /**
     * Adds a neighbor to a node with a weighted edge.
     * 
     * @param node the node we are making the edge from.
     * @param addedNode the node the edge goes to.
     * @param dist the weight of the edge.
     */
    private void addNeighbor(String node, String addedNode, int dist) {
    	List<Pair<String, Integer>> adjList = neighbors.get(node);
    	adjList.add(new Pair<>(addedNode, dist));
    }
    
    /**
     * Adds a node with the specified label.
     * 
     * @pre The node must not be present in the graph already.
     * @param label the label of the node.
     */
    public void addVertice(String label) {
    	neighbors.put(label, new LinkedList<Pair<String, Integer>>());
    }

    /**
     * Performs the Dijkstra algorithm to find the shortest path to any node
     * from the specified start node. It returns the destination node as a
     * Vertex object which includes it's path to the start node and the
     * distance to the start node.
     * 
     * @param startNodeLabel the label of the start node.
     * @param destNodeLabel the label of the destination node.
     * @return a Vertex object of the destination node including path and
     * distance to the start node.
     */
    private Vertex dijkstra(String startNodeLabel, String destNodeLabel) {
    	PrioMap<String, Vertex> unknownNodes = getPrioMapOfNodesWithDistInf();
    	Vertex start = unknownNodes.get(startNodeLabel).clone();
    	start.setDist(0);
    	unknownNodes.put(startNodeLabel, start);
    	// dest is the node we will return, will assign it when it's processed.
    	Vertex dest = null;
    	// While there  are still are unknown nodes, poll the one with the shortest
    	// dist. Check its neighbors and update their dist if its closer to get
    	// to them via the polled node.
    	while (unknownNodes.peek() != null) {
    		Vertex v = unknownNodes.poll().b;
    		List<Pair<String, Integer>> adjList = v.getAdjList();
    		if (v.getLabel().equals(destNodeLabel)) {
    			// Assign the destination node since it has been processed.
    			dest = v;
    		}
    		for (Pair<String, Integer> neighbor : adjList) {
    			if (unknownNodes.get(neighbor.a) != null) {
    				Vertex wTemp = unknownNodes.get(neighbor.a);
    				Vertex w = new Vertex(wTemp.getLabel());
    				w.setDist(wTemp.getDist());
    				w.setPath(wTemp.getPath());
    				w.setAdjList(wTemp.getAdjList());
    				int cvw = neighbor.b;
    				if ((v.getDist() + cvw) < w.getDist()) {
    					w.setDist(v.getDist() + cvw);
    					w.setPath(v);
    					unknownNodes.put(w.getLabel(), w);
    				}
    			}
    		}
    	}
    	return dest;
    }

    
    /**
     * Returns the path to the vertex v and saves it in the specified list.
     * 
     * @param v
     * @param list
     */
    private void getPathTo(Vertex v, List<String> list) {
		if (v.getPath() != null) {
			getPathTo(v.getPath(), list);
		}
		list.add(v.getLabel());
	}
    
    /**
     * Takes the nodes in neighbors and puts them in a PrioMap. The key is
     * the label of the vertex and the value is a Vertex object. The Vertex
     * objects are sorted by comparing their dist. It sets all the of the nodes
     * to INFINITY.
     * 
     * @return a PrioMap of the nodes in neighbor with labels as keys and
     * Vertex objects as values with dist INFINITY.
     */
    private PrioMap<String, Vertex> getPrioMapOfNodesWithDistInf() {
    	PrioMap<String, Vertex> unknownNodes = new APrioMap<>();
    	for (String label : neighbors.keySet()) {
    		Vertex vertex = new Vertex(label);
    		vertex.setDist(INFINITY);
    		List<Pair<String, Integer>> adjList = neighbors.get(label);
    		vertex.setAdjList(adjList);
    		unknownNodes.put(label, vertex);
    	}
    	return unknownNodes;
    }
    
	/**
	 * Returns a Path object representing one of the shortest paths between the
	 * start node and the dest node. Returns null if no path exists between the
	 * nodes.
	 * 
	 * @pre the two nodes start and dest must be in the graph.
	 * @param start the start node of the path.
	 * @param dest the last node of the path.
	 * @return a Path object representing one of the shortest paths between
	 * the start node and the dest node. Returns null if no path exists
	 * between the nodes.
	 */
    public Path shortestPath(String start, String dest) {
    	Vertex destVertex = dijkstra(start, dest);
    	int dist = destVertex.getDist();
    	List<String> entirePath = new LinkedList<String>();
    	getPathTo(destVertex, entirePath);
    	if (!entirePath.get(0).equals(start)) {
    		return null;
    	}
    	return new Path(dist, entirePath);
    }
    
    /**
     * Returns and removes the nodeToRemove from the node's neighborList.
     * 
     * @param node the label of the node that should get one less neighbor.
     * @param nodeToBeRemoved label of the node to be removed.
     * @return the label of the removed node, null if it wasn't found.
     */
    private String removeNeighbor(String node, String nodeToRemove) {
    	List<Pair<String, Integer>> neighborList = neighbors.get(node);
    	for (Pair<String, Integer> neighborPair : neighborList) {
    		if (neighborPair.a.equals(nodeToRemove)) {
    			neighborList.remove(neighborPair);
    			return nodeToRemove;
    		}
    	}
    	return null;
    }
    
    @Override
    public String toString() {
    	String str = neighbors.toString();
    	return str;
    }
    
    /**
     * Represents a path from one node to another in the graph.
     */
    public static class Path {
    	
    	/** The sum of the weights of all edges along the path. */
    	public int totalDist;
    	
    	/** A list of vertices in the path, including start and end nodes. */
    	public List<String> vertices;
    	
    	/**  Creates a new path. */
    	public Path(int totalDist, List<String> vertices) {
    		this.totalDist = totalDist;
    		this.vertices = vertices;
    	}
    	
    	/**
    	 * Overrides toString() of Object. Returns a String representation
    	 * of the path.
    	 */
    	@Override
    	public String toString() {
    		return "totalDist: " + totalDist + ", vertices: " + vertices.toString();
    	}
    }
}
