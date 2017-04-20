package at.rpisec.server.config;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
public class SecurityProperties {

    private static final String ROLE_PREIX = "ROLE_";

    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";
    public static final String ROLE_ADMIN = ROLE_PREIX + "ADMIN";
    public static final String ROLE_CLIENT = ROLE_PREIX + "CLIENT";
}
