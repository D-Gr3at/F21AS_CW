package threads;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Queue;

import exception.InvalidFlightException;
import exception.ResourceNotFoundException;
import flightressources.Aeroplane;
import flightressources.Airport;
import flightressources.ControlTower;
import flightressources.Flight;
import flightressources.FlightPlan;
import flightressources.GPSCoordinate;

public class FlightRunnable extends Flight implements Runnable{
	
	private boolean landed = false;
	private ControlTower nearestControlTower;
    private final Double EMISSION_FACTOR = 8.31; //kg per litre
    private double currentDistance = 1800.0;
	private double currentTime = 0.0;
	private double currentFuel = 0.0;
	private double currentCO2 = 0.0; 
	private GPSCoordinate currentGPSCoordinate;

	private static final int updateFrequency = 2000;

	
	
	public FlightRunnable() {
		//test constructor
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
	
	
	
	public boolean isLanded() {
		return landed;
	}

	public void setLanded(boolean landed) {
		this.landed = landed;
	}

	public ControlTower getNearestControlTower() {
		return nearestControlTower;
	}

	public void setNearestControlTower(ControlTower nearestControlTower) {
		this.nearestControlTower = nearestControlTower;
	}

	public double getCurrentDistance() {
		return currentDistance;
	}

	public void setCurrentDistance(double currentDistance) {
		this.currentDistance = currentDistance;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	public double getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(double currentFuel) {
		this.currentFuel = currentFuel;
	}

	public double getCurrentCO2() {
		return currentCO2;
	}

	public void setCurrentCO2(double currentCO2) {
		this.currentCO2 = currentCO2;
	}

	public GPSCoordinate getCurrentGPSCoordinate() {
		return currentGPSCoordinate;
	}

	public void setCurrentGPSCoordinate(GPSCoordinate currentGPSCoordinate) {
		this.currentGPSCoordinate = currentGPSCoordinate;
	}


	
	@Override
	public void run() {
		//Use these to compute the new coordinates of the flight
		int flightPlanStep = 0;
		ControlTower latestControlTower = this.getFlightPlan().getControlTowers().get(flightPlanStep);
		ControlTower nextControlTower = this.getFlightPlan().getControlTowers().get(flightPlanStep+1);
		
		setNearestControlTower(this.getFlightPlan().getControlTowers().get(flightPlanStep));
		
		setCurrentGPSCoordinate(this.getFlightPlan().getControlTowers().getFirst().getCoordinates());
				
		 //use this to compute the current distance in the currentFlightPlanStep (know which control tower is near)
		double currentStepDistanceTraveled= 0.0;
		
		double distanceUpdate = (this.getPlane().getSpeed()/3600000) * updateFrequency;


		try {
			while(!isLanded()) { //!landed
				sleep(updateFrequency);

				setCurrentDistance(getCurrentDistance() + distanceUpdate);
				setLanded(hasLanded(getCurrentDistance()));
				
				setCurrentGPSCoordinate(getCurrentGPSCoordinate().addCircleDistance(nextControlTower.getCoordinates(), distanceUpdate));
				updateInformation();
				
				Double dist = latestControlTower.distanceBetweenControlTower(nextControlTower);
				if(!isLanded() && getCurrentDistance() >= (dist+currentStepDistanceTraveled)) {
					flightPlanStep++;
					latestControlTower = this.getFlightPlan().getControlTowers().get(flightPlanStep);
					nextControlTower = this.getFlightPlan().getControlTowers().get(flightPlanStep+1);
					currentStepDistanceTraveled += dist;
				}

				if(!getNearestControlTower().compareTo(nextControlTower) && getCurrentDistance()-currentStepDistanceTraveled > dist/2) {
					updateNearestControlTower(flightPlanStep);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateInformation() {
		double dist = getCurrentDistance();
		setCurrentTime(getTimeFromDistance(dist));
		setCurrentFuel(getFuelFromDistance(dist));
		setCurrentCO2(getCO2FromDistance(dist));
	}
	 
	private void updateNearestControlTower(int flightPlanStep) {
		setNearestControlTower(this.getFlightPlan().getControlTowers().get(flightPlanStep+1));
	}
	
	private double getTimeFromDistance(double distance){
		return distance/this.getPlane().getSpeed();
	}
	
	private double getFuelFromDistance(double distance) {
		return distance * this.getPlane().getFuelConsumption() / 100;
	}
	
	private double getCO2FromDistance(double distance) {
		return getFuelFromDistance(distance) * this.EMISSION_FACTOR;
	}
	
	private boolean hasLanded(double currentDistance) {
		try {
			if(currentDistance >= this.distanceCovered()) {
				return true;
			}
		} catch(ResourceNotFoundException e) {
			//Throw exception
		}
		
		return false;
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	

}
