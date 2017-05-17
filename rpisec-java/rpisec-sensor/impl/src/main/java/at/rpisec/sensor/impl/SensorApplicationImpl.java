package at.rpisec.sensor.impl;

import at.rpisec.sensor.api.IncidentImageReportListener;
import at.rpisec.sensor.api.SensorApplication;

/**
 * Git does not allow empty directories
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public class SensorApplicationImpl implements SensorApplication {

    @Override
    public void register(IncidentImageReportListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(IncidentImageReportListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }
}
