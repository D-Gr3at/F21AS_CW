package exception;

/*
 * Signals that an error related to the creation or modification of an airport has occurred.
 */
public class InvalidAirportException extends Exception{
	
	/*
	 * Creates an InvalidAirportException with the specified detail message.
	 */
	public InvalidAirportException(String message) {
		super(message);
	}
}
