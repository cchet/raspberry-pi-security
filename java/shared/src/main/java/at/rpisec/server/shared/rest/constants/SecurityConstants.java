package at.rpisec.server.shared.rest.constants;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
public class SecurityConstants {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";
    public static final String SYSTEM = "SYSTEM";
    public static final String USER_SYSTEM = "system";
    public static final String USER_ADMIN = "admin";
    public static final String ROLE_ADMIN = ROLE_PREFIX + ADMIN;
    public static final String ROLE_CLIENT = ROLE_PREFIX + CLIENT;
    public static final String ROLE_SYSTEM = ROLE_PREFIX + SYSTEM;

    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String SCOPE_TRUST = "trust";

    public static long PASSWORD_VALIDITY_DURATION_MONTHS = 3;
    public static int TOKEN_VALIDITY_DURATION_SECONDS = 60;
    public static int REFRESH_TOKEN_VALIDITY_DURATION_SECONDS = 60 * 60 * 24;
}
