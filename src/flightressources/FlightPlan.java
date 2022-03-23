package flightressources;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import exception.InvalidFlightPlanException;

public class FlightPlan {

    private LinkedList<Airport> airports;
    
    public FlightPlan(LinkedList<Airport> airports) throws InvalidFlightPlanException {
    	setAirports(airports);
    }

    //CHANGED LIST
    public LinkedList<Airport> getAirports() {
        return airports;
    }

    public void setAirports(LinkedList<Airport> airports) throws InvalidFlightPlanException {
    	if(airports.size() > 20 || airports.size() < 2) throw new InvalidFlightPlanException("Invalid flight plan");
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