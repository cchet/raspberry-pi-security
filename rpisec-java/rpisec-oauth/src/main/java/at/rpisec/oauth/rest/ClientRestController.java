package at.rpisec.oauth.rest;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.TokenResponse;
import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

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
    private ClientDetailsService clientDetails;
    @Autowired
    private Logger log;

    @GetMapping(value = ClientRestConstants.REL_URI_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<TokenResponse> token(@RequestParam(ClientRestConstants.PARAM_CLIENT_ID) final String clientId,
                                               final Authentication auth) {
        final DeferredResult<TokenResponse> asyncResult = new DeferredResult<>();

        try {
            clientDetails.loadClientByClientId(clientId);
            firebaseAuth.createCustomToken(UUID.randomUUID().toString())
                        .addOnFailureListener((exception) -> {
                            log.error("Token creation failed for client with clientId '{}'", clientId, exception);
                            asyncResult.setResult(new TokenResponse(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ClientRestConstants.PATTERN_DATE_TIME)),
                                                                    "?",
                                                                    exception.getClass().getName()));
                        })
                        .addOnSuccessListener((token) -> {
                            log.info("Token successfully created. token: {} / username: {} / client: {}", token, auth.getPrincipal().toString(), clientId);
                            asyncResult.setResult(new TokenResponse(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ClientRestConstants.PATTERN_DATE_TIME)), token, null));
                        });

        } catch (NoSuchClientException e) {
            log.warn("Client with client_id={} for user={} not found", clientId, auth.getPrincipal().toString());
            asyncResult.setResult(new TokenResponse(DateTimeFormatter.ofPattern(ClientRestConstants.PATTERN_DATE_TIME).format(LocalDateTime.now()), "invalid", "Client not found"));
        }

        return asyncResult;
    }
}
