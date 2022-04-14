package exception;

/*
 * Signals that a resource already exists.
 */
public class DuplicateResourceException extends Exception {

	/*
	 * Creates a DuplicateResourceException with the specified detail message.
	 */
    public DuplicateResourceException(String errorMessage){
        super(errorMessage);
    }
}
