package core;

public class Airplane {
	private Flight flight;
	private int elapsedTime;
	private boolean finished;
	
	//Class that the program uses to model airplanes in flight during simulation.
	public Airplane(Flight flight) {
		this.flight = flight; //the airplane in flight must contain reference to the flight at hand.
		this.elapsedTime = 0; //time spent flying: needed for the speed of the plane.
		this.finished = false; //flag: airplane landed.
		
	}
	
	//Method that moves the Airplanes by increasing the elapsed time by a given amount, which will 
	//in turn move the airplane (via method in GUI)
	public void move(int minutes) {
		if (finished) return;
		
		elapsedTime += minutes;
		
		if(elapsedTime >= flight.getDuration()) {
			elapsedTime = flight.getDuration();
			finished = true; //we dont want to advance the airports flight time if he reached the destination.
		}
	}
	
	

	//Getters
	public Flight getFlight() {
		return flight;
	}

	public boolean isFinished() {
		return finished;
	}
	public int getElapsedTime() {
	    return elapsedTime;
	}
	
	
	
}
