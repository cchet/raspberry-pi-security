package at.rpisec.client.util;

/**
 * Created by Philipp Wurm on 10.06.2017.
 */

public final class RpiSecConstants {
    public static final String OAUTH_CREDENTIAL_HEADER_CONTENT_TYPE_NAME = "Content-Type";
    public static final String OAUTH_CREDENTIAL_HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    public static final String OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_NAME = "Authorization";
    public static final String OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_TYPE = "Basic "; //!!! trailing whitespace is important !!!

    public static final String MULTIVALUEMAP_FIELD_KEY = "key";
    public static final String MULTIVALUEMAP_FIELD_VALUE = "value";

    public static final String CREDENTIAL_CHARSET = "US-ASCII";

    public static final String RPI_SEC_IMAGE_DATATYPE = "image/*";
}
