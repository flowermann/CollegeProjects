package io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.Airport;
import core.Flight;

public class JsonWrite {
	//To be able to write data into the file using Strings and integers, the program must first convert the fields into
	//the correct type of data. For that purpose, similarly to json parsing, three static classes are made.
	//They will convert said data using their own constructors, and afterwards be stored in lists under JsonData.
	private static class AirportData {
		public String code;
		public String name;
		public int x;
		public int y;
		
		public AirportData(Airport airport) {
			this.code = airport.getCode();
			this.name = airport.getName();
			this.x = airport.getX();
			this.y = airport.getY();
		}
	}
	private static class FlightData {
		public String from;
		public String to;
		public String departure;
		public int duration;
		
		public FlightData(Flight flight) {
			this.from = flight.getFrom().getCode();
			this.to = flight.getTo().getCode();
			this.departure = flight.getDepartureString();
			this.duration = flight.getDuration();		}
	}
	
	private static class JsonData {
		public ArrayList<AirportData> airports = new ArrayList<AirportData>();
		public ArrayList<FlightData> flights = new ArrayList<FlightData>();
	}

	
	//Method for writing in desired file path.
	public static void writeJson(String filePath, ArrayList<Airport> airports, ArrayList<Flight> flights) 
			throws IOException {
		
		JsonData data = new JsonData();
		
		//Iterating through airports in current program and creating a AirportData object for each.
		if (airports != null) {
			for (Airport airport: airports) {
				data.airports.add(new AirportData(airport));
			}
		}
		//Iterating through flights in current program and creating a FlightData object for each
		if (flights != null) {
			for(Flight flight: flights) {
				data.flights.add(new FlightData(flight));
			}
		} //The program has made a list of Airports and Flights with the desired data types, ready for writing into file.
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		//Try-with-resources and writing into a json file.
		try(FileWriter writer = new FileWriter(filePath)) {
			gson.toJson(data,writer);
		}
	}

}


