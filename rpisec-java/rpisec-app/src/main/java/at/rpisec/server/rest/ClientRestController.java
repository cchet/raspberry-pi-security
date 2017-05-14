package at.rpisec.server.rest;

import at.rpisec.server.Application;
import at.rpisec.server.logic.api.ClientLogic;
import at.rpisec.server.logic.api.IncidentLogic;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(ClientRestConstants.BASE_URI)
public class ClientRestController {

    @Autowired
    private ClientLogic clientLogic;
    @Autowired
    private IncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    //region For Testing notifications
    @PutMapping("/notify")
    public void notifyTest() throws IOException {
        final byte[] data = IOUtils.toByteArray(Application.class.getResourceAsStream("/giraffe.jpg"));
        incidentLogic.logIncidentWithImage(data, "jpg");
    }
    //endregion

    @PutMapping(ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
    public void registerFCMToken(final @RequestParam(ClientRestConstants.PARAM_CLIENT_ID) String uuid,
                                 final @RequestParam(ClientRestConstants.PARAM_FCM_TOKEN) String fcmToken) {
        clientLogic.registerFcmToken(fcmToken, uuid);
        log.info("FCM token successfully registered for client. client-clientId:{} / token:{}", uuid, fcmToken);
    }
}
