package exception;

/*
 * Signals that a resource was not found.
 */
public class ResourceNotFoundException extends Exception {

	/*
	 * Creates an ResourceNotFoundException with the specified detail message.
	 */
	public ResourceNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
