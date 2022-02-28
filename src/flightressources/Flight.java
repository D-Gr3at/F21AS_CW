package flightressources;
import exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Flight {

    private String identifier;
    private Aeroplane plane;
    private Airport departureAirport;
    private Airport destinationAirport;
    private LocalDateTime departureDateTime;
    private FlightPlan flightPlan;
    private final Double EMISSION_FACTOR = 8.31; //kg per litre
    
    public Flight(String identifier, 
    			  Aeroplane plane,
    			  Airport departureAirport,
    			  Airport destinationAirport,
    			  LocalDateTime departureDateTime,
    			  FlightPlan flightPlan) {
    	setIdentifier(identifier);
    	setPlane(plane);
    	setDepartureAirport(departureAirport);
    	setDestinationAirport(destinationAirport);
    	setDepartureDateTime(departureDateTime);
    	setFlightPlan(flightPlan);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
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

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
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

    public void setFlightPlan(FlightPlan flightPlan) {
        this.flightPlan = flightPlan;
    }

    public Double distanceCovered() throws ResourceNotFoundException {
        double distance = 0;
        ControlTower controlTower = this.departureAirport.getControlTower();
        if (controlTower == null) {
            throw new ResourceNotFoundException("Control tower for this flight not found.");
        }
        GPSCoordinate gpsCoordinate = controlTower.getCoordinates();
        LinkedList<ControlTower> controlTowers = this.getFlightPlan().getControlTowers();
        if (controlTowers.isEmpty()) {
            throw new ResourceNotFoundException("Control towers to visit is empty.");
        }
        Double latitudeInRadian = gpsCoordinate.getLatitudeInRadian();
        Double longitudeInRadian = gpsCoordinate.getLongitudeInRadian();

        for (ControlTower otherControlTower : controlTowers) {
            GPSCoordinate otherControlTowerCoordinates = otherControlTower.getCoordinates();
            if (otherControlTowerCoordinates == null) {
                throw new ResourceNotFoundException("GPS coordinates not found.");
            }
            Double otherLatitudeInRadian = otherControlTowerCoordinates.getLatitudeInRadian();
            Double otherLongitudeInRadian = otherControlTowerCoordinates.getLongitudeInRadian();
            double deltaLongitude = otherLongitudeInRadian - longitudeInRadian;
            double deltaLatitude = otherLatitudeInRadian - latitudeInRadian;
            double trig = Math.pow(Math.sin(deltaLatitude / 2), 2.0) + Math.cos(latitudeInRadian)
                    * Math.cos(otherLatitudeInRadian) + Math.pow(Math.sin(deltaLongitude / 2), 2.0);
            
            
            double sqrt = Math.sqrt(trig);
            
            if(sqrt >= 1) {
            	sqrt -=1;
            }

            distance += 2 * 6371.00 * Math.asin(sqrt);
            
            latitudeInRadian = otherLatitudeInRadian;
            longitudeInRadian = otherLongitudeInRadian;
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
        LinkedList<ControlTower> controlTowers = flightPlan.getControlTowers();
        if (controlTowers.isEmpty()) {
            throw new ResourceNotFoundException("Control towers not found.");
        }
        for (ControlTower controlTower: controlTowers) {
            Double distanceBetweenControlTower = departureAirportControlTower
                    .distanceBetweenControlTower(controlTower);
            timeTaken += distanceBetweenControlTower / speed;
            departureAirportControlTower = controlTower;
        }
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

    public Double CO2Emission() throws ResourceNotFoundException{
        return this.fuelConsumption() * EMISSION_FACTOR;
    }
}
