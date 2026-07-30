package io;

import java.util.ArrayList;

import core.Airport;
import core.Flight;

//Class that will contain parsing results, regardless of file extension, with appropriate getters for both Airport and Flight data.
public class ParsingResult {
	private ArrayList<Airport> airports;
	private ArrayList<Flight> flights;
	
	public ParsingResult(ArrayList<Airport> airports, ArrayList<Flight> flights) {
		this.airports = airports;
		this.flights = flights;
	}

	public ArrayList<Airport> getAirports() {
		return airports;
	}

	public ArrayList<Flight> getFlights() {
		return flights;
	}
	
}
