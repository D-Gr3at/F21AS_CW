package model;

public class FlightInformation {

	/*
	* private fields of the class*/
	private String flightIdentifier;
	
	private boolean landed;
	private double currentDistance;
	private double currentTime;
	private double currentFuel;
	private double currentCO2;
	private GPSCoordinate currentGPSCoordinate;
	private ControlTower nearestControlTower;
	private FlightPlan flightPlan;

	public FlightPlan getFlightPlan() {
		return flightPlan;
	}

	public void setFlightPlan(FlightPlan flightPlan) {
		this.flightPlan = flightPlan;
	}

	public String getFlightIdentifier() {
		return flightIdentifier;
	}
	public void setFlightIdentifier(String flightIdentifier) {
		this.flightIdentifier = flightIdentifier;
	}
	
	public boolean isLanded() {
		return landed;
	}
	public void setLanded(boolean landed) {
		this.landed = landed;
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
	public ControlTower getNearestControlTower() {
		return nearestControlTower;
	}
	public void setNearestControlTower(ControlTower nearestControlTower) {
		this.nearestControlTower = nearestControlTower;
	}

	/*
	 * Default constructor
	 * */
	public FlightInformation() {
	}
	
	
	
}
