package controller;

import exception.InvalidAirlineException;
import exception.ResourceNotFoundException;
import model.Airline;
import io.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * This controller handles requests related to airlines
 */
public class AirlineController {

    /*
     * This method gets an airline given the name of the airline
     * @throws IOException
     * @throws ResourceNotFoundException, InvalidAirlineException
     * */
    public Airline getAirlineByName(String airlineName) throws IOException, ResourceNotFoundException, InvalidAirlineException {
        Optional<Airline> optionalAirline = FileManager.loadAirlines()
                .stream()
                .filter(airline -> airline.getName().equalsIgnoreCase(airlineName))
                .findFirst();
        if (!optionalAirline.isPresent()) {
            throw new ResourceNotFoundException("Airline not found");
        }
        return optionalAirline.get();
    }

    /*
     * This method gets all airlines from FileManger
     * @throws IOException
     * @throws InvalidAirlineException
     * */
    public String[] getAirlineNames() throws InvalidAirlineException, IOException {
        String[] airlines;
        List<String> airlineNames = FileManager.loadAirlines()
                .stream()
                .map(Airline::getName)
                .collect(Collectors.toList());
        airlines = new String[airlineNames.size()];
        airlineNames.toArray(airlines);
        return airlines;
    }


}
