package neko.convenient.nekoconvenientcommonbase.utils.exception;

public class MailSendException extends RuntimeException {
    public MailSendException(){

    }

    public MailSendException(String message) {
        super(message);
    }
}
