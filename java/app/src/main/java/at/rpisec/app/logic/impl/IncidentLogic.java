package at.rpisec.app.logic.impl;

import at.rpisec.app.config.FirebaseConfiguration;
import at.rpisec.app.config.SecurityConfiguration;
import at.rpisec.app.jpa.projection.IClientFirebaseToken;
import at.rpisec.app.jpa.repositories.ClientRepository;
import at.rpisec.app.logic.api.IIncidentLogic;
import at.rpisec.shared.rest.constants.FirebaseConstants;
import at.rpisec.shared.rest.model.FirebaseDatabaseItem;
import at.rpisec.shared.rest.model.FirebaseMessage;
import at.rpisec.shared.rest.model.FirebaseMessageResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Service
public class IncidentLogic implements IIncidentLogic {

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private FirebaseDatabase database;
    @Autowired
    @Qualifier(FirebaseConfiguration.REST_TEMPLATE_FCM)
    private RestTemplate fcmRestTemplate;
    @Autowired
    @Qualifier(FirebaseConfiguration.FCM_URL)
    private String fcmSendUrl;
    @Autowired
    @Qualifier(SecurityConfiguration.INCIDENT_IMAGE_LOCATION)
    private String imageLocation;
    @Autowired
    private MessageSource messages;
    @Autowired
    private Logger log;

    @Override
    public void logIncidentWithImage(final byte[] image,
                                     final String extension) {
        log.debug("Logging reported incident with image");
        final Task<Void> task = logIncidentWithImageAsync(image, extension);
        try {
            Tasks.await(task);
        } catch (Exception e) {
            log.error("Waiting for firebase task failed with an error", e);
        }
        log.debug("Logged reported incident with image");
    }

    @Override
    public Task<Void> logIncidentWithImageAsync(byte[] image,
                                                String extension) {
        Objects.requireNonNull(image, "Image must be given");
        Objects.requireNonNull(extension, "Image extension must not be null");
        if (extension.trim().isEmpty()) {
            throw new IllegalArgumentException("DeviceId must not be empty");
        }

        log.debug("Logging async reported incident with image");
        final Locale locale = Locale.ENGLISH;
        final String base64Data = Base64.getEncoder().encodeToString(image);
        final String occurringDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FirebaseConstants.DATE_TIME_FORMAT_PATTERN));
        final FirebaseDatabaseItem item = new FirebaseDatabaseItem(messages.getMessage("incident.db.message", new Object[]{occurringDate}, locale), false, extension, base64Data);

        final DatabaseReference ref = database.getReference(FirebaseConstants.DB_ITEM_INCIDENT);
        Task<Void> task = ref.child(occurringDate)
                             .setValue(item)
                             .addOnFailureListener((exception) -> log.error("Could not report incident to firebase database ", exception))
                             .addOnSuccessListener((var) -> log.info("Successfully reported incident to firebase database '{}'", occurringDate));

        // Send notifications to all known client apps
        final List<IClientFirebaseToken> tokens = clientRepo.findDistinctByFcmTokenIsNotNull();
        if (!tokens.isEmpty()) {
            for (final IClientFirebaseToken token : tokens) {
                final FirebaseMessage.FirebaseMessageBody notificationBody = new FirebaseMessage.FirebaseMessageBody(messages.getMessage("incident.fcm.title", null, locale),
                                                                                                                     messages.getMessage("incident.fcm.message", null, locale),
                                                                                                                     occurringDate);
                final FirebaseMessage notification = new FirebaseMessage(token.getToken(), notificationBody);
                ResponseEntity<FirebaseMessageResponse> result = fcmRestTemplate.postForEntity(fcmSendUrl, notification, FirebaseMessageResponse.class);
                if ((result.hasBody()) && (result.getBody().getFailure())) {
                    log.error("Notification failed for error: {}", result.getBody());
                } else {
                    log.info("Notification successfully send: fcmToken: {}", token.getToken());
                }
            }
        }

        // Save file to filesystem
        try {
            Files.createFile(Paths.get(imageLocation + occurringDate + "." + extension));
        } catch (Exception e) {
            log.error("Could not save file to filesystem", e);
        }
        log.debug("Logged async reported incident with image");

        return task;
    }
}
