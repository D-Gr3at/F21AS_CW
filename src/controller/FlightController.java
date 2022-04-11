package controller;

import exception.*;
import model.Airport;
import model.Flight;
import model.FlightInformation;
import model.FlightPlan;
import io.FileManager;
import io.LogsManager;
import threads.FlightRunnable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FlightController {

    private AirportController airportController;
    private AirlineController airlineController;
    private String[][] flightData;

    public void createNewFlight(List<Flight> flightList, String[][] details) throws IOException, ResourceNotFoundException, InvalidFlightException, InvalidFlightPlanException, InvalidAirportException, InvalidPlaneException, InvalidAirlineException {
        Flight flight = new FlightRunnable();
        airportController = new AirportController();
        airlineController = new AirlineController();
        try {
            flight.setIdentifier(airlineController.getAirlineByName(details[0][0]).getCode() + details[0][1]);
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No airline selected");
        }
        try {
            flight.setDepartureAirport(airportController.getAirportByCode(details[0][3]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No departure airport selected");
        }
        try {
            flight.setDestinationAirport(airportController.getAirportByCode(details[0][4]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No destination airport selected");
        }
        try {
            AeroplaneController aeroplaneController = new AeroplaneController();
            flight.setPlane(aeroplaneController.getPlaneByCode(details[0][2]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No plane selected");
        }
        try {
            flight.setAirline(airlineController.getAirlineByName(details[0][0]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No airline selected");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime
                .parse(details[0][5] + " " + details[0][6], formatter)
                .atZone(ZoneId.of("CET")).toLocalDateTime();
        flight.setDepartureDateTime(localDateTime);
        List<Airport> airports = Arrays.stream(details[1])
                .filter(code -> !code.contains("Choose"))
                .map(code -> {
                    try {
                        return airportController.getAirportByCode(code);
                    } catch (IOException | ResourceNotFoundException | InvalidAirportException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
        flight.setFlightPlan(new FlightPlan(new LinkedList<>(airports)));

        Thread flightThread = new Thread((FlightRunnable) flight);
        flightThread.start();
        LogsManager.addToLogs(flight.getIdentifier());
        flightList.add(flight);
    }

    public String[][] getCurrentFlightData(List<FlightInformation> flightInformationList, List<Flight> flightList) {
        String[][] data = new String[flightInformationList.size() + flightList.size()][2];
        if (flightInformationList.isEmpty()) {
            data = new String[1][2];
            data[0][0] = "No Flight";
        } else {
            for (int i = 0; i < flightInformationList.size(); i++) {
                data[i][0] = flightInformationList.get(i).getFlightIdentifier();
            }
        }
        if (flightList.isEmpty()) {
            data[0][1] = "No Flight";
        } else {
            for (int i = 0; i < flightList.size(); i++) {
                data[i][1] = flightList.get(i).getIdentifier();
            }
        }
        return data;
    }

    public String[] getFlightTimes() {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            int j = 0;
            while (j < 60) {
                String format = String.format("%02d:%02d", i, j);
                times.add(format);
                j += 10;
            }
        }
        String[] timing = new String[times.size()];
        return times.toArray(timing);
    }

    /*
     * Gets the flight plan for a particular flight
     * */
    public Object[][] getFlightPlan(String flightCode, List<Flight> flightList) throws ResourceNotFoundException {
        Flight flight = getFlightWithCode(flightCode, flightList);
        List<Airport> airports = flight.getFlightPlan().getAirports();
        flightData = new String[airports.size()][];
        Airport[] ports = new Airport[airports.size()];
        airports.toArray(ports);
        for (int i = 0; i < ports.length; i++) {
            String[] airportData = new String[]{
                    ports[i].getCode(),
                    ""
            };
            flightData[i] = airportData;
        }
        return flightData;
    }

    /*
     * Get a flight a given unique flight code
     * @throws ResourceNotFoundException if flight is not found.
     * @throws IOException if there's an error reading default data from file.
     * */
    private Flight getFlightWithCode(String code, List<Flight> flightList) throws ResourceNotFoundException {
        Optional<Flight> optionalFlight = flightList
                .stream()
                .filter(flight1 -> flight1.getIdentifier().equalsIgnoreCase(code))
                .findFirst();
        if (!optionalFlight.isPresent())
            throw new ResourceNotFoundException("Flight not found.");
        return optionalFlight.get();
    }

    public String[][] getFlights() throws IOException,
            InvalidFlightException, InvalidPlaneException,
            InvalidAirportException, InvalidFlightPlanException,
            InvalidAirlineException {
        List<Flight> flights = FileManager.getDefaultFlights();
        Flight[] flights1 = new Flight[flights.size()];
        flightData = new String[flights.size()][];
        flights.toArray(flights1);
        for (int i = 0; i < flights1.length; i++) {
            LocalDateTime departureDateTime = flights1[i].getDepartureDateTime();
            String latitude = flights1[i].getDepartureAirport()
                    .getControlTower()
                    .getCoordinates()
                    .getLatitude();
            String longitude = flights1[i].getDepartureAirport()
                    .getControlTower()
                    .getCoordinates()
                    .getLongitude();
            String[] line = new String[]{
                    flights1[i].getIdentifier(),
                    flights1[i].getPlane().getModel(),
                    flights1[i].getDepartureAirport().getCode(),
                    flights1[i].getDestinationAirport().getCode(),
                    departureDateTime.toLocalDate().toString(),
                    departureDateTime.toLocalTime().toString(),
                    latitude,
                    longitude,
                    "On-Flight"
            };

            flightData[i] = line;
        }
        return flightData;
    }


}
