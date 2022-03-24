package tests;

import java.util.LinkedList;

import exception.ResourceNotFoundException;
import flightressources.*;
import org.junit.jupiter.api.Test;
import exception.InvalidAirportException;
import exception.InvalidFlightException;
import exception.InvalidFlightPlanException;
import exception.InvalidPlaneException;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightTest {

   public Flight getFlight(){
      GPSCoordinate coordinates = new GPSCoordinate("164°57'12\"E", "77°30'36\"S");
      GPSCoordinate coordinates1 = new GPSCoordinate("37'19.85\"E", "41°37'0.26\"N");
      LinkedList<Airport> controlTowers = new LinkedList<>();
      ControlTower cont = new ControlTower(coordinates);
      Airport airport = null, dest = null;
      try {
	      airport = new Airport("Tio", "TSE", cont);
	      dest = new Airport("Geo", "GEW", new ControlTower(coordinates1));
      } catch (InvalidAirportException iae) {
    	  
      }

      GPSCoordinate coordinates2 = new GPSCoordinate("120°57'12\"E", "44°30'36\"S");
      GPSCoordinate coordinates3 = new GPSCoordinate("118°57'12\"E", "35°30'36\"S");

      ControlTower cont1 = new ControlTower(coordinates2);
	  FlightPlan plan = null;
	  try {
		  Airport airport1 = new Airport("", "", cont1);
		  controlTowers.add(airport1);
		  ControlTower cont2 = new ControlTower(coordinates3);
		  Airport airport2 = new Airport("", "", cont2);
		  controlTowers.add(airport2);
		  plan = new FlightPlan(controlTowers);
	  } catch (InvalidFlightPlanException ifpe) {
	  	  //Test failed?
	  } catch (InvalidAirportException ipe) {
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
    public void testDistanceCovered() {
       Flight flight = getFlight();
        try {
            assertEquals(flight.distanceCovered(), 1034.21, "Error in calculating distanced covered.");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
   public void testTimeTaken(){
       Flight flight = getFlight();
        try {
            assertEquals(flight.timeTaken(), 40, "Time not correct.");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
   public void consumption(){
      Flight flight = getFlight();
        try {
            assertEquals(flight.fuelConsumption(), 76, "Fuel consumption is wrong.");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
   public void CO2Emission(){
      Flight flight = getFlight();
        try {
            assertEquals(flight.CO2Emission(), 290, "Wrong value for CO2 emission.");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }
}