package at.rpisec.sensor.api;

/**
 * Interface for application specification.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public interface SensorApplication {

    void register(IncidentImageReportListener listener);

    void remove(IncidentImageReportListener listener);

    void start();

    void shutdown();
}
