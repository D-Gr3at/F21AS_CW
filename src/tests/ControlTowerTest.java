package tests;

import exception.InvalidAirportException;
import exception.InvalidFlightException;
import exception.InvalidFlightPlanException;
import exception.InvalidPlaneException;
import exception.ResourceNotFoundException;
import flightressources.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControlTowerTest {

    public Flight getFlight(){
        GPSCoordinate coordinates = new GPSCoordinate("164°57'12\"E", "77°30'36\"S");
        GPSCoordinate coordinates1 = new GPSCoordinate("37'19.85\"E", "41°37'0.26\"N");
        LinkedList<ControlTower> controlTowers = new LinkedList<>();
        ControlTower cont = new ControlTower(coordinates);
        Airport airport = null, dest = null;
        try {
            airport = new Airport("Tio", "TSE", cont);
            dest = new Airport("Geo", "GEW", new ControlTower(coordinates1));
        } catch (InvalidAirportException iae) {
        	//Test failed?
        }

        GPSCoordinate coordinates2 = new GPSCoordinate("120°57'12\"E", "44°30'36\"S");
        GPSCoordinate coordinates3 = new GPSCoordinate("118°57'12\"E", "35°30'36\"S");

        ControlTower cont1 = new ControlTower(coordinates2);
        controlTowers.add(cont1);
        ControlTower cont2 = new ControlTower(coordinates3);
        controlTowers.add(cont2);
        FlightPlan plan = null;
        try {
        	plan = new FlightPlan(controlTowers);
        } catch (InvalidFlightPlanException ifpe) {
        	//Test failed?
        }
        Flight flight = new Flight();
        try {
	        flight.setDestinationAirport(dest);
	        flight.setDepartureAirport(airport);
	        flight.setFlightPlan(plan);
	        flight.setPlane(new Aeroplane("", 0.0, "", 0.0));
        } catch (InvalidPlaneException ipe) {
        	//Test failed?
        }
        catch (InvalidFlightException ife) {
        	//Test failed?
        }
        return flight;
    }

    @Test
    public void testDistanceBetween(){
        GPSCoordinate coordinates2 = new GPSCoordinate("120°57'12\"E", "44°30'36\"S");
        GPSCoordinate coordinates3 = new GPSCoordinate("118°57'12\"E", "35°30'36\"S");
        ControlTower cont1 = new ControlTower(coordinates2);
        ControlTower cont2 = new ControlTower(coordinates3);
        try {
            assertEquals(cont1.distanceBetweenControlTower(cont2), 308.45, "Wrong result.");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }
}