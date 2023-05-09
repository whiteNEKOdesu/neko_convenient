package neko.convenient.nekoconvenientcommonbase.utils.exception;

public class OrderOverTimeException extends RuntimeException {
    public OrderOverTimeException(){

    }

    public OrderOverTimeException(String message) {
        super(message);
    }
}
