package at.rpisec.server.rest;

import at.rpisec.server.Application;
import at.rpisec.server.logic.api.IClientLogic;
import at.rpisec.server.logic.api.IIncidentLogic;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(ClientRestConstants.BASE_URI)
public class ClientRestController {

    @Autowired
    private IClientLogic clientLogic;
    @Autowired
    private IIncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    //region For Testing notifications
    @PutMapping("/notify")
    public void notifyTest() throws IOException {
        final byte[] data = IOUtils.toByteArray(Application.class.getResourceAsStream("/giraffe.jpg"));
        incidentLogic.logIncidentWithImage(data, "jpg");
    }
    //endregion

    @PostMapping(value = ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerFCMToken(final @RequestParam(ClientRestConstants.PARAM_DEVICE_ID) String deviceId,
                                 final @RequestParam(ClientRestConstants.PARAM_FCM_TOKEN) String fcmToken,
                                 final @RequestParam(ClientRestConstants.PARAM_USER_ID) Long userId) {
        clientLogic.registerFcmToken(fcmToken, deviceId, userId);

        log.info("FCM token successfully registered for client. deviceId:{} / token:{}", deviceId, fcmToken);
    }

    @PostMapping(value = ClientRestConstants.REL_URI_REGISTER, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerClient(final @RequestParam(ClientRestConstants.PARAM_DEVICE_ID) String deviceId,
                               final @RequestParam(ClientRestConstants.PARAM_USER_ID) Long userId) {
        clientLogic.register(deviceId, userId);

        log.info("User client device registered. deviceId={} / userId={}", deviceId, userId);
    }

    @PostMapping(value = ClientRestConstants.REL_URI_UNREGISTER, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void unregisterClientS(final @RequestParam(ClientRestConstants.PARAM_DEVICE_ID) List<String> deviceIds,
                                  final @RequestParam(ClientRestConstants.PARAM_USER_ID) Long userId) {
        clientLogic.unregister(deviceIds, userId);

        log.info("User client devices unregistered. deviceIds={}", String.join(",", deviceIds));
    }
}
