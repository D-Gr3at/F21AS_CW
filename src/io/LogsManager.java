package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class LogsManager {
	
	private static String logs = null;
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

	private LogsManager() {}
	
	private static String getLogs() {
		if(logs == null) {
			logs = "";
		}
		return logs;
	}
	
	public synchronized static void addToLogs(String flightCode) {
		synchronized(getLogs()){
			LocalDateTime now = LocalDateTime.now();  
			logs += "[" + dtf.format(now) + "] ";
			logs += "Flight " + flightCode +" is now on-going.\n";
		}
	}
	
	public synchronized static void addToLogs(String flightCode, boolean isLanded) {
		synchronized(getLogs()){
			if(isLanded) {
				LocalDateTime now = LocalDateTime.now();  
				logs += "[" + dtf.format(now) + "] ";
				logs += "Flight " + flightCode + " has landed correctly.\n";
			}
		}
	}
	
	public synchronized static void addToLogs(String flightCode, String airportCode) {
		synchronized(getLogs()){
			LocalDateTime now = LocalDateTime.now();  
			logs += "[" + dtf.format(now) + "] ";
			logs += "The " + airportCode + " control tower starts communicating with the " + flightCode + " flight.\n";
		}
	}
	
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
