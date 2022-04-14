package model;

import exception.InvalidFlightPlanException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightPlan {

    private LinkedList<Airport> airports;

    /*
     * Constructor of all private fields
     * */
    public FlightPlan(LinkedList<Airport> airports) throws InvalidFlightPlanException {
    	setAirports(airports);
    }

    public LinkedList<Airport> getAirports() {
        return airports;
    }

    public void setAirports(LinkedList<Airport> airports) throws InvalidFlightPlanException {
    	if(airports.size() > 20 || airports.size() < 2
    			||airports.getFirst().equals(airports.getLast())) throw new InvalidFlightPlanException("Invalid flight plan");
        this.airports = airports;
    }
    
    public List<ControlTower> getCorrespondingControlTowers(){
    	return this
                .getAirports()
                .stream()
                .map(Airport::getControlTower)
                .collect(Collectors.toList());
    }
}