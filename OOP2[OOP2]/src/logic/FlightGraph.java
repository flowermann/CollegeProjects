package logic;

import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.graph.DirectedWeightedMultigraph;

import core.Airport;
import core.Flight;

//Logic of the program (connecting of airports and flights) done using A directed weighted multigraph structure, implemented
//using the JGraphT library.

public class FlightGraph {

	private DirectedWeightedMultigraph<Airport, FlightEdge> graph;
	
	public FlightGraph() {
		this.graph = new DirectedWeightedMultigraph<Airport, FlightEdge>(FlightEdge.class);
	}
	
	//class that builds a graph using airports as vertices and flights as edges. if graph already exists, then it rebuilds over it.
	public void rebuildGraph (ArrayList<Airport> airports, ArrayList<Flight> flights) { 
		//reset graph (if made before) for security
		this.graph = new DirectedWeightedMultigraph<Airport, FlightEdge>(FlightEdge.class);
		
		if (airports!=null) { //checking to see if our vertices list is null
			for(Airport airport: airports) {
				addAirport(airport); //method that adds a vertex to our graph, in this case: an airport.
			}
		}
		if (flights!=null) {// checking to see if our edges list is null
			for(Flight flight: flights) {
				addFlight(flight); //method that adds an edge to our graph, in this case: a flight.
			}
		}
	}

	public boolean addAirport(Airport airport) {
		if (airport == null || graph.containsVertex(airport)) {
			return false;
		} return graph.addVertex(airport); //checks to see if airport we have exists or is already a vertex in the graph
		// if checks are passed, adds a vertex(airport) to the graph.
		
	}
	
	public boolean removeAirport(Airport airport) {
		if (airport == null || !graph.containsVertex(airport)) { //checks to see if airport exists or the graph already contains it.
			return false;
		} return graph.removeVertex(airport); //if checks passed, removes a vertex(airport) from the graph.
	}
	
	
	public boolean addFlight(Flight flight) {
		if (flight == null) { //checks to see if flight exists.
			return false;
		} 
		Airport from = flight.getFrom();
		Airport to = flight.getTo(); //its necessary to check whether out source and destination airports exist. 
									// an edge cannot exist without both vertices existing in the graph beforehand.
		
		addAirport(from); // Adds both from and to airports to the graph. If they already exists, the method wont make duplicates.
		addAirport(to);
		
		FlightEdge edge = graph.addEdge(from, to); //Creating new edge object between nodes signifed by from and to.
		if (edge != null) { //if the edge actually exists
			edge.setFlight(flight); //give the new edge the information it needs from the given Flight.
			graph.setEdgeWeight(edge, flight.getDuration()); //extract duration from Flight as the weight of this edge (flight).
			return true;
		} return false; //if the edge was never added (is null), then dont add anything. Adding failed.
		
	}
	
	public boolean removeFlight(Flight flight) {
		if (flight == null) { //Checks to see if inputed flight exists. Abort if doesn't.
			return false;
		} Set<FlightEdge> edges = graph.getAllEdges(flight.getFrom(), flight.getTo()); //Create a set of all edges between from and to airports
																					   // of said flight (since this is a multigraph).
		if (edges == null) {
			return false; //no edges between the two airports were found. Abort.
		} 
		for(FlightEdge edge: edges) { //Iterate through all the found edges and find the one that matches all the information given in Flight argument.
			if (edge.getFlight() != null && edge.getFlight().equals(flight)) { 
				return graph.removeEdge(edge); //Remove the edge if found.
			}
		}
		return false;
	}

	// Getters for the graph, a set of all vertices and all edges.
	public DirectedWeightedMultigraph<Airport, FlightEdge> getGraph() {
		return graph;
	}
	public Set<Airport> getAllVertices() {
		return graph.vertexSet();
	}
	public Set<FlightEdge> getAllEdges() {
		return graph.edgeSet();
	}
}
