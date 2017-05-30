package at.rpisec.oauth.rest;

import at.rpisec.oauth.logic.api.ClientLogic;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.TokenResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(ClientRestConstants.BASE_URI)
public class ClientRestController {

    @Autowired
    private ClientLogic clientLogic;
    @Autowired
    private Logger log;

    @GetMapping(value = ClientRestConstants.REL_CLIENT_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse login(@RequestParam(ClientRestConstants.PARAM_DEVICE_ID) final String deviceId,
                               final Authentication auth) {
        final TokenResponse response = clientLogic.loginClient(auth.getPrincipal().toString(), deviceId);

        log.info("User client registered. client_id={} / username={}", response.getClientId(), auth.getPrincipal().toString());

        return response;
    }

    @PutMapping(ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
    public void registerFCMToken(final @RequestParam(ClientRestConstants.PARAM_DEVICE_ID) String deviceId,
                                 final @RequestParam(ClientRestConstants.PARAM_FCM_TOKEN) String fcmToken,
                                 final Authentication auth) {
        clientLogic.registerFcmToken(auth.getPrincipal().toString(), deviceId, fcmToken);

        log.info("FCM token successfully registered for client device. username={} / device={} / fcmToken={}", auth.getPrincipal().toString(), deviceId, fcmToken);
    }
}
