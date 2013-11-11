package be.comicsdownloader.model;

public class CrashException extends RuntimeException {

    public CrashException(String message) {
        super(message);
    }

    public CrashException(Throwable cause) {
        super(cause);
    }

    public CrashException(String message, Throwable cause) {
        super(message, cause);
    }
}
