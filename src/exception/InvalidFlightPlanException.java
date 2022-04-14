package exception;

/*
 * Signals that an error related to the creation or modification of a flight plan has occurred.
 */
public class InvalidFlightPlanException extends Exception{

	/*
	 * Creates an InvalidFlightPlanException with the specified detail message.
	 */
	public InvalidFlightPlanException(String message) {
		super(message);
	}
	
}
