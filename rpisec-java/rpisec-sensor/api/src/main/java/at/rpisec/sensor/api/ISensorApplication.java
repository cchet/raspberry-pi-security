package at.rpisec.sensor.api;

import at.rpisec.sensor.api.config.ISensorApplicationConfiguration;
import at.rpisec.sensor.api.exception.SensorAppShutdownException;
import at.rpisec.sensor.api.exception.SensorAppStartupException;

/**
 * Interface for application specification.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public interface ISensorApplication {

    void register(IncidentImageObserver listener);

    void remove(IncidentImageObserver listener);

    void start(ISensorApplicationConfiguration config) throws SensorAppStartupException;

    void shutdown() throws SensorAppShutdownException;

    boolean isRunning();

    ISensorApplicationConfiguration getCurrentConfiguration();
}
