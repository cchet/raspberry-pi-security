package at.rpisec.oauth.config;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
public class SecurityProperties {

    public static final String ROLE_PREIX = "ROLE_";

    public static final String GROUP_ADMIN = "admin";
    public static final String GROUP_CLIENT = "client";
    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";
    public static final String ROLE_ADMIN = ROLE_PREIX + "ADMIN";
    public static final String ROLE_CLIENT = ROLE_PREIX + "CLIENT";
    public static long PASSWWORD_VALIDITY_DURATION_MONTHS = 3;
}
