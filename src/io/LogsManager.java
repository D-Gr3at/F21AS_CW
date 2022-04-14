package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Final class used to store and write the logs of events in our application.
 */
public final class LogsManager {
	
	/*
	 * Variable used to store the logs of event happening in the application.
	 */
	private static String logs = null;
	
	/*
	 * Variable used to generate the date and time at which the events happen.
	 */
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

	private LogsManager() {}
	
	/*
	 * Creates the logs string if it doesn't exist and return it.
	 */
	private static String getLogs() {
		if(logs == null) {
			logs = "";
		}
		return logs;
	}
	
	/*
	 * Adds the current system date and time to the logs string, and specifies that the event corresponds to a new flight starting.
	 */
	public synchronized static void addToLogs(String flightCode) {
		synchronized(getLogs()){
			LocalDateTime now = LocalDateTime.now();  
			logs += "[" + dtf.format(now) + "] ";
			logs += "Flight " + flightCode +" is now on-going.\n";
		}
	}
	
	/*
	 * Adds the current system date and time to the logs string, and specifies that the event corresponds to a flight landing.
	 */
	public synchronized static void addToLogs(String flightCode, boolean isLanded) {
		synchronized(getLogs()){
			if(isLanded) {
				LocalDateTime now = LocalDateTime.now();  
				logs += "[" + dtf.format(now) + "] ";
				logs += "Flight " + flightCode + " has landed correctly.\n";
			}
		}
	}
	
	/*
	 * Adds the current system date and time to the logs string, and specifies that the event corresponds to a flight changing the control tower it's communicating with.
	 */
	public synchronized static void addToLogs(String flightCode, String airportCode) {
		synchronized(getLogs()){
			LocalDateTime now = LocalDateTime.now();  
			logs += "[" + dtf.format(now) + "] ";
			logs += "The " + airportCode + " control tower starts communicating with the " + flightCode + " flight.\n";
		}
	}
	
	/*
	 * Tries to get the Logs.txt file and create it if it doesn't exist.
	 * Writes the logs string to the file.
	 * @throws Exception if there's a problem when creating the file.
	 */
	public synchronized static void saveLogsToFile() throws Exception {
		File file = new File("Logs.txt");
		if (!file.exists()){
			if (!file.createNewFile()){
				throw new Exception("There is an error creating the flights file.");
			}
		}
		new PrintWriter(file).close();
		FileWriter fileWriter = new FileWriter(file.getName());
		BufferedWriter writer = new BufferedWriter(fileWriter);
		writer.write(getLogs());
		
		writer.close();
	}
}
