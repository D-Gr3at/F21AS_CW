package controller;

import exception.InvalidAirlineException;
import exception.ResourceNotFoundException;
import model.Airline;
import io.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AirlineController {

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
