package logic;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.Timer;

import core.Airplane;
import core.Airport;
import core.Flight;


//Class containing logic for the simulation of flights.
public class Simulation {
	private int currentTime; //time in simulation
	private boolean running;//flag to see if the simulation is running
	private Timer timer; // timer is necessary so the program can move the airplanes every 200 ms (2 minutes in simulation time)
	
	private ArrayList<Flight> pendingFlights; //Flights that need to depart.
	private HashMap<Airport, Queue<Flight>> departureQueue; //A map: each airport has multiple flights.
															//If two flights happen at the same time, one must wait 10 in-sim minutes to depart.
															//Because of that, a queue is implemented for a FIFO approach.
	private HashMap<Airport, Integer> lastDepartureTime; //To ensure coinciding flights wait 10 in-sim minutes, we need to remember for each airport
														 // the departure time of the last flight that took place.
	private ArrayList<Airplane> activeAirplanes; //Airplanes currently flying. 
	
	private ActionListener callback; //ActionListener: we need it so the map knows to repaint each time the simulation state is changed.
	
	
	public Simulation(ActionListener callback) {
		this.callback = callback;
		this.currentTime = 0;
		this.running = false;
		
		this.pendingFlights = new ArrayList<Flight>();
		this.departureQueue = new HashMap<Airport, Queue<Flight>>();
		this.lastDepartureTime = new HashMap<Airport, Integer>();
		this.activeAirplanes = new ArrayList<Airplane>();
		
		this.timer = new Timer(200, e -> step()); //we will move our airplanes every 200 ms.
	}
	
	//Method for initializing the simulation.
	public void initialize(ArrayList<Flight> flights) {
		reset();
		this.pendingFlights = new ArrayList<Flight>(flights);
		//we need to sort the list of pending flights by departure time so as to make sure the flights with the earliest
		//departure time leave first.
		this.pendingFlights.sort(Comparator.comparingInt(flight -> flight.getDepartureInMinutes())); 
	}
	
	//Methods for GUI buttons: start, pause, reset.
	public void start() {
		if (!running) {
			running = true;
			timer.start();
		}
	}
	public void pause() {
		if (running) {
			running = false;
			timer.stop();
		}
	}
	
	public void reset() {
		pause();
		currentTime = 0;
		pendingFlights.clear();
		departureQueue.clear();
		lastDepartureTime.clear();
		activeAirplanes.clear();
		
		if(callback != null) {
			callback.actionPerformed(null);
		}
	}
	
	//Method to move our airplanes on the map using our move() method from Airplanes.
	//linear interpolation is used to determine where our planes need to go next.
	public void step() {
		currentTime += 2; //with each step, we want in simulation time to increase by 2 (200 ms - 2sim min)
		
		//Iterating through our pending flights
		Iterator<Flight> iterator = pendingFlights.iterator();
		while (iterator.hasNext()) {
			Flight flight = iterator.next();
			if (flight.getDepartureInMinutes() <= currentTime) { //checking to see if the flight should depart
				Airport src = flight.getFrom(); //for current airport, put the flight in the queue of flights
				departureQueue.putIfAbsent(src, new LinkedList<Flight>());
				departureQueue.get(src).add(flight);
				iterator.remove(); //remove current flight from iteration.
			} else break;
		}
		
		
		//for each airport check the queue of flights, get the departure time from last flight and see if 
		//the earliest available flight in the queue can start (if not, it will depart 10 (sim) minutes after the last one).
		for (Airport airport: departureQueue.keySet()) {
			Queue<Flight> queue = departureQueue.get(airport);
			if(queue != null && !queue.isEmpty()) {
				int lastDeparture = lastDepartureTime.getOrDefault(airport, -100);
				
				if (currentTime - lastDeparture >= 10) {
					Flight flightDepart = queue.poll();
					activeAirplanes.add(new Airplane(flightDepart)); //if flight started, that means we create a new Airplane with said flight.
					lastDepartureTime.put(airport, currentTime); //update the last departure time after the flight starts.
					
				}
			}
		}
		
		//for every active airplane, we want to make sure it moves on the map. 
		//as such, we iterate through the list of active airplanes and move them 
		//with every tick of the simulation. (in this case, 200ms = 2 in-sim min)
		Iterator<Airplane> airplaneIterator = activeAirplanes.iterator();
		while(airplaneIterator.hasNext()) {
			Airplane airplane = airplaneIterator.next();
			airplane.move(2);
			if (airplane.isFinished()) {
				airplaneIterator.remove(); //if the status flag for finished route in Airplane is up, we remove from list.
			}
		}
		if(callback!=null) {
			callback.actionPerformed(null);
			//notify GUI.
		}
	}
	
	//Getter that allows us to change the time label of the simulation every tick.
	public String getFormattedTime() {
		int hours = (currentTime/60) % 24;
		int minutes = currentTime % 60;
		return String.format("%02d:%02d", hours,minutes);
	}
	
	//Other notable getters.
	public int getCurrentTime() {
		return currentTime;
	}

	public boolean isRunning() {
		return running;
	}

	public ArrayList<Airplane> getActiveAirplanes() {
		return activeAirplanes;
	}

	
	
	
}
