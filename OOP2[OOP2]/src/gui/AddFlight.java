package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import core.Airport;
import core.Flight;
import core.InvalidFlightDataException;
import logic.Manager;
//Very similar class to AddAirport.java, so i wont comment too much on it. Still, there are some differences.
public class AddFlight extends JDialog{
	private JComboBox<Airport> fromCombo; //Since flights are made between existing airports, the program asks of the user to pick 
										  // an airport from the selection of existing airports (Hence why JComboBox is used).
	private JComboBox<Airport> toCombo;
	private JTextField departureField = new JTextField();
	private JTextField durationField = new JTextField();
	private boolean added = false;
	
	public AddFlight(JFrame owner) {
		super(owner, "Add new flight", true);
        setLayout(new GridLayout(5, 2, 5, 5));
        setSize(400, 220);
        setLocationRelativeTo(owner);

        //JComboBox requires an array of objects, so its necessary to turn our Array List to an array of Airports.
        Airport[] airportsArr = Manager.getInstance().getAirports().toArray(new Airport[0]);

        //Everything else is pretty much identical to AddAirport.java 
        fromCombo = new JComboBox<>(airportsArr);
        toCombo = new JComboBox<>(airportsArr);

        add(new JLabel(" Departure (From):"));
        add(fromCombo);

        add(new JLabel(" Destination (To):"));
        add(toCombo);

        add(new JLabel(" Departure time. Format:(HH:mm):"));
        add(departureField);

        add(new JLabel(" Duration (minutes):"));
        add(durationField);

        JButton add = new JButton("Add");
        JButton cancel = new JButton("Cancel");

        add.addActionListener(e -> addClick());
        cancel.addActionListener(e -> dispose());

        add(add);
        add(cancel);
    }

	private void addClick() {
		Airport from = (Airport) fromCombo.getSelectedItem();
		Airport to = (Airport) toCombo.getSelectedItem();
		String departureString = departureField.getText().trim();
		String durationString = durationField.getText().trim();
		
		//Checks for inputed user data and parsing it to the program.
		//Check: if the airports selected exist.
		if(from == null || to == null) {
			JOptionPane.showMessageDialog(this, "Selected airports do not exist.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Check: if the departure and destination airports are the same.
		if (from.getCode().equals(to.getCode())) {
			JOptionPane.showMessageDialog(this, "Departure and destination airports are the same.",
					"Impossible flight", JOptionPane.ERROR_MESSAGE);
			return;
		} // Check: if the inputed departure time does not follow the required format.
		if(!departureString.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
			JOptionPane.showMessageDialog(this, "Wrong format for departure time. Format: HH:mm",
					"Wrong string format", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int duration;
		try { //parsing our duration and checking if its a valid data type and if its a positive integer.
			duration = Integer.parseInt(durationString);
			if(duration <= 0) {
				JOptionPane.showMessageDialog(this, "Duration of flight must be a positive integer.",
						"Invalid duration", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Duration of flight must be an integer.",
					"Invalid duration", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try { //All checks passed, adding flight to database.
			Flight newFlight = new Flight(from,to,departureString,duration);
			Manager.getInstance().getFlights().add(newFlight);
			Manager.getInstance().getGraph().addFlight(newFlight);
			
			added = true;
			dispose();
		} catch(InvalidFlightDataException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Invalid flight data!", JOptionPane.ERROR_MESSAGE);
		}
	}
	public boolean isAdded() {
		return added;
	}
}
