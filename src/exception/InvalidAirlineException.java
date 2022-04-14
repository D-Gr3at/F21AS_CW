package exception;

/*
 * Signals that an error related to the creation or modification of an airline has occurred.
 */
public class InvalidAirlineException extends Exception {
	
	/*
	 * Creates an InvalidAirlineException with the specified detail message.
	 */
	public InvalidAirlineException(String message) {
		super(message);
	}
}
