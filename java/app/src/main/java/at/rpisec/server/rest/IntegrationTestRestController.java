package at.rpisec.server.rest;

import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.model.ClientId;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@RestController
@RequestMapping("/test")
@ConditionalOnProperty(name = "test.integration.rest.api.enabled", havingValue = "true")
@Validated
public class IntegrationTestRestController {

    @Autowired
    private ClientRepository clientRepo;

    @GetMapping("/alive")
    public boolean alive() {
        return true;
    }

    @PostMapping("/prepare")
    public void clear(final @NotEmpty @RequestParam(ClientRestConstants.PARAM_DEVICE_ID) String deviceId,
                      final @NotNull @Min(1) @RequestParam(ClientRestConstants.PARAM_USER_ID) Long userId) {
        // Delete all registered clients
        clientRepo.deleteAll();

        // Create new client for testing
        clientRepo.save(new Client(new ClientId(deviceId, userId)));
    }
}
