package at.rpisec.app.logic.api;

import com.google.firebase.tasks.Task;

import java.io.Serializable;

/**
 * This interface specifies the incident related business logic.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public interface IIncidentLogic extends Serializable {

    /**
     * Logs an incident with the given image synchronously.
     *
     * @param image     the image of the incident
     * @param extension the file extension of the given image
     * @see IIncidentLogic#logIncidentWithImageAsync(byte[], String)
     */
    void logIncidentWithImage(byte[] image,
                              String extension);

    /**
     * Logs an incident with the given image asynchronously.
     *
     * @param image     the image of the incident
     * @param extension the file extension of the given image
     */
    Task<Void> logIncidentWithImageAsync(byte[] image,
                                         String extension);
}
