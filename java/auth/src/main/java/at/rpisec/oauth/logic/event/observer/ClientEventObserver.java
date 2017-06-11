package at.rpisec.oauth.logic.event.observer;

import at.rpisec.oauth.logic.event.ClientCreatedEvent;
import at.rpisec.oauth.logic.event.ClientFcmTokenRegisteredEvent;
import at.rpisec.oauth.logic.event.ClientRemovedEvent;
import at.rpisec.swagger.client.app.client.api.InternalRestControllerApi;
import at.rpisec.swagger.client.app.client.invoker.ApiCallback;
import at.rpisec.swagger.client.app.client.invoker.ApiException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

/**
 * Observer class for observing client related events.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
@Component
public class ClientEventObserver {

    @Autowired
    private InternalRestControllerApi appServerInternalClient;

    @Autowired
    private Logger log;

    /**
     * Unregisters the created client device on the app server via its hosted rest api.
     *
     * @param event the event holding the request data
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientRemovedEvent(final ClientRemovedEvent event) {
        try {
            appServerInternalClient.unregisterClientSUsingPOSTAsync(event.getDeviceIds(), event.getUserId(), new ApiCallback<Void>() {
                @Override public void onFailure(ApiException e,
                                                int statusCode,
                                                Map<String, List<String>> responseHeaders) {
                    log.error("Could not unregister client devices of user. code: {} /  devices: {} / user: {}", statusCode, String.join(", ", event.getDeviceIds()), event.getUserId());
                }

                @Override public void onSuccess(Void result,
                                                int statusCode,
                                                Map<String, List<String>> responseHeaders) {
                    log.error("Successfully unregistered client devices of user. devices: {} / user: {}", String.join(", ", event.getDeviceIds()), event.getUserId());
                }

                @Override public void onUploadProgress(long bytesWritten,
                                                       long contentLength,
                                                       boolean done) { }

                @Override public void onDownloadProgress(long bytesRead,
                                                         long contentLength,
                                                         boolean done) { }
            });
        } catch (ApiException e) {
            log.error("Could not unregister client devices of user.", e);
        }
    }

    /**
     * Registers the created client device on the app server via its hosted rest api.
     *
     * @param event the event holding the request data
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientCreatedEvent(final ClientCreatedEvent event) {
        try {
            appServerInternalClient.registerClientUsingPOSTAsync(event.getDeviceId(), event.getUserId(), new ApiCallback<Void>() {
                @Override public void onFailure(ApiException e,
                                                int statusCode,
                                                Map<String, List<String>> responseHeaders) {
                    log.error("Could not register client device of user. code: {} /  device: {} / user: {}", statusCode, event.getDeviceId(), event.getUserId());
                }

                @Override public void onSuccess(Void result,
                                                int statusCode,
                                                Map<String, List<String>> responseHeaders) {
                    log.error("Successfully registered client device of user. code: {} /  device: {} / user: {}", statusCode, event.getDeviceId(), event.getUserId());
                }

                @Override public void onUploadProgress(long bytesWritten,
                                                       long contentLength,
                                                       boolean done) { }

                @Override public void onDownloadProgress(long bytesRead,
                                                         long contentLength,
                                                         boolean done) { }
            });
        } catch (ApiException e) {
            log.error("Could not register client devices of user.", e);
        }
    }

    /**
     * Registers the client device fcm token on the app server via its hosted rest api.
     *
     * @param event the event holding the request data
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientFcmTokenRegisteredEvent(final ClientFcmTokenRegisteredEvent event) {
        try {
            appServerInternalClient.registerFCMTokenUsingPOSTAsync(event.getDeviceId(), event.getFcmToken(), event.getUserId(), new ApiCallback<Void>() {
                @Override public void onFailure(ApiException e,
                                                int statusCode,
                                                Map<String, List<String>> responseHeaders) {
                    log.error("Could not register fcm token for client device of user. code: {} / fcmToken: {} / device: {} / user: {}",
                              statusCode,
                              event.getFcmToken(),
                              event.getDeviceId(),
                              event.getUserId());
                }

                @Override public void onSuccess(Void result,
                                                int statusCode,
                                                Map<String, List<String>> responseHeaders) {
                    log.error("Successfully registered fcm token for client device of user. code: {} / fcmToken: {} / device: {} / user: {}",
                              statusCode,
                              event.getFcmToken(),
                              event.getDeviceId(),
                              event.getUserId());
                }

                @Override public void onUploadProgress(long bytesWritten,
                                                       long contentLength,
                                                       boolean done) { }

                @Override public void onDownloadProgress(long bytesRead,
                                                         long contentLength,
                                                         boolean done) { }
            });
        } catch (ApiException e) {
            log.error("Could not register fcmToken.", e);
        }
    }
}
