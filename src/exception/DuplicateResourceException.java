package exception;

public class DuplicateResourceException extends Exception {

    public DuplicateResourceException(String errorMessage){
        super(errorMessage);
    }
}
