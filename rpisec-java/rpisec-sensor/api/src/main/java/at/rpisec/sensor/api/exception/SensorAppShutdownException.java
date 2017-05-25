package at.rpisec.sensor.api.exception;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
public class SensorAppShutdownException extends SensorAppException {

    public SensorAppShutdownException() {
    }

    public SensorAppShutdownException(String message) {
        super(message);
    }

    public SensorAppShutdownException(String message,
                                      Throwable cause) {
        super(message, cause);
    }

    public SensorAppShutdownException(Throwable cause) {
        super(cause);
    }

    public SensorAppShutdownException(String message,
                                      Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
