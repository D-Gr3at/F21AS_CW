package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exception.ResourceNotFoundException;
import flightressources.ControlTower;
import flightressources.Flight;
import flightressources.FlightInformation;

public class FlightRunnable extends Flight implements Runnable{
	
	private List<ControlTower> observers = new ArrayList<ControlTower>();
	
	//Replace this with flightInformation
    private final Double EMISSION_FACTOR = 8.31; //kg per litre
	
	private FlightInformation flightInformation = new FlightInformation();

	private static final int updateFrequency = 2000;

	
	
	public FlightRunnable() {
		super();
	}
	
	public FlightInformation getFlightInformation() {
		return flightInformation;
	}

	public void setFlightInformation(FlightInformation flightInformation) {
		this.flightInformation = flightInformation;
	}

	@Override
	public void run() {
		//Use these to compute the new coordinates of the flight
		int flightPlanStep = 0;
		ControlTower latestControlTower = this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep);
		ControlTower nextControlTower = this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep+1);
		
		flightInformation.setNearestControlTower(this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep));
		
		flightInformation.setCurrentGPSCoordinate(this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep).getCoordinates());
		flightInformation.setCurrentDistance(0.0);
		flightInformation.setCurrentCO2(0.0);
		flightInformation.setCurrentFuel(0.0);
		flightInformation.setCurrentTime(0.0);
		flightInformation.setFlightIdentifier(this.getIdentifier());
		
		 //use this to compute the current distance in the currentFlightPlanStep (know which control tower is near)
		double currentStepDistanceTraveled= 0.0;
		
		double distanceUpdate = (this.getPlane().getSpeed()/3600000) * updateFrequency;
		
		registerObserver(flightInformation.getNearestControlTower());
		
		notifyObservers();


		try {
			while(!flightInformation.isLanded()) { //!landed
				sleep(updateFrequency);

				flightInformation.setCurrentDistance(flightInformation.getCurrentDistance() + distanceUpdate);
				flightInformation.setLanded(hasLanded(flightInformation.getCurrentDistance()));
				
				flightInformation.setCurrentGPSCoordinate(flightInformation.getCurrentGPSCoordinate().addCircleDistance(nextControlTower.getCoordinates(), distanceUpdate));
				updateInformation();
				
				Double dist = latestControlTower.distanceBetweenControlTower(nextControlTower);
				if(!flightInformation.isLanded() && flightInformation.getCurrentDistance() >= (dist+currentStepDistanceTraveled)) {
					flightPlanStep++;
					latestControlTower = this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep);
					nextControlTower = this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep+1);
					currentStepDistanceTraveled += dist;
				}

				if(!flightInformation.getNearestControlTower().equals(nextControlTower) && flightInformation.getCurrentDistance()-currentStepDistanceTraveled > dist/2) {
					notifyObservers();
					removeObserver(flightInformation.getNearestControlTower());
					updateNearestControlTower(flightPlanStep);
					registerObserver(flightInformation.getNearestControlTower());
				} else {
					notifyObservers();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateInformation() {
		double dist = flightInformation.getCurrentDistance();
		flightInformation.setCurrentTime(getTimeFromDistance(dist));
		flightInformation.setCurrentFuel(getFuelFromDistance(dist));
		flightInformation.setCurrentCO2(getCO2FromDistance(dist));
	}
	 
	private void updateNearestControlTower(int flightPlanStep) {
		flightInformation.setNearestControlTower(this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep+1));
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
	
	public void registerObserver(ControlTower observer) {
		this.observers.add(observer);
	}
	
	public void removeObserver(ControlTower observer) {
		this.observers.remove(observer);
	}
	
	public void notifyObservers() {
		for(ControlTower observer: observers) {
			((ControlTowerRunnable) observer).update(flightInformation);
		}
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
