package core;

public class InvalidFlightDataException extends Exception {
	public InvalidFlightDataException(String error) {
		super(error);
	}
}
