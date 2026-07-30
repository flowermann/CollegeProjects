package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import core.Airport;
import core.Flight;

public class CsvWrite {

	public static void writeCsv(String filePath, ArrayList<Airport> airports, ArrayList<Flight> flights) throws IOException {
		
		//Using BufferedWriter for similar reasons as to why I used BufferedReader.
		//First writing airports, with segregator and header first, blank lines there for better visibility.
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			if(airports!=null && !airports.isEmpty()) {
				writer.write("# AIRPORTS");
				writer.newLine();
				writer.write("CODE,NAME,X,Y");
				writer.newLine();
				
				//Iterating line by line with the correct formatting for the csv file, using fields from the Airport class.
				for(Airport airport: airports) {
					String line = String.format("%s,%s,%s,%s", 
							airport.getCode(),
							airport.getName(),
							airport.getX(),
							airport.getY()
							);
					writer.write(line);
					writer.newLine();
				}
			}
			
			writer.newLine();
			
			//Writing flights, first writing the segregator and header, and then formatting each line with the correct data types 
			//for the fields in Flight.
			if(flights != null && !flights.isEmpty()) {
				writer.write("# FLIGHTS");
				writer.newLine();
				writer.write("FROM,TO,DEPARTURE,DURATION");
				writer.newLine();
				
				for(Flight flight: flights) {
					String line = String.format("%s,%s,%s,%s", 
							flight.getFrom().getCode(),
							flight.getTo().getCode(),
							flight.getDepartureString(),
							flight.getDuration());
					writer.write(line);
					writer.newLine();
				}
			}
		}
	}
}
