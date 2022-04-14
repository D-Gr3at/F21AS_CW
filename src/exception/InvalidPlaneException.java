package exception;

/*
 * Signals that an error related to the creation or modification of a plane has occurred.
 */
public class InvalidPlaneException extends Exception {
	
	/*
	 * Creates an InvalidPlaneException with the specified detail message.
	 */
	public InvalidPlaneException(String message) {
		super(message);
	}
}
