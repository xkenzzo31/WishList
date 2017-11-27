package core;

/**
 * Created by lucas on 24/11/2017.
 */

public class Error extends Exception{
    private enum ErrorType {
        NO_INTERNET_CONNECTION,

    }

    ErrorType errorType;

    public Error(String message) {
        super(message);
    }

    public Error(String message, Throwable cause) {
        super(message, cause);
    }

    public Error(Throwable cause) {
        super(cause);
    }
}
