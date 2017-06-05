package at.rpisec.oauth.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@RestController
@RequestMapping("/api/system")
@ConditionalOnProperty(name = "system.api.alive.enabled", havingValue = "true")
public class AliveRestController {

    @GetMapping("/alive")
    public boolean alive() {
        return true;
    }
}
