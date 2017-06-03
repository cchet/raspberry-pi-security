package at.rpisec.server.shared.rest.constants;

/**
 * This class specifies the oauth api and related constants.
 *
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class OAuthConstants {

    public OAuthConstants() {
    }

    public static final String BASE_URI = "/oauth";
    public static final String REL_URI_TOKEN = "/token";
    public static final String REL_URI_TOKEN_CHECK = "/check_token";

    // dynamic params
    public static final String REL_URI_TOKEN_CHECK_PARAMS = "?token=%s";

    // static params
    public static final String REL_URI_TOKEN_PARAMS = "?grant_type=password&scope=write";

    // oauth-server-get-access-token
    public static final String URI_TOKEN_REQUEST = BASE_URI + REL_URI_TOKEN + REL_URI_TOKEN_PARAMS;

    // oauth-server-check-access-token
    public static final String URI_TOKEN_CHECK = BASE_URI + REL_URI_TOKEN_CHECK + REL_URI_TOKEN_CHECK_PARAMS;
}
