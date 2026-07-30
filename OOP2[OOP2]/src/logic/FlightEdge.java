package logic;

import org.jgrapht.graph.DefaultWeightedEdge;

import core.Flight;

//Wrapper class for DefaultWeightedEdge to add information from Flight class to the edges of the graph.
//In other words, these are the edges of our graph, that require all the information that the Flight class have, because our Flights
//are edges in this graph. To do this, we have a setter for the edge that receives a Flight object.
public class FlightEdge extends DefaultWeightedEdge {
	private Flight flight;
	
	public FlightEdge() {
		super();
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	@Override
	public String toString() {
		return flight!=null ? flight.toString(): super.toString();
	}
	

}
