package at.rpisec.app.logic.api;

import com.google.firebase.tasks.Task;

import java.io.Serializable;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public interface IIncidentLogic extends Serializable {

    void logIncidentWithImage(byte[] image,
                              String extension);

    Task<Void> logIncidentWithImageAsync(byte[] image,
                                         String extension);
}
