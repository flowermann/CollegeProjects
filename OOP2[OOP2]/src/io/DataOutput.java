package io;

import java.io.IOException;
import java.util.ArrayList;

import core.Airport;
import core.Flight;

//Similar class to DataInput (Practically the exact same), only different in arguments. Choosing which type of file to make according to
// the extension named in file path. 

public class DataOutput {
	public static void writeData(String filePath, ArrayList<Airport> airports, ArrayList<Flight> flights) throws IOException {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("File path does not exist or nothing has been inputed as path.");
		}
		String path = filePath.toLowerCase();
		if (path.endsWith(".json")) {
			JsonWrite.writeJson(filePath, airports, flights);
		} else if (path.endsWith(".csv")) {
			CsvWrite.writeCsv(filePath, airports, flights);
		} else {
			throw new IllegalArgumentException("Unsupported file format. Expected formats: json/csv.");
		}
		
		}
	}

