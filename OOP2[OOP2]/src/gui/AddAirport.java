package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import core.Airport;
import core.InvalidAirportDataException;
import logic.Manager;


//A button for adding airports.
public class AddAirport extends JDialog {
	private JTextField codeField = new JTextField(); //JTextFields used for inputting strings we will later parse into correct data.
	private JTextField nameField = new JTextField();
	private JTextField xField = new JTextField();
	private JTextField yField = new JTextField();
	private boolean added = false; //flag to see if the airport was added.
	
	//Class only receives the parent frame for constructing the dialog efficiently.
	public AddAirport(JFrame owner) {
		super(owner, "Add new airport", true);
		setLayout(new GridLayout(5,2,5,5));
		setSize(350,200);
		setLocationRelativeTo(owner); //For example here, where we want the popup to be in the center of the screen, relative to the parent frame.
		
		
		//Labels with the adequate text field entries for each field.
		add(new JLabel("Code (Three UPPERCASE letters): "));
		add(codeField);
		add(new JLabel("Name: "));
		add(nameField);
		add(new JLabel("X [-180, 180]: "));
		add(xField);
		add(new JLabel("Y [-90,90]: "));
		add(yField);
		
		//button instancing
		JButton add = new JButton("Add");
		JButton cancel = new JButton("Cancel");
		
		
		//Event listeners added to buttons
		add.addActionListener(e -> addClick());
		cancel.addActionListener(e -> dispose());
		
		add(add);
		add(cancel); //adding buttons to dialog
		
	}

	//Method that parses the inputed data and adds it when the user clicks "Add Airport"
	private void addClick()  {
		String code = codeField.getText().trim().toUpperCase();
		String name = nameField.getText().trim();
		String xStr = xField.getText().trim();
		String yStr = yField.getText().trim();
		
		//Checks: seeing if the user entered the correct format listed in the labels.
		//Is code a three upper case letter string?
		if(!code.matches("[A-Z]{3}")) {
			JOptionPane.showMessageDialog(this, "Airport code must be 3 uppercase letters. (Ex. Belgrade - BEG)", 
					"Incorrect input.", JOptionPane.ERROR_MESSAGE); // if not, show an error message
		return;
		} 
		
		//Check: duplicate airports
		for(Airport a: Manager.getInstance().getAirports()) {
			if (a.getCode().equals(code)) {
				JOptionPane.showMessageDialog(this, "Airport with code: " + code + "already exists!",
						"Duplicate error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		//Check: if the name inputed is blank.
		if(name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Airport name must not be blank.", 
					"Blank name error", JOptionPane.ERROR_MESSAGE);
		return;
		} 
		
		//Check: seeing if the entered coordinates are out of bounds or if the user inputed wrong data type.
		int x,y;
		try {
			x = Integer.parseInt(xStr);
			y = Integer.parseInt(yStr);
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Coordinates x and y must be integers!",
					"Invalid number type!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (x <-180 || x>180) {
			JOptionPane.showMessageDialog(this, "X coordinate must be in range [-180,180]",
					"Out of bounds. ", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (y<-90 || y>90) {
			JOptionPane.showMessageDialog(this, "Y coordinate must be in range [-90,90]",
					"Out of bounds.", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//All checks passed, adding airport to database.
		try {
			Airport newAirport = new Airport(code, name, x, y);
			Manager.getInstance().getAirports().add(newAirport);
			Manager.getInstance().getGraph().addAirport(newAirport);
			added = true;
			dispose();
		} catch (InvalidAirportDataException e) { //Unless invalid airport data was inputed (failed constructor checks)
			JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid airport data", JOptionPane.ERROR_MESSAGE);
		}
	}
	public boolean isAdded() { //checking to see if the airport was added.
	    return added;
	}
}


