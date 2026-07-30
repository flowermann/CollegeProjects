package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

public final class GraphUtils {
	
	
	private GraphUtils() {}
	
	public static <V, E> GraphPath<V, E> dijkstraBetweenNodes(Graph<V, E> graph, V source, V target) {
		DijkstraShortestPath<V, E> dijkstra = new DijkstraShortestPath<V, E>(graph);
		return dijkstra.getPath(source, target);
	}
	
	public static <V, E> FloydWarshallShortestPaths<V, E> floydWarshall (Graph<V, E> graph) {
		return new FloydWarshallShortestPaths<V, E>(graph);
	}
	
	public static <V,E> ArrayList<V> getBFS(Graph<V, E> graph, V startNode) {
		ArrayList<V> result = new ArrayList<V>();
		BreadthFirstIterator<V, E> bfs = new BreadthFirstIterator<V, E>(graph, startNode);
		
		while(bfs.hasNext()) {
			result.add(bfs.next());
		}
		return result;
	}
	
	public static <V, E> ArrayList<V> getDFS(Graph<V, E> graph,V startNode) {
		ArrayList<V> result = new ArrayList<V>();
		DepthFirstIterator<V, E> dfs = new DepthFirstIterator<V, E>(graph, startNode);
		
		while (dfs.hasNext()) {
			result.add(dfs.next());
		} return result;
	}
	
	public static <V, E> Map<V, Double> getAirportImportance(Graph<V, E> graph) {
	    BetweennessCentrality<V, E> bc = new BetweennessCentrality<>(graph);
	    return bc.getScores();
} 
	
	public static <V, E> boolean isGraphConnected(Graph<V, E> graph) {
	    ConnectivityInspector<V, E> inspector = new ConnectivityInspector<>(graph);
	    return inspector.isConnected();
	}

	public static <V, E> List<Set<V>> getConnectedComponents(Graph<V, E> graph) {
	    ConnectivityInspector<V, E> inspector = new ConnectivityInspector<>(graph);
	    return inspector.connectedSets();
	}
	

	public static <V, E> Set<E> getMinimumSpanningTree(Graph<V, E> graph) {
	    KruskalMinimumSpanningTree<V, E> mst = new KruskalMinimumSpanningTree<>(graph);
	    return mst.getSpanningTree().getEdges(); 
	}
	
	public static <V, E> boolean hasCycles(Graph<V, E> graph) {
	    CycleDetector<V, E> cycleDetector = new CycleDetector<>(graph);
	    return cycleDetector.detectCycles();
	
		} 
	}


