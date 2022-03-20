import java.util.LinkedList;

import flightressources.ControlTower;
import gui.*;
import io.FileManager;
import flightressources.GPSCoordinate;

import threads.FlightRunnable;

public class FlightTracker {

	public static void main(String[] args) {
		Gui.startGUI();
				
		Thread test = new Thread(new FlightRunnable());
		test.start();
		
		
	}

	
	
}
