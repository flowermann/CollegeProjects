package io;

import java.io.IOException;


//Class used to determine what type of file is the program loading, and subsequently calling the correct methods.
public class DataInput {
	public static ParsingResult loadData(String filePath) throws IOException {
		// Checking to see if path is valid. 
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("File path not found or is empty.");
		}
		
		//Converting path to lower case to check for file extensions.
		String path = filePath.toLowerCase();
		if(path.endsWith(".json")) {
			return JsonParsing.loadJson(filePath); //Json file detected, parse json.
		} else if(path.endsWith(".csv")) {
			return CsvParsing.loadCsv(filePath); //Csv file detected, parse csv.
		} else {
			throw new IllegalArgumentException("Unsupported file format. Expected formats: .json/.csv"); //Unsupported file format detected.
		}
		
		
	}
}
