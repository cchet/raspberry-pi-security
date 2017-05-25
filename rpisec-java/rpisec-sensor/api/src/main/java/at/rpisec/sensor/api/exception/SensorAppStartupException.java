package at.rpisec.sensor.api.exception;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
public class SensorAppStartupException extends SensorAppException {

    public SensorAppStartupException() {
    }

    public SensorAppStartupException(String message) {
        super(message);
    }

    public SensorAppStartupException(String message,
                                     Throwable cause) {
        super(message, cause);
    }

    public SensorAppStartupException(Throwable cause) {
        super(cause);
    }

    public SensorAppStartupException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
