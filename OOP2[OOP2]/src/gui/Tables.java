package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import core.Airport;
import core.Flight;
import logic.Manager;

public class Tables extends JPanel {
	private JTable airportsTable; //Table for airport data
	private JTable flightsTable; // Table for flight data
	 
	private DefaultTableModel airportsModel; //Model that will be used to create the Airport table
	private DefaultTableModel flightsModel;  //Model that will be used to create the Flights table

	public Tables() {
		setLayout(new BorderLayout(5,5)); 
		
		JPanel addingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		JButton addAirport = new JButton("Add Airport");
		JButton addFlight = new JButton("Add Flight");
		
		addAirport.addActionListener(e -> {
			JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			AddAirport dialog = new AddAirport(parentFrame);
			dialog.setVisible(true);
			if(dialog.isAdded()) {
				refreshTables();
			}
		});
		
		addFlight.addActionListener(e -> {
			if (Manager.getInstance().getAirports().size()<2) {
				JOptionPane.showMessageDialog(this, "You need at least 2 airports to add a flight.",
						"Not enough airport", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			AddFlight dialog = new AddFlight(parentFrame);
			dialog.setVisible(true);
			if(dialog.isAdded()) {
				refreshTables();
			}
		});
		addingPanel.add(addAirport);
		addingPanel.add(addFlight);
		
		
		JPanel tablesPanel = new JPanel(new GridLayout(2,1,5,5));
		
		String[] airportsColumns = {"Show","Code", "Name", "X", "Y"}; //Columns for airport table. Added extra Show columns for filtering on map.
		airportsModel = new DefaultTableModel(airportsColumns, 0){
			//to insert a checkbox into the first columns, we will use getColumnClass(). JTable has a row of objects as its rows content.
			//Since it often times does not know what type of Object it is, it simply calls the toString() method of the object. However, it also can 
			//connect Object types to existing JSwing classes, for example a type Boolean is interpreted as a JCheckbox. As such, if the first columns
			//data type is in fact boolean, it will simply return a checkbox in the columns in every row, and every other columns will be a string 
			// (else String.class). 
			@Override
			public Class<?> getColumnClass(int columnIndex) { 
				if (columnIndex == 0) {
					return Boolean.class;
				} return String.class;
			}
			//To make the checkbox editable, the program ensures only the first column is cell-editable.
			@Override
			public boolean isCellEditable(int row, int column) { //Make editing table cells impossible.
				return column == 0;
			}
		};
		airportsTable = new JTable(airportsModel); //New airport table
		airportsTable.getColumnModel().getColumn(2).setPreferredWidth(220); //Name column is a bit bigger in string size.
		
		JScrollPane airportsScroll = new JScrollPane(airportsTable); //Add small scroll for larger tables.
		airportsScroll.setBorder(new TitledBorder("Airports")); //Adding a border to the table.
		
		
	
		//The logic is entirely the same as the Airport table creation, minus some minute differences. 
		String[] flightColumns = {"From", "To", "Departure", "Duration"};
		flightsModel = new DefaultTableModel(flightColumns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		flightsTable = new JTable(flightsModel);
		JScrollPane flightsScroll = new JScrollPane(flightsTable);
		flightsScroll.setBorder(new TitledBorder("Letovi"));
		
		//Adding our created tables to the Tables panel
		tablesPanel.add(airportsScroll);
		tablesPanel.add(flightsScroll);
		
		add(addingPanel, BorderLayout.NORTH);
		add(tablesPanel, BorderLayout.CENTER);
		
		refreshTables(); //Method that fills the tables with the data received from file.
	}

	public void refreshTables() {
		airportsModel.setRowCount(0); //Reseting our tables.
		flightsModel.setRowCount(0);
		
		
		//Gathering airport data. addRow() adds an array of Objects, so the program makes an array with fields from the Airport class.
		ArrayList<Airport> airports = Manager.getInstance().getAirports();
		for(Airport a: airports) {
			Object[] row = {a.isVisible(),a.getCode(), a.getName(), a.getX(), a.getY()};
			airportsModel.addRow(row);
		}
		
		//Similar logic here for flights, except the From and To fields are Airport type objects, so to get their codes 
		//(which we want), the program uses valid getters for codes of both Airports.
		ArrayList<Flight> flights = Manager.getInstance().getFlights();
		for(Flight f: flights) {
			Object[] row = {
					f.getFrom().getCode(),
					f.getTo().getCode(),
					f.getDepartureString(),
					f.getDuration()
			};
			flightsModel.addRow(row);
		}
	}
	public DefaultTableModel getModel() { //get model for the airport table, so we can call Swing methods in MainFrame.
		return airportsModel;
	}
	
	public JTable getAirportTable() { //Getter for the airport table itself.
		return airportsTable;
	}


}
