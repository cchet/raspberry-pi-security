package at.rpisec.app.rest.controller;

import at.rpisec.app.Application;
import at.rpisec.app.logic.api.IClientLogic;
import at.rpisec.app.logic.api.IIncidentLogic;
import at.rpisec.shared.rest.constants.AppRestConstants;
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
@RequestMapping(AppRestConstants.CLIENT_REST_API_BASE)
@Validated
public class ClientRestController {

    @Autowired
    private IClientLogic clientLogic;
    @Autowired
    private IIncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    @PutMapping(AppRestConstants.REL_URI_CAPTURE)
    public void notifyTest() throws IOException {
        final byte[] data = IOUtils.toByteArray(Application.class.getResourceAsStream("/giraffe.jpg"));
        incidentLogic.logIncidentWithImageAsync(data, "jpg");
    }
}
