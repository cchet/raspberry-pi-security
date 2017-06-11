package at.rpisec.server.rest.controller;

import at.rpisec.server.logic.api.IClientLogic;
import at.rpisec.server.logic.api.IIncidentLogic;
import at.rpisec.server.shared.rest.constants.AppRestConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(AppRestConstants.INTERNAL_REST_API_BASE)
@Validated
public class InternalRestController {

    @Autowired
    private IClientLogic clientLogic;
    @Autowired
    private IIncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    @PostMapping(value = AppRestConstants.REL_URI_REGISTER_FCM_TOKEN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerFCMToken(final @NotEmpty(message = "deviceId must not be null or empty") @RequestParam(AppRestConstants.PARAM_DEVICE_ID) String deviceId,
                                 final @NotEmpty(message = "fcmToken must not be null or empty") @RequestParam(AppRestConstants.PARAM_FCM_TOKEN) String fcmToken,
                                 final @NotNull(message = "userId must not be null") @Min(value = 1, message = "userId must be greater than 0") @RequestParam(AppRestConstants.PARAM_USER_ID) Long userId) {
        clientLogic.registerFcmToken(fcmToken, deviceId, userId);

        log.info("FCM token successfully registered for client. deviceId:{} / token:{}", deviceId, fcmToken);
    }

    @PostMapping(value = AppRestConstants.REL_URI_REGISTER, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerClient(final @NotEmpty(message = "deviceId must not be null or empty") @RequestParam(AppRestConstants.PARAM_DEVICE_ID) String deviceId,
                               final @NotNull(message = "userId must not be null") @Min(value = 1, message = "userId must be greater than 0") @RequestParam(AppRestConstants.PARAM_USER_ID) Long userId) {
        clientLogic.register(deviceId, userId);

        log.info("User client device registered. deviceId={} / userId={}", deviceId, userId);
    }

    @PostMapping(value = AppRestConstants.REL_URI_UNREGISTER, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void unregisterClientS(final @NotEmpty(message = "deviceId must not be null or empty") @RequestParam(AppRestConstants.PARAM_DEVICE_ID) List<String> deviceIds,
                                  final @NotNull(message = "userId must not be null") @Min(value = 1, message = "userId must be greater than 0") @RequestParam(AppRestConstants.PARAM_USER_ID) Long userId) {
        clientLogic.unregister(deviceIds, userId);

        log.info("User client devices unregistered. deviceIds={}", String.join(",", deviceIds));
    }
}
