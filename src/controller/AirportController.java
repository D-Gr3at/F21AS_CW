package controller;

import exception.InvalidAirportException;
import exception.ResourceNotFoundException;
import model.Airport;
import io.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * This controller handles requests related to airport
 */
public class AirportController {

    /*
     * This method gets an airport given the airportCode
     * @throws IOException
     * @throws ResourceNotFoundException, InvalidAirportException
     * */
    public Airport getAirportByCode(String code) throws IOException, ResourceNotFoundException, InvalidAirportException {
        Optional<Airport> optionalAirport = FileManager.loadAirports()
                .stream()
                .filter(airport -> airport.getCode().equalsIgnoreCase(code))
                .findFirst();
        if (!optionalAirport.isPresent()) {
            throw new ResourceNotFoundException("Airport not found");
        }
        return optionalAirport.get();
    }

    /*
     * This method gets all airports from FileManger
     * @throws IOException
     * @throws InvalidAirportException
     * */
    public String[] getAirportCodes() throws IOException, InvalidAirportException {
        List<String> airportCodeList = FileManager.loadAirports()
                .stream()
                .map(Airport::getCode)
                .collect(Collectors.toList());
        String[] airportCodes = new String[airportCodeList.size()];
        airportCodeList.toArray(airportCodes);
        return airportCodes;
    }

}
