package io;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import core.Airport;
import core.Flight;
import core.InvalidAirportDataException;
import core.InvalidFlightDataException;

public class JsonParsing {
// Json parsing implemented using Googles Gson library. 
	//Three Static classes for raw data parsed through Gson, which the program will convert into correct data later.
	private static class AirportData {
		public String code;
		public String name;
		public int x;
		public int y;
	}
	
	private static class FlightData {
		public String from;
		public String to;
		public String departure;
		public int duration;
	}

	// Lists for our raw Gson data.
	private static class JsonClassData {
		public ArrayList<AirportData> airports;
		public ArrayList<FlightData> flights;
	}
	
	//The result of our json parsing will be returned to ParsingResult class. Since our parsing has to return both Airports
	//and Flights, its best that a result class exists with valid getters for both lists, regardless of what file type is used.
	public static ParsingResult loadJson(String filePath) throws IOException {
		Gson gson = new Gson(); 

		// Lists made so we can pass them to ParsingResults constructor, which contains two lists.
		ArrayList<Airport> jsonAirport = new ArrayList<Airport>();
		ArrayList<Flight> jsonFlight = new ArrayList<Flight>();

		//Map used so as to map each airport code to its valid airport object, since Flight class takes Airport objects (to and from)
		// in its constructor. Having a map of said codes connected to each airport object gives us O(1) time.
		HashMap<String, Airport> mapping = new HashMap<String, Airport>();

		//Try-with-resources. Gson parses through the file, creates an adequate object to store the lists of both raw Airport and Flight data
		//and checks to see if the created class was empty (nothing was parsed).
		try (FileReader reader = new FileReader(filePath)) {
			JsonClassData data = gson.fromJson(reader, JsonClassData.class);

			if (data == null) {
				System.out.println("Error: JSON file empty. No data loaded.");
				return new ParsingResult(jsonAirport, jsonFlight);
			} // returns empty lists.

			//******* 1. Airport parsing
			if (data.airports != null) {
				for (AirportData parseA : data.airports) {
					try {
						Airport airport = new Airport(parseA.code,parseA.name, parseA.x, parseA.y);
						jsonAirport.add(airport);
						mapping.put(airport.getCode(), airport); //used for later flight parsing, as mentioned before.
					} catch(InvalidAirportDataException e) { //Error catching after the class constructors check for errors.
						System.out.println("Invalid airport detected:[" + parseA.code + "]. Removed from list. Error: " + e.getMessage());
					}
				}
			}
		

		//******** 2. Flight parsing
			if (data.flights != null) {
				for(FlightData parseF: data.flights) {
					try { 
						//Using the map to create adequate airport objects, as the Flight constructor requires them.
						Airport from = mapping.get(parseF.from);
						Airport to = mapping.get(parseF.to);
						if(from==null || to == null) { //Check to see if said airports exist.
							throw new InvalidFlightDataException("Source or destination airport do not exist.");}
						
						Flight flight = new Flight(from,to,parseF.departure,parseF.duration);
						jsonFlight.add(flight);
						
					} catch(InvalidFlightDataException e) { //Catching errors.
						System.out.println("Invalid flight data detected: [" + parseF.from + "->" + parseF.to + "]. Removed from list. Error: " 
								+ e.getMessage());
					}
				}
			}
		}
		return new ParsingResult(jsonAirport, jsonFlight); //New result object with both Airport and Flight parsed data.
	}
}
