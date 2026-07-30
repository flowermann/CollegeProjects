package core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Flight {
	private Airport from;
	private Airport to;
	private LocalTime departureTime;
	private int duration;

	// Formatter for time data. Formats 8:30 to 08:30, for example.
	private static DateTimeFormatter formatT = DateTimeFormatter.ofPattern("HH:mm");

	public Flight(Airport from, Airport to, String departureTime, int duration) throws InvalidFlightDataException {
		if (from == null) { // Checking if source airport is null.
			throw new InvalidFlightDataException("Invalid route, source airport does not exist");
		} else if (to == null) { // Checking if destination airport is null
			throw new InvalidFlightDataException("Invalid route, destination airport does not exist.");
		} else if (from.equals(to)) { // Checking if the destination is the same as the source.
			throw new InvalidFlightDataException("Destination airport cannot be the same as source.");
		}

		if (duration <= 0) { // Checking if the duration of the flight is not less or equal to zero.
			throw new InvalidFlightDataException("Duration of flight cannot be zero or negative.");
		}

		try { // Parsing string time data from csv/json files as LocalTime objects, using the
				// aforementioned formatter.
			this.departureTime = LocalTime.parse(departureTime, formatT);
		} catch (DateTimeParseException | NullPointerException e) { // Catching DateTimeParseException from .parse and
																	// null exceptions.
			throw new InvalidFlightDataException(
					"Departure time was given in an invalid format. Expected format: hh:mm");
		} // All checks passed.
		this.from = from;
		this.to = to;
		this.duration = duration;
	}

	// Getters for fields.
	public Airport getFrom() {
		return from;
	}

	public Airport getTo() {
		return to;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}
	public String getDepartureString() {
		return getDepartureTime().format(formatT);		
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return "Flight [from=" + from + ", to=" + to + ", departureTime=" + departureTime + ", duration=" + duration
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(departureTime, duration, from, to);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		return Objects.equals(departureTime, other.departureTime) && duration == other.duration
				&& Objects.equals(from, other.from) && Objects.equals(to, other.to);
	}
	

}
