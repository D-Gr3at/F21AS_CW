package threads;

import exception.InvalidAirportException;
import model.ControlTower;
import model.FlightInformation;
import model.GPSCoordinate;
import view.Gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Extends the ControlTower class to implement the Runnable interface and be used in threads.
 */
public class ControlTowerRunnable extends ControlTower implements Runnable{

	
	/*
	 * Variable used to store the GUI observers we want to communicate with.
	 */
	private List<Gui> observers = new ArrayList<Gui>();
	
	/*
	 * Variable used to store the flight information from the flights runnable having this as observer.
	 */
	private ArrayList<FlightInformation> flightInformation = new ArrayList<FlightInformation>();

	/*
	 * Static variable corresponding to the frequency at which this will notify its observers.
	 */
	private static int updateFrequency = 3000;
	
	
	/*
	 * Class constructor using GPSCoordinates
	 */
	public ControlTowerRunnable(GPSCoordinate newCoordinates) {
		super(newCoordinates);
	}

	/*
	 * Class constructor using Strings for latitude and longitude
	 */
	public ControlTowerRunnable(String longitude, String latitude) {
		super(longitude, latitude);
	}
	
	/*
	 * Method used to change the updateFrequency.
	 */
	public static void setUpdateFrequency(int newUpdateFrequency) {
		updateFrequency = newUpdateFrequency;
	}

	
	/*
	 * Infinite loop that sleeps for the time specified in updateFrequency, then sends the list of flightInformation to its observers.
	 */
	@Override
	public void run() {
		while(true) {
			sleep(updateFrequency);
			synchronized(flightInformation) {
				//System.out.println(this.flightInformation);
				try {
					notifyObservers();
				} catch (InvalidAirportException e) {
					e.printStackTrace();
				}
			}
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
	
	/*
	 * Adds an observer to the list of observers.
	 */
	public void registerObserver(Gui observer) {
		this.observers.add(observer);
	}
	
	/*
	 * Removes an observer from the list of observers.
	 */
	public void removeObserver(Gui observer) {
		this.observers.remove(observer);
	}
	
	/*
	 * Sends the list of flight information to each observer in the list of observers.
	 * @throws InvalidAirportException if there's an error with the airports used in the update method from the view.Gui class.
	 */
	private synchronized void notifyObservers() throws InvalidAirportException {
		for(Gui observer: observers) {
			observer.update(flightInformation);
		}
	}
	
	/*
	 * Updates the flightInformation array list depending on the data received from the classes observing this.
	 */
	public void update(FlightInformation flightInformation) {
		synchronized(this.flightInformation) {
			this.flightInformation.remove(flightInformation);
			if(this.equals(flightInformation.getNearestControlTower())){
				this.flightInformation.add(flightInformation);
			}
		}
	}
	
	/*
	 * Updates the frequency at which the flights runnable communicate with their observers.
	 */
	public static void setFlightUpdateFrequency(int newUpdateFrequency) {
		FlightRunnable.setUpdateFrequency(newUpdateFrequency);
	}
}
