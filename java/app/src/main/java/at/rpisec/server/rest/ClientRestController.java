package at.rpisec.server.rest;

import at.rpisec.server.Application;
import at.rpisec.server.logic.api.IClientLogic;
import at.rpisec.server.logic.api.IIncidentLogic;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(ClientRestConstants.BASE_URI)
@Validated
public class ClientRestController {

    @Autowired
    private IClientLogic clientLogic;
    @Autowired
    private IIncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    @PutMapping("/notify")
    public void notifyTest() throws IOException {
        final byte[] data = IOUtils.toByteArray(Application.class.getResourceAsStream("/giraffe.jpg"));
        incidentLogic.logIncidentWithImage(data, "jpg");
    }
}
