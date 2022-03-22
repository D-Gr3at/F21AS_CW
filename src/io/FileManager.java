package io;

import exception.ResourceNotFoundException;
import flightressources.*;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public void writeFlightDataToFile(List<Flight> flightList) throws Exception {
		File file = new File("Report.txt");
		if (!file.exists()){
			if (file.createNewFile()){
				updateFile(flightList, file);
			}else {
				throw new Exception("There is an error creating report file.");
			}
		}else {
			new PrintWriter(file).close();
			updateFile(flightList, file);
		}
    }

	private void updateFile(List<Flight> flightList, File file) throws IOException {
		FileWriter fileWriter = new FileWriter(file.getName());
		BufferedWriter writer = new BufferedWriter(fileWriter);
		Map<Airline, List<Flight>> airlineListMap = flightList.stream()
				.filter(flight -> flight.getAirline() != null)
				.collect(Collectors.groupingBy(Flight::getAirline));
		for (Airline airline: airlineListMap.keySet()){
			List<Flight> flights = airlineListMap.get(airline);
			writer.write(airline.getName());
			writer.write("\nNumber of flights: "+flights.size());
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
			writer.write("\nTotal number of kilometers covered: "+df.format(totalDistance));

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
				writer.write("\nAverage Fuel Consumption: "+df.format(averageFuelConsumption.getAsDouble()));

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
				writer.write("\nAverage CO2 Emission: "+df.format(averageCO2Emission.getAsDouble()));
			writer.write("\n \n");
		}
		writer.close();
	}
}
