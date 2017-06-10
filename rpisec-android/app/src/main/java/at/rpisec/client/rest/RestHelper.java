package at.rpisec.client.rest;

import android.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.util.Map;

import at.rpisec.client.util.RpiSecConstants;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public final class RestHelper {

    public static HttpEntity<Object> getHttpEntity(String userName, String password) {
        return getHttpEntity(userName, password, MediaType.APPLICATION_JSON);
    }

    public static HttpEntity<Object> getHttpEntity(String userName, String password, MediaType mediaType) {
        return getHttpEntity(userName, password, mediaType, null, null);
    }

    public static HttpEntity<Object> getHttpEntity(String userName, String password, MediaType mediaType, Map<String, String> additionalHeaderData) {
        return getHttpEntity(userName, password, mediaType, additionalHeaderData, null);
    }

    public static HttpEntity<Object> getHttpEntity(String userName, String password, MediaType mediaType, Map<String, String> additionalHeaderData, Map<String, String> additionalBodyData) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = null;

        final String authStr = Base64.encodeToString((userName + ":" + password).getBytes(Charset.forName(RpiSecConstants.CREDENTIAL_CHARSET)), Base64.URL_SAFE);

        headers.add(RpiSecConstants.OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_NAME, RpiSecConstants.OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_TYPE + authStr);
        headers.setContentType(mediaType);

        if (additionalHeaderData != null && !additionalHeaderData.isEmpty()) {
            for (Map.Entry<String, String> entry : additionalHeaderData.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }

        if (additionalBodyData != null && !additionalBodyData.isEmpty()) {
            body = new LinkedMultiValueMap<String, String>();

            for (Map.Entry<String, String> entry : additionalBodyData.entrySet()) {
                body.add(RpiSecConstants.MULTIVALUEMAP_FIELD_KEY, entry.getKey());
                body.add(RpiSecConstants.MULTIVALUEMAP_FIELD_VALUE, entry.getValue());
            }
        }

        return new HttpEntity<>(body, headers);
    }
}
