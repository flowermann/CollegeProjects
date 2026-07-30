package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import core.Airport;
import core.Flight;
import core.InvalidAirportDataException;
import core.InvalidFlightDataException;

public class CsvParsing {
	//Use of Enum to follow the state of the program and decide whether to parse flights or Airports. 
	private enum Selector {
		NULL,
		AIRPORTS,
		FLIGHTS
	}
	
	public static ParsingResult loadCsv (String filePath) throws IOException {
		//Lists to store the parsed data in, so as to forward them to the ParsingResult constructor for final program data.
		//Method returns both airports and flights, so it is necessary to have two lists and let ParsingResult return them both with adequate getters.
		ArrayList<Airport> csvAirport = new ArrayList<Airport>();
		ArrayList<Flight> csvFlight = new ArrayList<Flight>();
		
		//Map used later for mapping the code of an airport to its adequate class, similar to Json parsing.
		HashMap<String, Airport> mapping = new HashMap<String, Airport>();
		
		//Program state.
		Selector state = Selector.NULL;
		
		//Try-with-resources. Used BufferedReader for implementation, because it is simple to use, fast, and allows me to read line by line
		// which is how i parsed the csv data.
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			//Skipping empty spaces.
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				//Skipping the denoting strings that signify what type of data awaits next. Changing the program state to allow
				//loading of correct data later.
				if(line.equalsIgnoreCase("# AIRPORTS")) {
					state = Selector.AIRPORTS;
					continue;
				}
				else if (line.equalsIgnoreCase("# FLIGHTS")) {
					state = Selector.FLIGHTS;
					continue;
				}
				//Skipping the csv header.
				if (line.startsWith("CODE,") || line.startsWith("FROM,")) {
					continue;
				}
			
				//Tokenizing our parse data for each line and calling the adequate method according to the program state.
				String[] tokens = line.split(",");
				
				if (state == Selector.AIRPORTS) {
					parseAirports(tokens,csvAirport,mapping);
				} else if(state == Selector.FLIGHTS) {
					parseFlights(tokens,csvFlight, mapping);
				}
			}
			// If nothing was loaded into the lists, the file was either empty, or simply written in an unexpected format. 
			if (csvAirport.isEmpty() && csvFlight.isEmpty()) {
				throw new IllegalArgumentException("The file is written in an incorrect format from expected or is empty.");
			}
		}
		// Returning the result of parsing.
		return new ParsingResult(csvAirport,csvFlight);
		
	}

	//Method for parsing a single line, parsing airport.
	private static void parseAirports(String[] tokens, ArrayList<Airport> csvAirport,
			HashMap<String, Airport> mapping) {
		
			if (tokens.length < 4) { //the Airport class has 4 fields. 
				System.out.println("Error loading csv file: Detected row with insufficient columns(4 columns required).");
				return;
			}
			try { //Extracting data from the tokens constructed for current line in loadCsv().
				String code = tokens[0].trim();
				String name = tokens[1].trim();
				int x = Integer.parseInt(tokens[2].trim());
				int y = Integer.parseInt(tokens[3].trim());
				
				//Constructing airport object, adding to parse list and mapping current airport code with its adequate class for later use.
				Airport airport = new Airport(code,name,x,y);
				csvAirport.add(airport);
				mapping.put(airport.getCode(), airport);
			} catch(NumberFormatException e) { //Error catching: parseInt must catch this error.
				System.out.println("Error: Integer value in csv file could not be read (invalid format or typo). Row: "+ String.join(",", tokens));
			} catch(InvalidAirportDataException e) { // Constructor detected invalid data.
				System.out.println("Invalid airport data detected: [" + tokens[0] + "]. Error: " + e.getMessage());
			}
		
	} 
	
	//Method parsing a single line, parsing flight.
	private static void parseFlights(String[] tokens, ArrayList<Flight> csvFlight, HashMap<String, Airport> mapping) {
		if (tokens.length < 4) { //Flight class has 4 fields.
			System.out.println("Error loading csv file: Detected row with insufficient columns(4 columns required).");
			return;
		}
		try { //Extracting data from tokens.
			String fromSt = tokens[0].trim();
			String toSt = tokens[1].trim();
			String departure = tokens[2].trim();
			int duration = Integer.parseInt(tokens[3].trim());
			
			//Converting string data to adequate Airport objects, as the Flight class requires Object type Airport in its Constructor.
			//Afterwards, checking if said airports exist.
			Airport from = mapping.get(fromSt);
			Airport to = mapping.get(toSt);
			if (from == null || to == null) {
				throw new InvalidFlightDataException("Source or destination airport do not exist.");
			}
			Flight flight = new Flight(from, to, departure, duration);
			csvFlight.add(flight);
			
		} catch(NumberFormatException e) { //Same error catching as the previous method.
			System.out.println("Error: Integer value in csv file could not be read (invalid format or typo). Row: "+ String.join(",", tokens));
		} catch(InvalidFlightDataException e) {
			System.out.println("Invalid flight data detected: [" + tokens[0] + "->" + tokens[1] + "]. Error: "  + e.getMessage());
		}
	}
}
