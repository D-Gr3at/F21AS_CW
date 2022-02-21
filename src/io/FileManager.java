package io;

import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

public class FileManager {
	
	//Useless?
	public static LinkedList<String> getFlightPlan() {
		
		return new LinkedList<String>();
	}
	
	
	//Replace all integer thing by an airport class
	public static HashMap<String, Integer> loadAirports() {		
		
		HashMap<String, Integer> airports = new HashMap<String, Integer>();
		
		try {
			FileReader fileReader = new FileReader("Airports.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String airport = br.readLine();
			
			int i=0;
			
			while(airport != null) {
				String[] airportSplit = airport.split("; ");
				airports.put(airportSplit[0], i);
				i++;
				//System.out.println(airportSplit[0]);
				//Add a for loop in order to add everything into an "Airport" object
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
	
	//Replace all integer thing by a Flight class
	public static HashMap<String, Integer> loadFlights() {		
		
		HashMap<String, Integer> flights = new HashMap<String, Integer>();
		
		try {
			FileReader fileReader = new FileReader("Flights.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String flight = br.readLine();
			int i=0;
			
			while(flight != null) {
				String[] flightSplit = flight.split("; ");

				flights.put(flightSplit[0], i);
				i++;
				
				System.out.println(flightSplit[0]);
				//Add a for loop in order to add everything into a "Flight" object
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

	//Replace all integer thing by an Airplane class
	public static HashMap<String, Integer> loadAirplanes() {		
		
		HashMap<String, Integer> airplanes = new HashMap<String, Integer>();
		
		try {
			FileReader fileReader = new FileReader("Airplanes.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String airplane = br.readLine();
			int i=0;
			
			while(airplane != null) {
				String[] airplaneSplit = airplane.split("; ");

				airplanes.put(airplaneSplit[0], i);
				i++;
				
				System.out.println(airplaneSplit[0]);
				//Add a for loop in order to add everything into an "Airplanes" object
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

	//Replace all integer thing by an Airline class
	public static HashMap<String, Integer> loadAirlines() {		
		
		HashMap<String, Integer> airlines = new HashMap<String, Integer>();
		
		try {
			FileReader fileReader = new FileReader("Airlines.txt");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String airline = br.readLine();
			int i=0;
			
			while(airline != null) {
				String[] airlineSplit = airline.split("; ");

				airlines.put(airlineSplit[0], i);
				i++;
				
				System.out.println(airlineSplit[0]);
				//Add a for loop in order to add everything into an "Airlines" object
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
	
	//Replace all integer things by the Flight Class
	public static void saveFlights(HashMap<String, Integer> flightList) {
		System.out.println("List with change: " + flightList.toString());
				
		try {
			FileWriter fw = new FileWriter("Flights.txt");
			
			//Have to do it this way, otherwise there is an error
			int[] sizeFlightList = new int[1];
			sizeFlightList[0] = flightList.size();
			
			flightList.forEach((code, integ) -> {
				try{
					sizeFlightList[0]--;
					if(sizeFlightList[0] !=0) {
						fw.write(code + System.lineSeparator());
					} else {
						fw.write(code);
					}
				} 
				catch(Exception e) {
					
				}
			});
			
			fw.close();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
				
		System.out.println("New list from file: " + FileManager.loadAirports());

	}
}
