package cl.gd.kt.leg.exception;

import lombok.ToString;

@ToString
public class AppException extends Exception {

    /**
     * TODO: throw correctly the error code with the message, now just the message is right and appears in both fields: message & error
     */
    private static final int CODE_INTERNAL_ERROR = 500;
    private final int code;


    public AppException(final String msg) {
        super(msg);
        this.code = CODE_INTERNAL_ERROR;

    }

    public int getCode() {
        return code;
    }
}
