package threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import flightressources.ControlTower;
import flightressources.GPSCoordinate;
import gui.Gui;
import flightressources.Flight;
import flightressources.FlightInformation;

public class ControlTowerRunnable extends ControlTower implements Runnable{

	
	//change GUI to JFrame?
	private List<Gui> observers = new ArrayList<Gui>();
	
	private ArrayList<FlightInformation> flightInformation = new ArrayList<FlightInformation>();

	private static final int updateFrequency = 3000;
	
	
	
	public ControlTowerRunnable(GPSCoordinate newCoordinates) {
		super(newCoordinates);
	}
	
	public ControlTowerRunnable(String longitude, String latitude, HashMap<String, FlightRunnable> sharedFlightHashMap) {
		super(longitude, latitude);
		//this.sharedFlightHashMap = sharedFlightHashMap;
	}
	
	public ControlTowerRunnable(String longitude, String latitude) {
		super(longitude, latitude);
	}

	@Override
	public void run() {
		while(true) {
			sleep(updateFrequency);
			synchronized(flightInformation) {
				//System.out.println(this.flightInformation);
				notifyObservers();
			}
		}
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void registerObserver(Gui observer) {
		this.observers.add(observer);
	}
	
	public void removeObserver(Gui observer) {
		this.observers.remove(observer);
	}
	
	//Here, communicate information about flights
	private synchronized void notifyObservers() {
		for(Gui observer: observers) {
			observer.update(flightInformation);
		}
	}
	
	public void update(FlightInformation flightInformation) {
		synchronized(this.flightInformation) {
			this.flightInformation.remove(flightInformation);
			if(this.equals(flightInformation.getNearestControlTower())){
				this.flightInformation.add(flightInformation);
			}
		}
	}
}