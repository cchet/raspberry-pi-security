package at.rpisec.server.rest;

import at.rpisec.server.logic.api.ClientLogic;
import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.UserRestConstants;
import at.rpisec.server.shared.rest.model.TokenResponse;
import at.rpisec.server.shared.rest.model.UserDto;
import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
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
    private Logger log;

    @GetMapping(ClientRestConstants.REL_URI_TOKEN)
    public DeferredResult<TokenResponse> token(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                                               final Authentication auth) {
        final DeferredResult<TokenResponse> asyncResult = new DeferredResult<>();

        firebaseAuth.createCustomToken(UUID.randomUUID().toString())
                    .addOnFailureListener((exception) -> {
                        log.error("Token creation failed.", exception);
                        asyncResult.setResult(new TokenResponse("?", exception.getClass().getName()));
                    })
                    .addOnSuccessListener((token) -> {
                        log.info("Token successfully created.\ntoken: " + token + "\nclient: " + auth.getPrincipal().toString());
                        asyncResult.setResult(new TokenResponse(token));
                    });

        return asyncResult;
    }

    @PutMapping(ClientRestConstants.REL_URI_REGISTER)
    public void register(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                         final Authentication auth) {
        clientLogic.create(uuid, auth.getPrincipal().toString());
    }

    @PutMapping(ClientRestConstants.REL_URI_UNREGISTER)
    public void unregister(final @RequestParam(ClientRestConstants.PARAM_UUID) String uuid,
                           final Authentication auth) {
        clientLogic.delete(uuid, auth.getPrincipal().toString());
    }

    @PutMapping(value = UserRestConstants.REL_URI_UPDATE)
    public UserDto update(@RequestBody @Valid UserDto model) {
        userLogic.update(model);
        return userLogic.byId(model.getId());
    }
}
