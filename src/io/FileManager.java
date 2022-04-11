package io;

import model.*;
import threads.ControlTowerRunnable;
import threads.FlightRunnable;
import exception.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class FileManager {
	
	private FileManager() {}
	
	private static List<Airport> airportsSingleton = null;
	
	private static List<Flight> flightsSingleton = null;
	
	private static List<Aeroplane> planesSingleton = null;
	
	private static List<Airline> airlinesSingleton = null;

	public static List<Flight> getDefaultFlights() throws IOException, InvalidFlightException, InvalidPlaneException, InvalidAirportException, InvalidFlightPlanException, InvalidAirlineException {
		if(flightsSingleton == null) {
			List<Flight> flights = new ArrayList<>();
			String str;
			File file = new File("Flights.txt");
			if (!file.exists()){
				if (!file.createNewFile()){
					throw new IOException("There is an error creating the flights file.");
				}
			}
			FileReader fileReader = new FileReader("Flights.txt");
			//InputStream data = FileManager.class.getResourceAsStream("/Flights.txt");
			//InputStreamReader isr = new InputStreamReader(fileReader);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((str = bufferedReader.readLine()) != null) {
				String[] line = str.split("; ");
				Flight flight = new FlightRunnable();
				flight.setIdentifier(line[0]);
				List<Airline> airlines = loadAirlines();
				Optional<Airline> optionalAirline = airlines.stream()
						.filter(airline -> airline.getCode() != null)
						.filter(airline -> airline.getCode().trim().equalsIgnoreCase(line[0].substring(0,2))
								|| airline.getCode().trim().equalsIgnoreCase(line[0].substring(0,3)))
						.findFirst();
				if(optionalAirline.isPresent()) {
					Airline airline = optionalAirline.get();
					flight.setAirline(airline);
				}
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
				Collections.sort(airports1, Comparator.comparing(item -> airportList.indexOf(item.getCode())));
				FlightPlan flightPlan = new FlightPlan(new LinkedList<>(airports1));
				flight.setFlightPlan(flightPlan);
				flights.add(flight);
				
				Thread flightThread = new Thread((FlightRunnable) flight);
				flightThread.start();
				LogsManager.addToLogs(flight.getIdentifier());
			}
			bufferedReader.close();
			flightsSingleton = flights;
		}
		return flightsSingleton;
	}

	public static List<Aeroplane> loadAeroplanes() throws IOException, InvalidPlaneException {
		if(planesSingleton == null) {
			List<Aeroplane> aeroplanes = new ArrayList<>();
			String str;
			//FileReader fileReader = new FileReader("Planes.txt");
			InputStream data = FileManager.class.getResourceAsStream("/Planes.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
			while ((str = bufferedReader.readLine()) != null) {
				String[] line = str.split("; ");
				Aeroplane aeroplane = new Aeroplane();
				aeroplane.setModel(line[0]);
				aeroplane.setManufacturer(line[1]);
				aeroplane.setSpeed(Double.parseDouble(line[2]));
				aeroplane.setFuelConsumption(Double.parseDouble(line[3]));
				aeroplanes.add(aeroplane);
			}
			bufferedReader.close();
			planesSingleton = aeroplanes;
		}
		return planesSingleton;
	}

	public synchronized static List<Airport> loadAirports() throws IOException, InvalidAirportException {
		if(airportsSingleton == null) {
			List<Airport> airports = new ArrayList<>();
			String str;
			//FileReader fileReader = new FileReader("Airports.txt");
			InputStream data = FileManager.class.getResourceAsStream("/Airports.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
			while ((str = bufferedReader.readLine()) != null) {
				String[] line = str.split("; ");
				ControlTower controlTowerToAdd = new ControlTowerRunnable(new GPSCoordinate(line[3], line[2]));
				Airport airport = new Airport();
				airport.setCode(line[0]);
				airport.setName(line[1]);
				airport.setControlTower(controlTowerToAdd);
				airports.add(airport);
				Thread controlTowerThread = new Thread((ControlTowerRunnable) controlTowerToAdd);
				controlTowerThread.start();
			}
			bufferedReader.close();
			airportsSingleton = airports;
		}
		return airportsSingleton;
	}

	public static List<Airline> loadAirlines() throws IOException, InvalidAirlineException {
		if(airlinesSingleton == null) {
			List<Airline> airlines = new ArrayList<>();
			String str;
			//FileReader fileReader = new FileReader("Airlines.txt");
			InputStream data = FileManager.class.getResourceAsStream("/Airlines.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
			while ((str = bufferedReader.readLine()) != null) {
				String[] line = str.split("; ");
				airlines.add(new Airline(line[1], line[0]));
			}
			bufferedReader.close();
			airlinesSingleton = airlines;
		}
		return airlinesSingleton;
	}

    public static void writeFlightDataToReport(List<Flight> flightList) throws Exception {
		File file = new File("Report.txt");
		if (!file.exists()){
			if (file.createNewFile()){
				updateReport(flightList, file);
			}else {
				throw new Exception("There is an error creating report file.");
			}
		}else {
			new PrintWriter(file).close();
			updateReport(flightList, file);
		}
    }

	private static void updateReport(List<Flight> flightList, File file) throws IOException {
		FileWriter fileWriter = new FileWriter(file.getName());
		BufferedWriter writer = new BufferedWriter(fileWriter);
		Map<Airline, List<Flight>> airlineListMap = flightList.stream()
				.filter(flight -> flight.getAirline() != null)
				.collect(Collectors.groupingBy(Flight::getAirline));
		
		for (Airline airline: airlineListMap.keySet()){
			List<Flight> flights = airlineListMap.get(airline);
			writer.write(airline.getName());
			writer.write("\n\tNumber of flights: "+flights.size());
			double totalDistance = flights.stream()
					.mapToDouble(flight -> {
						try {
							return flight.distanceCovered();
						} catch (ResourceNotFoundException e) {
							System.out.println(e.getMessage());
						}
						return 0;
					}).sum();
			DecimalFormat df = new DecimalFormat("###.##");
			writer.write("\n\tTotal number of kilometers covered: "+df.format(totalDistance));

			OptionalDouble averageFuelConsumption = flights.stream()
					.mapToDouble(flight -> {
						try {
							return flight.fuelConsumption();
						} catch (ResourceNotFoundException e) {
							e.printStackTrace();
						}
						return 0;
					}).average();
			if (averageFuelConsumption.isPresent())
				writer.write("\n\tAverage Fuel Consumption: "+df.format(averageFuelConsumption.getAsDouble()));

			OptionalDouble averageCO2Emission = flights.stream()
					.mapToDouble(flight -> {
						try {
							return flight.CO2Emission();
						} catch (ResourceNotFoundException e) {
							e.printStackTrace();
						}
						return 0;
					}).average();
			if (averageCO2Emission.isPresent())
				writer.write("\n\tAverage CO2 Emission: "+df.format(averageCO2Emission.getAsDouble()));
			writer.write("\n \n");
		}
		writer.close();
	}
	
    public static void writeFlightDataToFlightFile(List<Flight> flightList) throws Exception {
		File file = new File("Flights.txt");
		if (!file.exists()){
			if (file.createNewFile()){
				updateFlightFile(flightList, file);
			}else {
				throw new Exception("There is an error creating the flights file.");
			}
		}else {
			new PrintWriter(file).close();
			updateFlightFile(flightList, file);
		}
    }
	
	private static void updateFlightFile(List<Flight> flightList, File file) throws IOException{
		FileWriter fileWriter = new FileWriter(file.getName());
		BufferedWriter writer = new BufferedWriter(fileWriter);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy; HH:mm");

		for (Flight flight: flightList){
			writer.write(flight.getIdentifier());
			writer.write("; ");
			
			writer.write(flight.getPlane().getModel());
			writer.write("; ");
			
			writer.write(flight.getDepartureAirport().getCode());
			writer.write("; ");

			writer.write(flight.getDestinationAirport().getCode());
			writer.write("; ");

			writer.write(flight.getDepartureDateTime().format(formatter));
			writer.write("; ");
			
			for(Airport airport: flight.getFlightPlan().getAirports()) {
				writer.write(airport.getCode());
				writer.write("; ");
			}
			writer.write("\n");
		}
		writer.close();
	}
}
