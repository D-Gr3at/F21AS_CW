package io;

import flightressources.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileManager {

	public List<Flight> getDefaultFlights() throws IOException {
		List<Flight> flights = new ArrayList<>();
		String str;
		FileReader fileReader = new FileReader("Flights.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((str = bufferedReader.readLine()) != null) {
			String[] line = str.split("; ");
			Flight flight = new Flight();
			flight.setIdentifier(line[0]);
			List<Aeroplane> aeroplanes = loadAeroplanes();
			Optional<Aeroplane> optionalAeroplane = aeroplanes.stream()
					.filter(aeroplane -> aeroplane.getModel() != null)
					.filter(aeroplane -> aeroplane.getModel().trim().equalsIgnoreCase(line[1]))
					.findFirst();
			if (optionalAeroplane.isPresent()) {
				Aeroplane aeroplane = optionalAeroplane.get();
				flight.setPlane(aeroplane);
			}
			List<Airport> airports = loadAirports();
			Optional<Airport> optionalAirport = airports.stream()
					.filter(airport -> airport.getCode() != null)
					.filter(airport -> airport.getCode().equalsIgnoreCase(line[2]))
					.findFirst();
			if (optionalAirport.isPresent()){
				Airport airport = optionalAirport.get();
				flight.setDepartureAirport(airport);
			}

			Optional<Airport> airportOptional = airports.stream()
					.filter(airport -> airport.getCode() != null)
					.filter(airport -> airport.getCode().equalsIgnoreCase(line[3]))
					.findFirst();
			if (airportOptional.isPresent()){
				Airport airport = airportOptional.get();
				flight.setDestinationAirport(airport);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");
			LocalDateTime localDateTime = LocalDateTime
					.parse(line[4]+" "+line[5], formatter)
					.atZone(ZoneId.of("CET")).toLocalDateTime();
			flight.setDepartureDateTime(localDateTime);
			List<String> airportList = new ArrayList<>();
			for (int i = 6; i < line.length; i++){
				if (line[i] != null){
					airportList.add(line[i]);
				}
			}
			List<Airport> airports1 = airports.stream()
					.filter(airport -> airport.getCode() != null)
					.filter(airport -> airportList.contains(airport.getCode()))
					.collect(Collectors.toList());
			FlightPlan flightPlan = new FlightPlan(new LinkedList<>(airports1));
			flight.setFlightPlan(flightPlan);
			flights.add(flight);
		}
		return flights;
	}

	public List<Aeroplane> loadAeroplanes() throws IOException {
		List<Aeroplane> aeroplanes = new ArrayList<>();
		String str;
		FileReader fileReader = new FileReader("Planes.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((str = bufferedReader.readLine()) != null) {
			String[] line = str.split("; ");
			Aeroplane aeroplane = new Aeroplane();
			aeroplane.setModel(line[0]);
			aeroplane.setManufacturer(line[1]);
			aeroplane.setSpeed(Double.parseDouble(line[2]));
			aeroplane.setFuelConsumption(Double.parseDouble(line[3]));
			aeroplanes.add(aeroplane);
		}
		return aeroplanes;
	}

	public List<Airport> loadAirports() throws IOException {
		List<Airport> airports = new ArrayList<>();
		String str;
		FileReader fileReader = new FileReader("Airports.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((str = bufferedReader.readLine()) != null) {
			String[] line = str.split("; ");
			Airport airport = new Airport();
			airport.setCode(line[0]);
			airport.setName(line[1]);
			airport.setControlTower(new ControlTower(new GPSCoordinate(line[3], line[2])));
			airports.add(airport);
		}
		return airports;
	}

	public List<Airline> loadAirlines() throws IOException {
		List<Airline> airlines = new ArrayList<>();
		String str;
		FileReader fileReader = new FileReader("Airlines.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((str = bufferedReader.readLine()) != null) {
			String[] line = str.split("; ");
			airlines.add(new Airline(line[1], line[0]));
		}
		return airlines;
	}
	
	//Method used to retrieve the list of Airports in the Airport.txt file
//	public static HashMap<String, Airport> loadAirports() {
//
//		HashMap<String, Airport> airports = new HashMap<String, Airport>();
//
//		try {
//			FileReader fileReader = new FileReader("Airports.txt");
//
//			BufferedReader br = new BufferedReader(fileReader);
//
//			String airport = br.readLine();
//
//			while(airport != null) {
//				String[] airportSplit = airport.split("; ");
//				airports.put(airportSplit[0],
//							new Airport(airportSplit[1],
//										airportSplit[0],
//										new ControlTower(airportSplit[3], airportSplit[2])));
//				airport = br.readLine();
//			}
//
//			fileReader.close();
//			br.close();
//		}
//		catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//
//		return airports;
//	}
	
	//Method used to retrieve the list of Planes in the Planes.txt file
//	public static HashMap<String, Aeroplane> loadAeroplanes() {
//
//		HashMap<String, Aeroplane> airplanes = new HashMap<String, Aeroplane>();
//
//		try {
//			FileReader fileReader = new FileReader("Planes.txt");
//
//			BufferedReader br = new BufferedReader(fileReader);
//
//			String airplane = br.readLine();
//
//			while(airplane != null) {
//				String[] airplaneSplit = airplane.split("; ");
//
//				airplanes.put(airplaneSplit[0],
//							  new Aeroplane(airplaneSplit[0],
//											Double.parseDouble(airplaneSplit[2]),
//											airplaneSplit[1],
//											Double.parseDouble(airplaneSplit[3])));
//				airplane = br.readLine();
//			}
//
//			fileReader.close();
//			br.close();
//		}
//		catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//
//		return airplanes;
//	}

	//Method used to retrieve the list of Airlines in the Airlines.txt file
//	public static HashMap<String, Airline> loadAirlines() {
//
//		HashMap<String, Airline> airlines = new HashMap<String, Airline>();
//
//		try {
//			FileReader fileReader = new FileReader("Airlines.txt");
//
//			BufferedReader br = new BufferedReader(fileReader);
//
//			String airline = br.readLine();
//
//			while(airline != null) {
//				String[] airlineSplit = airline.split("; ");
//
//				airlines.put(airlineSplit[0],
//							 new Airline(airlineSplit[1],
//									 	 airlineSplit[0]));
//
//				airline = br.readLine();
//			}
//
//			fileReader.close();
//			br.close();
//		}
//		catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//
//		return airlines;
//	}
	
	//Method used to retrieve the list of Flight in the Flights.txt file
//	public static HashMap<String, Flight> loadFlights() {
//
//		HashMap<String, Flight> flights = new HashMap<String, Flight>();
//
//		HashMap<String, Aeroplane> aeroplanes = loadAeroplanes();
//		HashMap<String, Airport> airports = loadAirports();
//
//		try {
//			FileReader fileReader = new FileReader("Flights.txt");
//
//			BufferedReader br = new BufferedReader(fileReader);
//
//			String flight = br.readLine();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");
//
//			while(flight != null) {
//				String[] flightSplit = flight.split("; ");
//
//				LocalDateTime dateTime = LocalDateTime.parse(flightSplit[4] + " " + flightSplit[5], formatter);
//
//				LinkedList<ControlTower> controlTowers = new LinkedList<ControlTower>();
//				for(int i=6; i<flightSplit.length; i++) {
//					controlTowers.add(airports.get(flightSplit[i]).getControlTower());
//				}
//
//
//
//				flights.put(flightSplit[0],
//							new Flight(flightSplit[0],
//									   aeroplanes.get(flightSplit[1]),
//									   airports.get(flightSplit[2]),
//									   airports.get(flightSplit[3]),
//									   dateTime,
//									   new FlightPlan(controlTowers)
//								   ));
//
//				flight = br.readLine();
//			}
//
//			fileReader.close();
//			br.close();
//		}
//		catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//
//		return flights;
//	}
	
	//Method used to save the flight
//	public static void saveFlights(HashMap<String, Flight> flightHM) {
//		try {
//			FileWriter fw = new FileWriter("Flights.txt");
//
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy; HH:mm");
//
//			HashMap<String, Airport> airports = loadAirports();
//
//
//			//fw.write(System.lineSeparator());
//			flightHM.forEach((code, flight) -> {
//				try{
//					String flightToSave = code + "; "
//							 			  + flight.getPlane().getModel() + "; "
//							 			  + flight.getDepartureAirport().getCode() + "; "
//							 			  + flight.getDestinationAirport().getCode() + "; "
//							 			  + flight.getDepartureDateTime().format(formatter) + "; ";
//
//
//					for(ControlTower controlTower: flight.getFlightPlan().getControlTowers()) {
//						for(Airport airport: airports.values()) {
//							if(controlTower.compareTo(airport.getControlTower())) {
//								flightToSave = flightToSave + airport.getCode() + "; ";
//							}
//						}
//					}
//
//					flightToSave = flightToSave.trim();
//					if(flightToSave.endsWith(";")) {
//						flightToSave = flightToSave.substring(0, flightToSave.length()-1);
//					}
//
//					fw.write(flightToSave
//							 + System.lineSeparator());
//				}
//				catch(Exception e) {
//
//				}
//			});
//
//			fw.close();
//		}
//		catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//	}
	
	//Write report of all flights
	//Replace all Integers things by the Flight Class
	//Need testing once we have all the other classes
//	public static void generateReport(HashMap<String, Flight> flightHM) {
//
//		/**Structure of the file:
//			Company1Code:
//				- Number of flights:X
//				- Total number of kilometers:X
//				- Average fuel consumption: X
//				- Average CO2 emission: X
//			Company2Code:
//			...
//		**/
//
//
//		//Copy the flightList in a separate HashMap in order to modify it later without impact flightList
//		HashMap<String, Flight> tempFlightList = new HashMap<String, Flight>();
//		flightHM.forEach((code, flight) -> {
//			tempFlightList.put(code, flight);
//		});
//
//
//		//HashMap<String, Aeroplane> aeroplanes = loadAeroplanes();
//		//HashMap<String, Airport> airports = loadAirports();
//		HashMap<String, Airline> airlines = loadAirlines();
//
//
//
//		try {
//			FileWriter fw = new FileWriter("Report.txt");
//
//			//get first element's company code
//
//			while(tempFlightList.size() != 0) {
//				String airlineCode = ((String) tempFlightList.keySet().toArray()[0]).substring(0,2); //REMOVE the serial number
//				fw.write(airlines.get(airlineCode).getName() + System.lineSeparator());
//
//
//				/**Concerning this array:
//					-0 is the number of flight
//					-1 is the total number of kilometers
//					-2 is the average fuel consumption
//					-3 is the average CO2 emission
//				**/
//				double[] companyInformation = new double[4];
//				tempFlightList.forEach((code, flight) -> {
//					if(flight.getIdentifier().contains(airlineCode)) {
//						companyInformation[0] += 1.0;
//						try {
//							companyInformation[1] += flight.distanceCovered();
//							companyInformation[2] += flight.fuelConsumption();
//							companyInformation[3] += flight.CO2Emission();
//						}
//						catch (Exception E) {
//							System.err.println(E.getMessage());
//						}
//					}
//				});
//
//				fw.write("	Number of flights: " + companyInformation[0] + System.lineSeparator());
//				fw.write("	Total number of kilometers: " + companyInformation[1] + System.lineSeparator());
//				fw.write("	Average Fuel consumption: " + companyInformation[2] + System.lineSeparator());
//				fw.write("	Average CO2 emission: " + companyInformation[3] + System.lineSeparator());
//				tempFlightList.entrySet().removeIf(entry -> entry.getKey().contains(airlineCode));
//			}
//
//			fw.close();
//		}
//		catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//
//	}
}
