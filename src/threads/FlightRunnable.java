package threads;

import exception.ResourceNotFoundException;
import model.ControlTower;
import model.Flight;
import model.FlightInformation;
import io.LogsManager;

import java.util.ArrayList;
import java.util.List;

/*
 * Extends the Flight class to implement the Runnable interface and be used in threads.
 */
public class FlightRunnable extends Flight implements Runnable{
	
	/*
	 * Variable used to store the ControlTowers observers we want to communicate with.
	 */
	private List<ControlTower> observers = new ArrayList<>();
	
	/*
	 * Variable used to store the emission_factor depending on the fuel used in kg/litre.
	 */
    private final Double EMISSION_FACTOR = 8.31; //kg per litre
	
	/*
	 * Variable used to store the flight information for this flight.
	 */
	private FlightInformation flightInformation = new FlightInformation();

	/*
	 * Static variable corresponding to the frequency at which this will notify its observers.
	 */
	private static int updateFrequency = 2000;

	
	/*
	 * Constructor class.
	 */
	public FlightRunnable() {
		super();
	}
	
	/*
	 * Returns the flight information variable.
	 */
	public FlightInformation getFlightInformation() {
		return flightInformation;
	}

	/*
	 * Sets the flight information variable.
	 */
	public void setFlightInformation(FlightInformation flightInformation) {
		this.flightInformation = flightInformation;
	}
	
	/*
	 * Method used to change the updateFrequency variable.
	 */
	public static void setUpdateFrequency(int newUpdateFrequency) {
		updateFrequency = newUpdateFrequency;
	}

	/*
	 * Initializes the flight information then enters a loop that sleeps for the time specified in updateFrequency and updates the flightInformation variable.
	 */
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
				distanceUpdate = (this.getPlane().getSpeed()/3600000) * updateFrequency;


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

					ControlTower observerToRemove = flightInformation.getNearestControlTower();
					updateNearestControlTower(flightPlanStep);
					LogsManager.addToLogs(this.getIdentifier(), 
							flightInformation.getNearestControlTower().getCorrespondingAirport().getName());
					registerObserver(flightInformation.getNearestControlTower());
					notifyObservers();
					removeObserver(observerToRemove);
				} else {
					notifyObservers();
				}
			}
			LogsManager.addToLogs(getIdentifier(), flightInformation.isLanded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Calls different methods to update the current informations of this flight: current distance, time, fuel consumed, CO2 emitted.
	 */
	private void updateInformation() {
		double dist = flightInformation.getCurrentDistance();
		flightInformation.setCurrentTime(getTimeFromDistance(dist));
		flightInformation.setCurrentFuel(getFuelFromDistance(dist));
		flightInformation.setCurrentCO2(getCO2FromDistance(dist));
	}
	 
	/*
	 * Changes the nearestControlTower variable from the flightInformation depending on the integer specified.
	 * 0 means the first control tower of the flight plan, 1 the second one...
	 */
	private void updateNearestControlTower(int flightPlanStep) {
		flightInformation.setNearestControlTower(this.getFlightPlan().getCorrespondingControlTowers().get(flightPlanStep+1));
	}
	
	/*
	 * Computes and returns how much time passed since the beginning of this flight depending on the specified current distance.
	 */
	private double getTimeFromDistance(double distance){
		return distance/this.getPlane().getSpeed();
	}
	
	/*
	 * Computes and returns the fuel consumed by this flight depending on the specified current distance.
	 */
	private double getFuelFromDistance(double distance) {
		return distance * this.getPlane().getFuelConsumption() / 100;
	}
	
	/*
	 * Computes and returns the CO2 emitted by this flight depending on the specified current distance.
	 */
	private double getCO2FromDistance(double distance) {
		return getFuelFromDistance(distance) * this.EMISSION_FACTOR;
	}
	
	/*
	 * Computes if the flight has landed or not depending on the distance specified.
	 * Returns true if the distance specified is bigger than the total distance the flight is supposed to travel, false otherwise.
	 */
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
	
	/*
	 * Adds an observer to the list of observers.
	 */
	public void registerObserver(ControlTower observer) {
		this.observers.add(observer);
	}
	
	/*
	 * Removes an observer from the list of observers.
	 */
	public void removeObserver(ControlTower observer) {
		this.observers.remove(observer);
	}
	
	/*
	 * Sends this flight information to each observer in the list of observers.
	 */
	public synchronized void notifyObservers() {
		for(ControlTower observer: observers) {
			((ControlTowerRunnable) observer).update(flightInformation);
		}
	}
	
	/*
	 * Method used to make the thread this is used in sleep for the specified time in ms.
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
