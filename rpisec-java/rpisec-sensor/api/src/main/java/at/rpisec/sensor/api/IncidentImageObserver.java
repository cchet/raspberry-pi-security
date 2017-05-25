package at.rpisec.sensor.api;

/**
 * Specifies a incident listener.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/17/17
 */
@FunctionalInterface
public interface IncidentImageObserver {

    void handle(byte[] image);
}
