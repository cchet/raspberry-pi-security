package at.rpisec.server.rest;

import at.rpisec.server.config.ConfigurationDev;
import at.rpisec.server.logic.api.ClientLogic;
import at.rpisec.server.logic.api.IncidentLogic;
import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.TokenResponse;
import at.rpisec.server.shared.rest.model.UserDto;
import com.google.firebase.auth.FirebaseAuth;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(ClientRestConstants.BASE_URI)
public class ClientRestController {

    @Autowired
    private FirebaseAuth firebaseAuth;
    @Autowired
    private ClientLogic clientLogic;
    @Autowired
    private UserLogic userLogic;
    @Autowired
    private IncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    //region For Testing notifications
    @PutMapping("/notify")
    public void notifyTest() throws IOException {
        final byte[] data = IOUtils.toByteArray(ConfigurationDev.class.getResourceAsStream("/giraffe.jpg"));
        incidentLogic.logIncidentWithImage(data, "jpg");
    }
    //endregion

    @GetMapping(ClientRestConstants.REL_URI_TOKEN)
    public DeferredResult<TokenResponse> token(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                                               final Authentication auth) {
        final DeferredResult<TokenResponse> asyncResult = new DeferredResult<>();
        clientLogic.checkIfClientExists(uuid, auth.getPrincipal().toString());

        firebaseAuth.createCustomToken(UUID.randomUUID().toString())
                    .addOnFailureListener((exception) -> {
                        log.error("Token creation failed for client with uuid '{}'", uuid, exception);
                        asyncResult.setResult(new TokenResponse(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ClientRestConstants.PATTERN_DATE_TIME)), "?", exception.getClass().getName()));
                    })
                    .addOnSuccessListener((token) -> {
                        log.info("Token successfully created. token: {} / username: {} / client: {}", token, auth.getPrincipal().toString(), uuid);
                        asyncResult.setResult(new TokenResponse(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ClientRestConstants.PATTERN_DATE_TIME)), token, null));
                    });

        return asyncResult;
    }

    @PutMapping(ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
    public void registerFCMToken(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                                 final @RequestParam(ClientRestConstants.PARAM_FCM_TOKEN) String fcmToken,
                                 final Authentication auth) {
        clientLogic.registerFcmToken(fcmToken, uuid, auth.getPrincipal().toString());
        log.info("FCM token successfully registered for client. client-uuid:{} / token:{}", uuid, fcmToken);
    }

    @PutMapping(ClientRestConstants.REL_URI_REGISTER)
    public void registerClient(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                               final Authentication auth) {
        clientLogic.register(uuid, auth.getPrincipal().toString());
    }

    @DeleteMapping(ClientRestConstants.REL_URI_UNREGISTER)
    public void unregisterClient(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                                 final Authentication auth) {
        clientLogic.unregister(uuid, auth.getPrincipal().toString());
    }

    @PostMapping(value = ClientRestConstants.REL_URI_PROFILE + "/{" + ClientRestConstants.PARAM_UUID + "}")
    public UserDto updateProfile(final @RequestBody @Valid UserDto model,
                                 final @PathVariable(ClientRestConstants.PARAM_UUID) String uuid,
                                 final Authentication auth) {
        final Long id = clientLogic.updateProfile(uuid, auth.getPrincipal().toString(), model);

        return userLogic.byId(id);
    }

    @PostMapping(value = ClientRestConstants.REL_URI_PASSWORD)
    public void updatePassword(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                               final @RequestParam(ClientRestConstants.PARAM_PASSWORD) String password,
                               final Authentication auth) {
        clientLogic.updatePassword(uuid, auth.getPrincipal().toString(), password);
    }
}
