package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


import javax.swing.JPanel;
import javax.swing.Timer;

import core.Airport;

//Creating a map of all airports to show on the MainFrame.
public class MapPanel extends JPanel {
	private ArrayList<Airport> airports = new ArrayList<Airport>(); //List of airports.
	private Airport selectedAirport = null; //Airport that is currently selected.
	private Timeout timeout; //inactivity timer (so we can pause it when airport is selected)
	
	private Timer blinkTimer; //timer the program used to make the selected airport blink red.
	private boolean blinkState = false; //flag to see if the selected airport should blink red.
	private static final int RECT_SIZE = 12; //The size of the square that will be drawn at Airport coordinates.
	
	public MapPanel() {
		setBackground(new Color(245,245,245));
		
		blinkTimer = new Timer(400, e-> { //blinks every 0.4 seconds by turning the blinking state on and off.
			blinkState = !blinkState;
			repaint();
		});
		
		//We desire to have an airport select on click, so we need a mouse listener and a method that handles what happens 
		//when we click.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e.getPoint());
			}
		});
		
		
	}
	//Setting the inactivity timer.
	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}
	
	//Setting the airports that we want to draw on the map. 
	//if adding airports or reseting data, we want to stop the blink timer and repaint the map too.
	public void setAirports(ArrayList<Airport> airports) {
		this.airports = airports;
		this.selectedAirport = null;
		if(blinkTimer.isRunning()) {
			blinkTimer.stop();
		}
		repaint();
	}

	//Method for handling mouse clicking event by taking a point on the panel that we clicked on and checking what we clicked.
	protected void handleMouseClick(Point clickPoint) {
		if (airports == null || airports.isEmpty()) return; //if we do not have airports to click on, its pointless to do anything; the map isn't created.
		
		Airport clickedAirport = null; //variable that helps us with knowing which airport the user clicked on.
		
		
		for(Airport airport: airports) {
			if(!airport.isVisible()) continue; //we do not want to show airports that the user has deselected.
			
			//The coordinates (0,0) in Swing are in the top left corner of the screen.. Thats undesirable, so we scale our coordinates
			//to move the center of the "coordinate system" somewhere more desirable, like the center of the screen.
			Point pt = toScreenCoordinates(airport.getX(),airport.getY());
			//Making an invisible rectangle that the program will use as a hitbox to detect clicks on the airport.
			Rectangle rect = new Rectangle(pt.x - RECT_SIZE/2, pt.y - RECT_SIZE/2, RECT_SIZE, RECT_SIZE);
			
			//if the hitbox contains the point that the user clicked on,that means the user clicked on the airport, and so we update the clickedAirport.
			if (rect.contains(clickPoint) ) {
				clickedAirport = airport;
				break;
			}
			
			
			} 
		//If the selectedAirport is the same as the clicked one, that means the user wanted to deselect the airport
		if (clickedAirport != null) {
			if(selectedAirport == clickedAirport) {
				clearSelection();
			} else { //otherwise, it means that the clicked airport should become the selected one.
				selectAirport(clickedAirport);
			}
			
		}
		
		
		
		}
		
	//Method that selects an airport if clicked on
	private void selectAirport(Airport clickedAirport) {
		this.selectedAirport = clickedAirport; //clicked on airport, select it.
		this.blinkState = true; //airport selected, should blink
		
		if(!blinkTimer.isRunning()) { //if the blink timer is not running, make it run so the airport can blink.
			blinkTimer.start();
		}
		if (timeout != null) { //while selected, pause inactivity timer.
			timeout.pause();
		}
		repaint(); //repaint canvas so the blinking actually happens 
		
	}
	
	//Method that deselects an airport.
	private void clearSelection() {
		if (selectedAirport != null || blinkTimer.isRunning()) { //if the airport is blinking
			this.selectedAirport = null; //deselect it.
			if(blinkTimer.isRunning()) { //stop the blinking
				blinkTimer.stop();
			}
		}
		
		if(timeout!=null) { //resume inactivity timer (since airport is deselected.
			timeout.resume();
		}
		repaint(); //repaint map after updates.
	}
	
	//Swing paints starting from the top left corner of the screen. To move the "coordinate center", this method is used.
	private Point toScreenCoordinates(int x, int y) {
		int width = getWidth();
		int height = getHeight();
		
		if (width <= 0) width = 650; //fail-safes: if the program runs before the coordinates initialize on time.
		if (height <= 0) height = 600;
		
		int centerX = width/2; //to move the center, take panel width and height and divide them by half to reach the "true (0,0)".
		int centerY = height/2;
		
		double scale = 5.5; //scaling: so the airports dont clutter in one spot and make testing unreliable.
		
		int screenX = (int) (centerX + (x*scale)); //get true coordinates after the system transformations, using the scale on parsed parameters.
		int screenY = (int) (centerY - (y*scale));
		
		return new Point(screenX,screenY); //initialize new point.
		
	}
	
	// Painting the map
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//Anti-Aliasing on for aesthetics mostly
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//the program cant paint if there are no airports to paint.
		if(airports == null || airports.isEmpty()) {
			return;
		}
		
		
		for (Airport airport: airports) {
			if(!airport.isVisible()) continue; //do not paint airports that the user doesnt wanna see.
			
			//get the coordinates of every airport
			Point pt = toScreenCoordinates(airport.getX(), airport.getY());
			int x = pt.x - RECT_SIZE/2;
			int y = pt.y - RECT_SIZE/2;
			
			//if an airport is selected, make sure it blinks red, otherwise make it gray.
			if(airport.equals(selectedAirport)) {
				g2d.setColor(blinkState?Color.RED:Color.GRAY);
			} else {
				g2d.setColor(Color.GRAY);
			}
			
			//creating colored square, then a black outline around the square.
			g2d.fillRect(x, y, RECT_SIZE, RECT_SIZE);
			g2d.setColor(Color.BLACK);
			g2d.drawRect(x, y, RECT_SIZE, RECT_SIZE);
			
			//Label over every square (Airport code)
			g2d.setFont(new Font("Calibri", Font.BOLD, 12));
			g2d.drawString(airport.getCode(), x + RECT_SIZE + 4, y + RECT_SIZE + - 2);
		}
		
	}
	
}
