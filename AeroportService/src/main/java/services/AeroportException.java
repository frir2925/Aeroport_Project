package services;
public class AeroportException extends Exception{
    public AeroportException() {
    }

    public AeroportException(String message) {
        super(message);
    }

    public AeroportException(String message, Throwable cause) {
        super(message, cause);
    }
}
