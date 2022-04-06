package model;

import exception.InvalidFlightException;
import exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class Flight {

    private String identifier;
    private Aeroplane plane;
    private Airport departureAirport;
    private Airport destinationAirport;
    private LocalDateTime departureDateTime;
    private FlightPlan flightPlan;
    private Airline airline;
    private final Double EMISSION_FACTOR = 8.31; //kg per litre
    
    public Flight(String identifier, 
    			  Aeroplane plane,
    			  Airport departureAirport,
    			  Airport destinationAirport,
    			  LocalDateTime departureDateTime,
    			  FlightPlan flightPlan,
    			  Airline airline) throws InvalidFlightException{
    	
    	setIdentifier(identifier);
    	setPlane(plane);
    	setDepartureAirport(departureAirport);
    	setDestinationAirport(destinationAirport);
    	setDepartureDateTime(departureDateTime);
    	setFlightPlan(flightPlan);
    	setAirline(airline);
    }

    public Flight() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) throws InvalidFlightException{
    	if(identifier != null && identifier.length() > 7) throw new InvalidFlightException("Invalid identifier");
        this.identifier = identifier;
    }

    public Aeroplane getPlane() {
        return plane;
    }

    public void setPlane(Aeroplane plane) {
    	this.plane = plane;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) throws InvalidFlightException {
    	if(this.flightPlan != null 
    	   &&
    	   !this.flightPlan.getAirports()
				  		   .getFirst()
				  		   .equals(departureAirport)){
    		throw new InvalidFlightException("The departure airport doesn't correspond to the first airport of the flight plan");
    	}
    	if(this.destinationAirport != null
    	   &&
    	   departureAirport.equals(this.destinationAirport)) {
    		throw new InvalidFlightException("The departure is the same as the destination");
    	}
        this.departureAirport = departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) throws InvalidFlightException {
    	if(this.flightPlan != null
    	   &&
    	   !this.flightPlan.getAirports()
    					   .getLast()
    					   .equals(destinationAirport)) {
    		throw new InvalidFlightException("The destination airport doesn't correspond to the last airport of the flight plan");
    	}
    	if(this.departureAirport != null
    	   &&
    	   destinationAirport.equals(this.destinationAirport)) {
    		throw new InvalidFlightException("The destination is the same as the departure");
    	}
        this.destinationAirport = destinationAirport;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public FlightPlan getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(FlightPlan flightPlan) throws InvalidFlightException {
    	if(this.departureAirport != null
    	   &&
    	   !flightPlan.getAirports()
    				  .getFirst()
    				  .equals(this.departureAirport)) {
    		throw new InvalidFlightException("The departure airport doesn't correspond to the first airport of the flight plan");
    	}
    	if(this.destinationAirport != null
    	   &&
    	   !flightPlan.getAirports()
    				  .getLast()
    				  .equals(this.destinationAirport)) {
    		throw new InvalidFlightException("The destination airport doesn't correspond to the last airport of the flight plan");
    	}
        this.flightPlan = flightPlan;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Double distanceCovered() throws ResourceNotFoundException {
        double distance = 0;
        ControlTower controlTower = this.departureAirport.getControlTower();
        if (controlTower == null) {
            throw new ResourceNotFoundException("Control tower for this flight not found.");
        }        
        List<ControlTower> controlTowers = flightPlan.getCorrespondingControlTowers();
        if (controlTowers.isEmpty()) {
            throw new ResourceNotFoundException("Control towers to visit is empty.");
        }

        for (ControlTower otherControlTower : controlTowers) {
            Double distanceBetweenControlTower = controlTower
                    .distanceBetweenControlTower(otherControlTower);
            controlTower = otherControlTower;
            distance += distanceBetweenControlTower;
        }
        return distance;
    }
    

    public Double timeTaken() throws ResourceNotFoundException {
        double timeTaken = 0.0;
        Aeroplane aeroplane = this.getPlane();
        Double speed = aeroplane.getSpeed();
        FlightPlan flightPlan = this.getFlightPlan();
        ControlTower departureAirportControlTower = this.departureAirport.getControlTower();
        if (departureAirportControlTower == null){
            throw new ResourceNotFoundException("Departure airport control tower not found.");
        }
        List<ControlTower> controlTowers = flightPlan.getCorrespondingControlTowers();
        if (controlTowers.isEmpty()) {
            throw new ResourceNotFoundException("Control towers not found.");
        }
        timeTaken = this.distanceCovered() / speed;
        return timeTaken;
    }

    public Double fuelConsumption() throws ResourceNotFoundException {
        Aeroplane aeroplane = this.getPlane();
        Double fuelConsumption = aeroplane.getFuelConsumption();
        if (fuelConsumption == null){
            throw new ResourceNotFoundException("Fuel consumption for this plane is null.");
        }
        Double distanceCovered = this.distanceCovered();
        return distanceCovered * fuelConsumption / 100;
    }

    public Double CO2Emission() throws ResourceNotFoundException {
        return this.fuelConsumption() * EMISSION_FACTOR;
    }
}
