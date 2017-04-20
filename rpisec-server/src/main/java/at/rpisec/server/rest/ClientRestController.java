package at.rpisec.server.rest;

import at.rpisec.server.rest.model.TokenResponse;
import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
@RestController()
@RequestMapping("/api/client")
public class ClientRestController {

    @Autowired
    private FirebaseAuth firebaseAuth;
    @Autowired
    private Logger log;

    @GetMapping("/token/{uuid}")
    public DeferredResult<TokenResponse> clientToken(final @PathVariable String uuid,
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
}
