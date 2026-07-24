package core;

import java.util.Objects;

public class Airport {
	private String code;
	private String name;
	private int x;
	private int y;

	public Airport(String code, String name, int x, int y) throws InvalidAirportDataException {
		// Checking code input.
		if (code == null) { // Checking if inputed code is null.
			throw new InvalidAirportDataException("Invalid Code: Code does not exist.");
		} else if (!code.matches("^[A-Z]{3}$")) { // Checking if inputed code is longer than 3 letters or not uppercase.
			throw new InvalidAirportDataException("Airport code must be exactly 3 uppercase letters.");
		}

		if (name == null) { // Checking if inputed name is null.
			throw new InvalidAirportDataException("No airport with given name exists");

		} else if (name.trim().isEmpty()) { // Checking if inputed name is blank.
			throw new InvalidAirportDataException("Airport name cannot be blank.");
		}

		if (x < -180 || x > 180) { // Checking if inputed x-coordinate is out of bounds.
			throw new InvalidAirportDataException("X-coordinate out of bounds.");
		}
		if (y < -90 || y > 90) { // Checking if inputed y-coordinate is out of bounds.
			throw new InvalidAirportDataException("Y-coordinate out of bounds.");
		}
		// All Checks passed.
		this.code = code;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	// Getters for all fields.
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	// Two airports are the same if their codes are the same.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Airport other = (Airport) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return "Airport [code=" + code + ", name=" + name + ", x=" + x + ", y=" + y + "]";
	}

}
