package logic;


import java.io.IOException;
import java.util.ArrayList;

import core.Airport;
import core.Flight;
import io.DataInput;
import io.DataOutput;
import io.ParsingResult;

//Singleton class that will serve as a connecting controller between all classes in the project and a direct connector between the GUI and 
//the program logic. 

public class Manager {
	private static Manager instance; //Field for singleton construction: only one instance of class allowed.
	
	//All other fields are simply the data we parsed to our core classes, and connecting them by creating a graph.
	private ArrayList<Airport> airports;
	private ArrayList<Flight> flights;
	private FlightGraph graph;
	
	//Singleton constructor.
	private Manager() {
		this.airports = new ArrayList<Airport>();
		this.flights = new ArrayList<Flight>();
		this.graph = new FlightGraph();
	}
	//Creating an instance of this class. Singleton logic.
	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}
		return instance;
	}
	
	//Loading data from json/csv files. DataInput class resolves which file type to read, and then calls the correct methods for parsing it.
	public void loadData(String filePath) throws IOException, IllegalArgumentException {
		ParsingResult result = DataInput.loadData(filePath);
		
		if(result!=null) {
			this.airports = result.getAirports();
			this.flights = result.getFlights();
			this.graph.rebuildGraph(this.airports, this.flights); //Creating a graph right away from loaded, parsed data.
		}
	}
	//Writing program data in json/csv format. DataInput class decides what format will be chosen, and calls the correct method for writing the data.
	public void writeData(String filePath, ArrayList<Airport> airports, ArrayList<Flight> flights) throws IOException,
	IllegalArgumentException {
		
		DataOutput.writeData(filePath, this.airports, this.flights);
	}
	
	//Classes to add and remove airports and flights, both from our lists of both and also to/from our graph. Logic already handled by FlightGraph.
	public void addAirport(Airport airport) {
		if (airport!=null && !airports.contains(airport)) {
			airports.add(airport);
			graph.addAirport(airport);
		}
	}
	public void removeAirport (Airport airport) {
		if (airport!=null && airports.contains(airport)) {
			airports.remove(airport);
			graph.removeAirport(airport);
		}
	}
	public void addFlight (Flight flight) {
		if (flight!=null) {
			flights.add(flight);
			graph.addFlight(flight);
		}
	}
	public void removeFlight (Flight flight) {
		if (flight!=null) {
			flights.remove(flight);
			graph.removeFlight(flight);
		}
	}
	// Since this class connects all our real data into one place, it is convenient to have getters for the data.
	public ArrayList<Airport> getAirports() {
		return airports;
	}
	public ArrayList<Flight> getFlights() {
		return flights;
	}
	public FlightGraph getGraph() {
		return graph;
	}
	
} 
