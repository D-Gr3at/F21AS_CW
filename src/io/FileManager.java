package io;

import java.util.HashMap;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.LinkedList;

import flightressources.Airport;
import flightressources.ControlTower;
import flightressources.Aeroplane;
import flightressources.Airline;
import flightressources.Flight;
import flightressources.FlightPlan;

public class FileManager {
	
	//Method used to retrieve the list of Airports in the Airport.txt file
	public static HashMap<String, Airport> loadAirports() {		
		
		HashMap<String, Airport> airports = new HashMap<String, Airport>();
		
		try {
			FileReader fileReader = new FileReader("Airports.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String airport = br.readLine();
			
			while(airport != null) {
				String[] airportSplit = airport.split("; ");
				airports.put(airportSplit[0],
							new Airport(airportSplit[1], 
										airportSplit[0], 
										new ControlTower(airportSplit[3], airportSplit[2])));
				airport = br.readLine();
			}
			
			fileReader.close();
			br.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return airports;
	}
	
	//Method used to retrieve the list of Planes in the Planes.txt file
	public static HashMap<String, Aeroplane> loadAeroplanes() {		
		
		HashMap<String, Aeroplane> airplanes = new HashMap<String, Aeroplane>();
		
		try {
			FileReader fileReader = new FileReader("Planes.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String airplane = br.readLine();
			
			while(airplane != null) {
				String[] airplaneSplit = airplane.split("; ");

				airplanes.put(airplaneSplit[0],
							  new Aeroplane(airplaneSplit[0],
											Double.parseDouble(airplaneSplit[2]),
											airplaneSplit[1],
											Double.parseDouble(airplaneSplit[3])));
				airplane = br.readLine();				
			}
			
			fileReader.close();
			br.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return airplanes;
	}

	//Method used to retrieve the list of Airlines in the Airlines.txt file
	public static HashMap<String, Airline> loadAirlines() {		
		
		HashMap<String, Airline> airlines = new HashMap<String, Airline>();
		
		try {
			FileReader fileReader = new FileReader("Airlines.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String airline = br.readLine();
			
			while(airline != null) {
				String[] airlineSplit = airline.split("; ");

				airlines.put(airlineSplit[0], 
							 new Airline(airlineSplit[1],
									 	 airlineSplit[0]));
				
				airline = br.readLine();
			}
			
			fileReader.close();
			br.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return airlines;
	}
	
	//Method used to retrieve the list of Flight in the Flights.txt file
	public static HashMap<String, Flight> loadFlights() {		
		
		HashMap<String, Flight> flights = new HashMap<String, Flight>();
		
		HashMap<String, Aeroplane> aeroplanes = loadAeroplanes();
		HashMap<String, Airport> airports = loadAirports();
		
		try {
			FileReader fileReader = new FileReader("Flights.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String flight = br.readLine();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");

			while(flight != null) {
				String[] flightSplit = flight.split("; ");				
				
				LocalDateTime dateTime = LocalDateTime.parse(flightSplit[4] + " " + flightSplit[5], formatter);
				
				LinkedList<ControlTower> controlTowers = new LinkedList<ControlTower>();
				for(int i=6; i<flightSplit.length; i++) {
					controlTowers.add(airports.get(flightSplit[i]).getControlTower());
				}
				


				flights.put(flightSplit[0], 
							new Flight(flightSplit[0],
									   aeroplanes.get(flightSplit[1]),
									   airports.get(flightSplit[2]),
									   airports.get(flightSplit[3]),
									   dateTime,
									   new FlightPlan(controlTowers)
								   ));
								
				flight = br.readLine();
			}
			
			fileReader.close();
			br.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return flights;
	}
	
	//Method used to save the flight
	public static void saveFlights(HashMap<String, Flight> flightHM) {
		try {
			FileWriter fw = new FileWriter("Flights.txt");
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy; HH:mm");
			
			HashMap<String, Airport> airports = loadAirports();


			//fw.write(System.lineSeparator());
			flightHM.forEach((code, flight) -> {
				try{
					String flightToSave = code + "; "
							 			  + flight.getPlane().getModel() + "; "
							 			  + flight.getDepartureAirport().getCode() + "; "
							 			  + flight.getDestinationAirport().getCode() + "; "
							 			  + flight.getDepartureDateTime().format(formatter) + "; ";
					
					
					for(ControlTower controlTower: flight.getFlightPlan().getControlTowers()) {
						for(Airport airport: airports.values()) {
							if(controlTower.compareTo(airport.getControlTower())) {
								flightToSave = flightToSave + airport.getCode() + "; ";
							}
						}
					}
					
					flightToSave = flightToSave.trim();
					if(flightToSave.endsWith(";")) {
						flightToSave = flightToSave.substring(0, flightToSave.length()-1);
					}
					
					fw.write(flightToSave
							 + System.lineSeparator());
				} 
				catch(Exception e) {
					
				}
			});
			
			fw.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	//Write report of all flights
	//Replace all Integers things by the Flight Class
	//Need testing once we have all the other classes
	public static void generateReport(HashMap<String, Flight> flightHM) {
		
		/**Structure of the file:
			Company1Code:
				- Number of flights:X
				- Total number of kilometers:X
				- Average fuel consumption: X
				- Average CO2 emission: X
			Company2Code:
			...
		**/
		
		
		//Copy the flightList in a separate HashMap in order to modify it later without impact flightList
		HashMap<String, Flight> tempFlightList = new HashMap<String, Flight>();
		flightHM.forEach((code, flight) -> {
			tempFlightList.put(code, flight);
		});
			
		
		//HashMap<String, Aeroplane> aeroplanes = loadAeroplanes();
		//HashMap<String, Airport> airports = loadAirports();
		HashMap<String, Airline> airlines = loadAirlines();

		
		
		try {
			FileWriter fw = new FileWriter("Report.txt");
						
			//get first element's company code
			
			while(tempFlightList.size() != 0) {
				String airlineCode = ((String) tempFlightList.keySet().toArray()[0]).substring(0,2); //REMOVE the serial number
				fw.write(airlines.get(airlineCode).getName() + System.lineSeparator());


				/**Concerning this array:
					-0 is the number of flight
					-1 is the total number of kilometers
					-2 is the average fuel consumption
					-3 is the average CO2 emission 
				**/
				double[] companyInformation = new double[4];
				tempFlightList.forEach((code, flight) -> {
					if(flight.getIdentifier().contains(airlineCode)) {
						companyInformation[0] += 1.0;
						try {
							companyInformation[1] += flight.distanceCovered();
							companyInformation[2] += flight.fuelConsumption();
							companyInformation[3] += flight.CO2Emission();
						}
						catch (Exception E) {
							System.err.println(E.getMessage());
						}
					}						
				});
				
				fw.write("	Number of flights: " + companyInformation[0] + System.lineSeparator());
				fw.write("	Total number of kilometers: " + companyInformation[1] + System.lineSeparator());
				fw.write("	Average Fuel consumption: " + companyInformation[2] + System.lineSeparator());
				fw.write("	Average CO2 emission: " + companyInformation[3] + System.lineSeparator());
				tempFlightList.entrySet().removeIf(entry -> entry.getKey().contains(airlineCode));
			}
			
			fw.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}

	}
}
