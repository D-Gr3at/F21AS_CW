package exception;

/*
 * Signals that an error related to the creation or modification of a flight has occurred.
 */
public class InvalidFlightException extends Exception {
	
	/*
	 * Creates an InvalidFlightException with the specified detail message.
	 */
	public InvalidFlightException(String message) {
		super(message);
	}
}
