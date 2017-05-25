package at.rpisec.sensor.api.exception;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
public class SensorAppException extends Exception {

    public SensorAppException() {
    }

    public SensorAppException(String message) {
        super(message);
    }

    public SensorAppException(String message,
                              Throwable cause) {
        super(message, cause);
    }

    public SensorAppException(Throwable cause) {
        super(cause);
    }

    public SensorAppException(String message,
                              Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
