package threads;

import java.time.LocalDateTime;

import exception.InvalidFlightException;
import flightressources.Aeroplane;
import flightressources.Airport;
import flightressources.Flight;
import flightressources.FlightPlan;

public class FlightRunnable extends Flight implements Runnable{
	//duration?
	//landed?
	//nearest control tower?
	//current information: distance, time, fuel, co2?
	//current time?
	
	public FlightRunnable() {
		//simply test constructor
		super();
	}
	
	public FlightRunnable(Flight flight) throws InvalidFlightException {
			super(flight.getIdentifier(),
				  flight.getPlane(),
				  flight.getDepartureAirport(), 
				  flight.getDestinationAirport(),
				  flight.getDepartureDateTime(),
				  flight.getFlightPlan());
	}
	
	public FlightRunnable(String identifier, 
						  Aeroplane plane,
						  Airport departureAirport,
						  Airport destinationAirport,
						  LocalDateTime departureDateTime,
						  FlightPlan flightPlan) throws InvalidFlightException{
		super(identifier, plane, departureAirport, destinationAirport, departureDateTime, flightPlan);
	}
	
	
	public void run() {
		System.out.println("Flight runnable running in a thread!");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Just woke up!");
	}
	

}
