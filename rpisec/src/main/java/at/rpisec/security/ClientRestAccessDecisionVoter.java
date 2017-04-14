package at.rpisec.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * This class implements the access control for client rest api access.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/14/17
 */
public class ClientRestAccessDecisionVoter implements AccessDecisionVoter {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication,
                    Object object,
                    Collection collection) {
        // TODO: Implement voting for admin rest access
        return ACCESS_GRANTED;
    }
}
