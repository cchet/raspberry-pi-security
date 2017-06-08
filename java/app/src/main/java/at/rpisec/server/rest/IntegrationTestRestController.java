package at.rpisec.server.rest;

import at.rpisec.server.jpa.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@RestController
@RequestMapping("/api/system")
@ConditionalOnProperty(name = "system.api.alive.enabled", havingValue = "true")
public class IntegrationTestRestController {

    @Autowired
    private ClientRepository clientRepo;

    @GetMapping("/alive")
    public boolean alive(){
        return true;
    }

    @PostMapping("/prepare")
    public void clear() {
        clientRepo.deleteAll();
    }
}
