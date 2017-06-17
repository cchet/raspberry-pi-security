package at.rpisec.auth.rest.controller;

import at.rpisec.auth.logic.api.ClientLogic;
import at.rpisec.auth.rest.model.TokenResponse;
import at.rpisec.shared.rest.constants.AuthRestConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * This class represents the rest interface for the clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(AuthRestConstants.CLIENT_REST_API_BASE)
@Validated
public class ClientRestController {

    @Autowired
    private ClientLogic clientLogic;
    @Autowired
    private Logger log;

    @GetMapping(value = AuthRestConstants.REL_CLIENT_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse login(final @NotEmpty(message = "deviceId must not be null or empty") @RequestParam(AuthRestConstants.PARAM_DEVICE_ID) String deviceId,
                               final Authentication auth) {
        final TokenResponse response = clientLogic.loginClient(auth.getPrincipal().toString(), deviceId);

        log.info("User client registered. client_id={} / username={}", response.getClientId(), auth.getPrincipal().toString());

        return response;
    }

    @PutMapping(AuthRestConstants.REL_URI_REGISTER_FCM_TOKEN)
    public void registerFCMToken(final @NotEmpty(message = "deviceId must not be null or empty") @RequestParam(AuthRestConstants.PARAM_DEVICE_ID) String deviceId,
                                 final @NotEmpty(message = "fcmToken must not be null or empty") @RequestParam(AuthRestConstants.PARAM_FCM_TOKEN) String fcmToken,
                                 final Authentication auth) {
        clientLogic.registerFcmToken(auth.getPrincipal().toString(), deviceId, fcmToken);

        log.info("FCM token successfully registered for client device. username={} / device={} / fcmToken={}", auth.getPrincipal().toString(), deviceId, fcmToken);
    }
}
