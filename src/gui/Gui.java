package gui;

import javax.swing.*;
import java.awt.*;

public class Gui {
	
	public static void startGUI() {
		//Use a grid layout for the frame
		JFrame frame = new JFrame("Flight Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Adapt this size
		frame.setSize(1575,1050);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		frame.getContentPane().setLayout(new GridBagLayout());

		
		
		

		
		//Panel for the top part of the GUI
		JPanel flightInformationPanel = new JPanel();
		flightInformationPanel.setLayout(new GridBagLayout());
		
		//Panel containing the list of flights
		JPanel flightsPanel = new JPanel();
		JLabel flightsLabel = new JLabel("Flights");
		
				//REMOVE THESE VARIABLES
				String[] columnNames = {"First Name",
		                "Last Name",
		                "Sport",
		                "# of Years",
		                "Vegetarian"};
				
				Object[][] data = {
					    {"Kathy", "Smith",
					     "Snowboarding", new Integer(5), new Boolean(false)},
				};
		
		JTable flightsTable = new JTable(data, columnNames);
		JScrollPane flightsScrollPane = new JScrollPane(flightsTable);
				
		flightsPanel.setLayout(new BoxLayout(flightsPanel, BoxLayout.PAGE_AXIS));
		flightsPanel.add(flightsLabel);
		flightsPanel.add(flightsScrollPane);
		
		
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		flightInformationPanel.add(flightsPanel, gbc);
		
		
		//Panel containing the flight plan for the selected flight
		JPanel flightPlanPanel = new JPanel();
		JLabel flightPlanLabel = new JLabel("Flight Plan");
		
				//REMOVE THESE VARIABLES
				String[] columnNames2 = {"First Name"};
				
				Object[][] data2 = {
					    {"Kathy"},
				};
		

		JTable flightPlanTable = new JTable(data2, columnNames2);
		JScrollPane flightPlanScrollPane = new JScrollPane(flightPlanTable);
		flightPlanPanel.setLayout(new BoxLayout(flightPlanPanel, BoxLayout.PAGE_AXIS));
		flightPlanPanel.add(flightPlanLabel);
		flightPlanPanel.add(flightPlanScrollPane);
		
		gbc.gridwidth=1;
		gbc.gridx = 3;
		gbc.gridy = 0;
		flightInformationPanel.add(flightPlanPanel, gbc);
		
		
		//Panel containing the data for the selected flight
		JPanel flightDataPanel = new JPanel();
		flightDataPanel.setLayout(new GridBagLayout());
		
		
		
		JLabel distanceLabel = new JLabel("Distance");
		JLabel kmLabel = new JLabel("km");
		JLabel timeLabel = new JLabel("Time");
		JLabel fuelConsumptionLabel = new JLabel("Fuel Consumption");
		JLabel litreLabel = new JLabel("litre");
		JLabel coLabel = new JLabel("CO2");
		JLabel kgLabel = new JLabel("kg");

		JTextArea distanceTextArea = new JTextArea(1,5);
		JTextArea timeTextArea = new JTextArea(1,5);
		JTextArea fuelTextArea = new JTextArea(1,5);
		JTextArea coTextArea = new JTextArea(1,5);
		
		
		
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		flightDataPanel.add(distanceLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		flightDataPanel.add(distanceTextArea, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		flightDataPanel.add(kmLabel, gbc);
		gbc.gridx=0;
		gbc.gridy=2;
		flightDataPanel.add(timeLabel,gbc);
		gbc.gridx=0;
		gbc.gridy=3;
		flightDataPanel.add(timeTextArea, gbc);
		gbc.gridx=0;
		gbc.gridy=4;
		flightDataPanel.add(fuelConsumptionLabel, gbc);
		gbc.gridx=0;
		gbc.gridy=5;
		flightDataPanel.add(fuelTextArea, gbc);
		gbc.gridx=1;
		gbc.gridy=5;
		flightDataPanel.add(litreLabel, gbc);
		gbc.gridx=0;
		gbc.gridy=6;
		flightDataPanel.add(coLabel, gbc);
		gbc.gridx=0;
		gbc.gridy=7;
		flightDataPanel.add(coTextArea, gbc);
		gbc.gridx=1;
		gbc.gridy=7;
		flightDataPanel.add(kgLabel, gbc);
		
		
		gbc.gridx = 4;
		gbc.gridy=0;
		flightInformationPanel.add(flightDataPanel, gbc);
				
		
		//Creating the bottom part of the GUI
		JPanel addFlightPanel = new JPanel();
		addFlightPanel.setLayout(new GridBagLayout());
		
		
		//Creating the table for adding the flight information:
		JPanel addFlightInformationPanel = new JPanel();
		
		JLabel addFlightLabel = new JLabel("Add Flight");
		
		//REMOVE THESE VARIABLES
		String[] addFlightColumnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
		
		Object[][] addFlightData = {
			    {"Kathy", "Smith",
			     "Snowboarding", new Integer(5), new Boolean(false)},
		};

		JTable addFlightsTable = new JTable(addFlightData, addFlightColumnNames);
		JScrollPane addFlightsScrollPane = new JScrollPane(addFlightsTable);
		addFlightsScrollPane.setPreferredSize(new Dimension (600, 40));
				
		addFlightInformationPanel.setLayout(new BoxLayout(addFlightInformationPanel, BoxLayout.PAGE_AXIS));
		addFlightInformationPanel.add(addFlightLabel);
		addFlightInformationPanel.add(addFlightsScrollPane);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		addFlightPanel.add(addFlightInformationPanel, gbc);	
		
		
		//Creating the table for adding the flight plan:
		JPanel addFlightPlanPanel = new JPanel();
		
		JLabel addFlightPlanLabel = new JLabel("Flight Plan");
		
		//REMOVE THESE VARIABLES
		String[] addFlightPlanColumnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
		
		Object[][] addFlightPlanData = {
			    {"Kathy", "Smith",
			     "Snowboarding", new Integer(5), new Boolean(false)},
		};

		JTable addFlightsPlanTable = new JTable(addFlightPlanData, addFlightPlanColumnNames);
		JScrollPane addFlightsPlanScrollPane = new JScrollPane(addFlightsPlanTable);
		addFlightsPlanScrollPane.setPreferredSize(new Dimension (600, 40));
				
		addFlightPlanPanel.setLayout(new BoxLayout(addFlightPlanPanel, BoxLayout.PAGE_AXIS));
		addFlightPlanPanel.add(addFlightPlanLabel);
		addFlightPlanPanel.add(addFlightsPlanScrollPane);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		addFlightPanel.add(addFlightPlanPanel, gbc);		
		
		
		JButton buttonAdd = new JButton("Add");
		JButton buttonCancel = new JButton("Cancel");
		JButton buttonExit = new JButton("Exit");
		gbc.gridx = 0;
		gbc.gridy = 2;
		addFlightPanel.add(buttonAdd, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		addFlightPanel.add(buttonCancel, gbc);
		gbc.gridx = 2;
		gbc.gridy = 2;
		addFlightPanel.add(buttonExit, gbc);
		
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		frame.getContentPane().add(flightInformationPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		frame.getContentPane().add(addFlightPanel, gbc);
		
		frame.setVisible(true);
	}
}
