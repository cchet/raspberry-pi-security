package at.rpisec.app.rest.controller;

import at.rpisec.app.jpa.model.Client;
import at.rpisec.app.jpa.model.ClientId;
import at.rpisec.app.jpa.repositories.ClientRepository;
import at.rpisec.shared.rest.constants.AppRestConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This class represents the rest interface for the integration tests which is only active if the 'integrationTest' profile is active.
 *
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
    public void clear(final @NotEmpty @RequestParam(AppRestConstants.PARAM_DEVICE_ID) String deviceId,
                      final @NotNull @Min(1) @RequestParam(AppRestConstants.PARAM_USER_ID) Long userId) {
        // Delete all registered clients
        clientRepo.deleteAll();

        // Create new client for testing
        clientRepo.save(new Client(new ClientId(deviceId, userId)));
    }
}
