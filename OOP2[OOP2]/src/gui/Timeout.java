package gui;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

//Creating an inactivity timer of 60s. Done by using the Toolkit from AWT to detect certain events from the OS.
public class Timeout {
	private static final int WARNING_TIME = 55; //Warns the user at 55 seconds passed
	private static final int TIMEOUT_TIME = 60; //Ends program at 60s.
	
	private int secondsPassed = 0; //The actual stop-watch in the background.
	private Timer timer; // JSwing Timer 
	private boolean warningShown = false; //Flag to show the warning at 55s.
	
	public Timeout() {
		//creating a timer: every 1 second, he will do action e
		timer = new Timer(1000, e -> {
			secondsPassed++;
		
		
		if(secondsPassed == WARNING_TIME && !warningShown) { //Show warning when our stop-watch reaches 55s.
			warningShown = true;
			new Thread(() ->  { //We run the JOptionPane inactivity warning in a separate thread so as to not pause the GUI (EDT) thread while 
								//the warning is on the screen: without this logic, the program would be stuck in place and the timer wouldnt count
								//for as long as the warning was open.
				JOptionPane.showMessageDialog(null, "Inactivity timer: Application will close in 5 seconds due to inactivity.",
						"Inactivity warning", JOptionPane.WARNING_MESSAGE);
			}).start();
		}
		if(secondsPassed >= TIMEOUT_TIME ) { //If the stop-watch counts 60s: end process.
			System.exit(0);
		}
	});
		
		//To disrupt the inactivity timer, the program needs to track any input from the user:clicking, moving mouse or entering keys on the keyboard.
		//For that purpose AWTEventListener is useful and can be used due to Swing being an upgrade over AWTs base.
		AWTEventListener listener = new AWTEventListener() {
			
			@Override
			public void eventDispatched(AWTEvent event) {
				resetTimer(); //If event is detected by the AWTListener, then reset the timer.
			}
		};
		
		//eventMask must be type long as the actual constants that define an event in AWT are bit masks.
		//eventMask is defined as one of the three events listed above.
		long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK;
		
		//The program uses Toolkit from AWT as a way to communicate with the OS and detect activity (such as ones in eventMask) with the required
		// action to take when the listener detects the event.
		Toolkit.getDefaultToolkit().addAWTEventListener(listener, eventMask);
		
		}
		
	//Method to start the timer.
		public void start() {
			secondsPassed = 0;
			warningShown = false;
			timer.start();
		}
	//Method to reset the timer.
		public synchronized void resetTimer() {
			secondsPassed = 0;
			warningShown = false;
	}
	//Method to pause the timer.
		public void pause() {
			if (timer != null && timer.isRunning() ) {
				timer.stop();
			}
			
		}
	//Method to resume the paused timer.	
		public void resume() {
			if (timer!=null && !timer.isRunning()) {
				timer.start();
			}
		}
}
