package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.Airport;
import logic.Manager;

//The frame the program will be adding elements to in the user interface.
public class MainFrame extends JFrame{
	
	private Tables tablesPanel;
	private MapPanel map;
	private Timeout timeout;
	
	//Constructor: Window title, size, position and click on "x" event, alongside instantiating other panels it contains.
	public MainFrame() {
		setTitle("Simulation: Flights and Airports");
		setSize(1600, 1200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		timeout = new Timeout(); //Inactivity timer
		timeout.start();
		
		tablesPanel = new Tables(); //New table object
		map = new MapPanel(); //New map 
		map.setTimeout(timeout); //Setting the timeout field in map.
		
		//Size of panels.
		map.setPreferredSize(new Dimension(650,600));
		tablesPanel.setPreferredSize(new Dimension(450,600));
		
		//Positioning of panels.
		add(tablesPanel, BorderLayout.EAST); //Positioning our table on the right edge of the app.
		add(map, BorderLayout.CENTER);
		
		setupTableListener();
		paintAirports();
		
		setupMenuBar(); //Method that creates a Menu Bar.
		
		
	}
	//Method that allows users to filter shown airports on map via checkbox.
	private void setupTableListener() {
		tablesPanel.getModel().addTableModelListener(e -> {
			int row = e.getFirstRow();
			int col = e.getColumn();
			
			if(col == 0 && row >= 0 ) {
				ArrayList<Airport> airports = Manager.getInstance().getAirports();
				if (row < airports.size()) {
					Boolean isChecked = (Boolean) tablesPanel.getModel().getValueAt(row, 0);
					airports.get(row).setVisible(isChecked);
					
					map.repaint();
				}
			}
		});
	}
	//Method to paint the airports on the MainFrame.
	private void paintAirports() {
		ArrayList<Airport> airports =  Manager.getInstance().getAirports();
		
		tablesPanel.refreshTables();
		map.setAirports(airports);
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		
		//Three options in "File" menu: Loading, Saving data and exiting the app.
		JMenuItem loadData = new JMenuItem("Load JSON/CSV file..");
		JMenuItem saveData = new JMenuItem("Save in JSON/CSV file..");
		JMenuItem exit = new JMenuItem("Exit");
		
		loadData.addActionListener(e -> { // Lambda function to add an event to the button.
			JFileChooser chooser = new JFileChooser(); //File chooser.
			chooser.setDialogTitle("Select JSON or CSV file."); //Popup title.
			chooser.setFileFilter(new FileNameExtensionFilter("JSON/CSV", "json","csv")); //Filtering which files are allowed.
			
			int selection = chooser.showOpenDialog(this);  //Opens dialogue.
			if (selection == JFileChooser.APPROVE_OPTION) { //If the user  selected a correct file and approved
				File selectedFile = chooser.getSelectedFile(); //Then the program has the file, and can now load data from it.
			
			try {
				Manager.getInstance().loadData(selectedFile.getAbsolutePath()); //Calling our singleton to get the parsing method.
				paintAirports(); //This method will both refresh the table and paint the airports on the map.
				
				JOptionPane.showMessageDialog(this, "Data successfully loaded from file: " + selectedFile.getName(),
						"Successfully loaded.", JOptionPane.INFORMATION_MESSAGE); //Information: successful load.
			} catch(Exception ex) {
				
				JOptionPane.showMessageDialog(this, "Error while loading: " + ex.getMessage(), //Unsuccessful load.
						"Loading Failed", JOptionPane.ERROR_MESSAGE);
			}
			}
		});
		
		saveData.addActionListener(e -> { //Lambda function for adding event to the button.
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save data in JSON/CSV file..");
			chooser.setFileFilter(new FileNameExtensionFilter("JSON/CSV", "json","csv")); //The rest of the code is the same as loading data, minus the 
																						// the method called by the Manager and the information messages.
			
			int selection = chooser.showSaveDialog(this);
			if(selection == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				try {
					Manager.getInstance().writeData(selectedFile.getAbsolutePath(), Manager.getInstance().getAirports(), 
							Manager.getInstance().getFlights());
					JOptionPane.showMessageDialog(this,"Successfully saved in file." + selectedFile.getName(), 
							"Successfully saved.", JOptionPane.INFORMATION_MESSAGE);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Error while saving: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		//Exit the program when clicked.
		exit.addActionListener(e -> System.exit(0));
		
		//Adding our items to our menu.
		menu.add(loadData);
		menu.add(saveData);
		menu.addSeparator();
		menu.add(exit);
		
		//Adding "File" menu to the Bar.
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}
	
	//Main method.
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{ //JSwing, unlike AWT, is not thread-friendly. To ensure it is, invokeLater() is called.
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
		});
	}
}
