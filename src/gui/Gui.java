package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionEvent;
import java.util.*;
import io.FileManager;
import flightressources.*;

public class Gui {
	
	public static void startGUI() {
		//Use a grid layout for the frame
		JFrame frame = new JFrame("Flight Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Adapt this size
		frame.setSize(1400,900);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		frame.getContentPane().setLayout(new GridBagLayout());

		
		
		

		
		//Panel for the top part of the GUI
		JPanel flightInformationPanel = new JPanel();
		flightInformationPanel.setLayout(new GridBagLayout());
		
		HashMap<String, Flight> flightsHM = FileManager.loadFlights();
		HashMap<String, Airport> airportsHM = FileManager.loadAirports();
		HashMap<String, Airline> airlinesHM = FileManager.loadAirlines();
		HashMap<String, Aeroplane> planesHM = FileManager.loadAeroplanes();
		
		//Panel containing the list of flights
		JPanel flightsPanel = new JPanel();
		JLabel flightsLabel = new JLabel("Flights");
		
				//REMOVE THESE VARIABLES
				String[] columnNames = {"Flight",
		                "Plane",
		                "Departure",
		                "Destination",
		                "Date",
		                "Time"};
				
				int numberOfFlights = flightsHM.keySet().size();

				
				Object[][] data = new Object[numberOfFlights][6];
				
		int i=0;		
		for(String key: flightsHM.keySet()) {
			data[i][0] = flightsHM.get(key).getIdentifier();
			data[i][1] = flightsHM.get(key).getPlane().getModel();
			data[i][2] = flightsHM.get(key).getDepartureAirport().getCode();
			data[i][3] = flightsHM.get(key).getDestinationAirport().getCode();
			data[i][4] = flightsHM.get(key).getDepartureDateTime().toString().split("T")[0].replace("-", ":");
			data[i][5] = flightsHM.get(key).getDepartureDateTime().toString().split("T")[1];
			i++;
		}
		
		JTable flightsTable = new JTable(new DefaultTableModel(data, columnNames));
		flightsTable.getTableHeader().setReorderingAllowed(false);
		flightsTable.setDefaultEditor(Object.class, null);
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
				String[] columnNames2 = {""};
				
				Object[][] data2 = new Object[20][1];
		

		JTable flightPlanTable = new JTable(data2, columnNames2);
		flightPlanTable.getTableHeader().setReorderingAllowed(false);
		flightPlanTable.setEnabled(false);
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
		distanceTextArea.setEditable(false);
		JTextArea timeTextArea = new JTextArea(1,5);
		timeTextArea.setEditable(false);
		JTextArea fuelTextArea = new JTextArea(1,5);
		fuelTextArea.setEditable(false);
		JTextArea coTextArea = new JTextArea(1,5);
		coTextArea.setEditable(false);
		
		
		
		
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
		
		
		//Setting up the flightPlanTable when a row of the flightTable is selected
		//This is most likely a problem, there is probably a better solution, but I don't see it for the moment
		flightsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if(!event.getValueIsAdjusting()) {
					String flightCode = (String) flightsTable.getValueAt(flightsTable.getSelectedRow(), 0);
					
					
					
					//Updates flightPlanTable to display the flight plan of the selected flight
					LinkedList<ControlTower> flightPlanControlTowers = flightsHM.get(flightCode).getFlightPlan().getControlTowers();
					int browseTable = 0;
					
					for(ControlTower controlTower: flightPlanControlTowers) {
						for(Airport airport: airportsHM.values()) {
							if(controlTower.compareTo(airport.getControlTower())) {
								data2[browseTable][0] = airport.getCode();
								browseTable++;
							}
						}
					}
					for(int browseData2= browseTable; browseData2<20;browseData2++) {
						Arrays.fill(data2[browseData2], null);
					}
					flightPlanTable.repaint();
					
					
					//Updates the different text area for the flight information
					try {
						String distance = flightsHM.get(flightCode).distanceCovered().toString();
						distanceTextArea.setText(distance);
						String time = flightsHM.get(flightCode).timeTaken().toString();
						timeTextArea.setText(time);
						String fuel = flightsHM.get(flightCode).fuelConsumption().toString();
						fuelTextArea.setText(fuel);
						String co2 = flightsHM.get(flightCode).CO2Emission().toString();
						coTextArea.setText(co2);
					}
					catch (Exception e) {		
					}
					
				}
			}
		});
				
		
		//Creating the bottom part of the GUI
		JPanel addFlightPanel = new JPanel();
		addFlightPanel.setLayout(new GridBagLayout());
		
		
		//Creating the table for adding the flight information:
		JPanel addFlightInformationPanel = new JPanel();
		
		JLabel addFlightLabel = new JLabel("Add Flight");
		
		//REMOVE THESE VARIABLES
		String[] addFlightColumnNames = {"Airline",
                "Number",
                "Plane",
                "Departure",
                "Destination",
                "Date",
                "Time"};
		
		Object[][] addFlightData = new Object[1][7];

		JTable addFlightsTable = new JTable(new DefaultTableModel(addFlightData, addFlightColumnNames));
		addFlightsTable.getTableHeader().setReorderingAllowed(false);
		
			TableColumn airlineColumn = addFlightsTable.getColumnModel().getColumn(0);
			JComboBox<Airline> airlines = new JComboBox<Airline>();
			
			for(Airline airline: airlinesHM.values()) {
				airlines.addItem(airline);
			}
			airlineColumn.setCellEditor(new DefaultCellEditor(airlines));
			
			
			TableColumn planeColumn = addFlightsTable.getColumnModel().getColumn(2);
			JComboBox<Aeroplane> planes = new JComboBox<Aeroplane>();
			
			for(Aeroplane plane: planesHM.values()) {
				planes.addItem(plane);
			}
			planeColumn.setCellEditor(new DefaultCellEditor(planes));
			
			TableColumn depAirportColumn = addFlightsTable.getColumnModel().getColumn(3);
			JComboBox<Airport> airports = new JComboBox<Airport>();
			
			for(Airport airport: airportsHM.values()) {
				airports.addItem(airport);
			}
			depAirportColumn.setCellEditor(new DefaultCellEditor(airports));
			
			TableColumn destAirportColumn = addFlightsTable.getColumnModel().getColumn(4);
			destAirportColumn.setCellEditor(new DefaultCellEditor(airports));
		
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
		String[] addFlightPlanColumnNames = new String[20];
		for(int j=0; j<20; j++) {
			addFlightPlanColumnNames[j] = "";
		}
		
		Object[][] addFlightPlanData = new Object[1][20];

		JTable addFlightsPlanTable = new JTable(new DefaultTableModel(addFlightPlanData, addFlightPlanColumnNames));
		addFlightsPlanTable.getTableHeader().setReorderingAllowed(false);
		
		for(int j=0; j<addFlightsPlanTable.getColumnCount(); j++) {
			TableColumn airportColumn = addFlightsPlanTable.getColumnModel().getColumn(j);
			
			airportColumn.setCellEditor(new DefaultCellEditor(airports));
		}
		
		JScrollPane addFlightsPlanScrollPane = new JScrollPane(addFlightsPlanTable);
		addFlightsPlanScrollPane.setPreferredSize(new Dimension (1000, 40));
				
		addFlightPlanPanel.setLayout(new BoxLayout(addFlightPlanPanel, BoxLayout.PAGE_AXIS));
		addFlightPlanPanel.add(addFlightPlanLabel);
		addFlightPlanPanel.add(addFlightsPlanScrollPane);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		addFlightPanel.add(addFlightPlanPanel, gbc);		
		
		
		JButton buttonAdd = new JButton("Add");
		buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LinkedList<ControlTower> flightPlan = new LinkedList<ControlTower>();
				for(int i=0; i<20; i++) {
					if(addFlightsPlanTable.getValueAt(0,i) != null)
					flightPlan.add(((Airport) addFlightsPlanTable.getValueAt(0, i)).getControlTower());
				}
				
				try {
					((Airline) addFlightsTable.getValueAt(0, 0)).getCode();
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");
					LocalDateTime dateTime = LocalDateTime.parse(addFlightsTable.getValueAt(0, 5) + " " + addFlightsTable.getValueAt(0, 6), formatter);
					
					Flight newFlightToAdd = new Flight(((Airline) addFlightsTable.getValueAt(0, 0)).getCode() + addFlightsTable.getValueAt(0,1),
													   (Aeroplane) addFlightsTable.getValueAt(0, 2),
													   (Airport) addFlightsTable.getValueAt(0, 3),
													   (Airport) addFlightsTable.getValueAt(0, 4),
													   dateTime,
													   new FlightPlan(flightPlan));
					
					if(flightsHM.get(newFlightToAdd.getIdentifier()) == null){
						
						flightsHM.put(newFlightToAdd.getIdentifier(), newFlightToAdd);
						
						Object [] newData = new Object[6];
						newData[0] = newFlightToAdd.getIdentifier();
						newData[1] = flightsHM.get(newFlightToAdd.getIdentifier()).getPlane().getModel();
						newData[2] = flightsHM.get(newFlightToAdd.getIdentifier()).getDepartureAirport().getCode();
						newData[3] = flightsHM.get(newFlightToAdd.getIdentifier()).getDestinationAirport().getCode();
						newData[4] = flightsHM.get(newFlightToAdd.getIdentifier()).getDepartureDateTime().toString();
						newData[5] = flightsHM.get(newFlightToAdd.getIdentifier()).getDepartureDateTime().toString();
						((DefaultTableModel) flightsTable.getModel()).addRow(newData);
						flightsTable.repaint();
						
						FileManager.saveFlights(flightsHM);
					}else {
						JOptionPane.showMessageDialog(frame, "This flight identifier already exist, please change the airline or code.", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}catch (NullPointerException npe) {
					JOptionPane.showMessageDialog(frame, "At least one element of the array is empty.Please check the flight details.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				catch(DateTimeParseException dtpe) {
					JOptionPane.showMessageDialog(frame, "The time or date written aren't valid. Please make sure they match the following format:\n"
							+ 								"Date: MM:dd:yyyy\n"
							+ 								"Time: HH:mm", "Warning", JOptionPane.WARNING_MESSAGE);
				}
				
				
			}
		});
		
		JButton buttonExit = new JButton("Exit");
		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileManager.generateReport(flightsHM);
				System.exit(0);
			}
		});
		
		JButton buttonClear = new JButton("Clear");
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] newAddFlightData = new Object[7];
				((DefaultTableModel) addFlightsTable.getModel()).setRowCount(0);
				((DefaultTableModel) addFlightsTable.getModel()).addRow(newAddFlightData);
				
				Object[] newAddFlightPlanData = new Object[20];
				((DefaultTableModel) addFlightsPlanTable.getModel()).setRowCount(0);
				((DefaultTableModel) addFlightsPlanTable.getModel()).addRow(newAddFlightPlanData);
				
				addFlightsTable.repaint();
				addFlightsPlanTable.repaint();
			}
		});

		
		gbc.gridx = 0;
		gbc.gridy = 2;
		addFlightPanel.add(buttonAdd, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		addFlightPanel.add(buttonClear, gbc);
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
		
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				FileManager.generateReport(flightsHM);
				System.exit(0);
			}
		});
		
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		startGUI();
	}
}
