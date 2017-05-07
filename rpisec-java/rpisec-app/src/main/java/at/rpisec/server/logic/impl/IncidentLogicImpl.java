package at.rpisec.server.logic.impl;

import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.projection.ClientFirebaseToken;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.logic.api.IncidentLogic;
import at.rpisec.server.shared.rest.constants.FirebaseConstants;
import at.rpisec.server.shared.rest.model.FirebaseDatabaseItem;
import at.rpisec.server.shared.rest.model.FirebaseMessage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Service
public class IncidentLogicImpl implements IncidentLogic {

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private FirebaseDatabase database;
    @Autowired
    @Qualifier("fcmRestTemplate")
    private RestTemplate fcmRestTemplate;
    @Autowired
    @Qualifier("fcmSendUrl")
    private String fcmSendUrl;
    @Autowired
    private MessageSource messages;
    @Autowired
    private Logger log;

    @Override
    public void logIncidentWithImage(final byte[] image,
                                     final String extension) {

        final Task<Void> task = logIncidentWithImageAsync(image, extension);
        Tasks.whenAll(task);
    }

    @Override
    public Task<Void> logIncidentWithImageAsync(byte[] image,
                                                String extension) {
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
        final List<ClientFirebaseToken> tokens = clientRepo.findDistinctByFirebaseTokenIsNotNull();
        if (!tokens.isEmpty()) {
            for (final ClientFirebaseToken token : tokens) {
                final FirebaseMessage.FirebaseMessageBody notificationBody = new FirebaseMessage.FirebaseMessageBody(messages.getMessage("incident.fcm.title", null, locale),
                                                                                                                     messages.getMessage("incident.fcm.message", null, locale),
                                                                                                                     occurringDate);
                final FirebaseMessage notification = new FirebaseMessage(token.getToken(), notificationBody);
                fcmRestTemplate.postForLocation(fcmSendUrl, notification);
            }
        }

        return task;
    }
}
