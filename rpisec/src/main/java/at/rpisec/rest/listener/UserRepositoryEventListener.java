package at.rpisec.rest.listener;

import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.rest.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class validates the exposed crud operations of the {@link UserRestRepository}.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
@Component
public class UserRepositoryEventListener extends AbstractRepositoryEventListener<User> {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepo;

    @Override
    protected void onBeforeCreate(User entity) {
        entity.setPassword(encoder.encode(entity.getPassword()));
    }

    @Override
    protected void onAfterCreate(User entity) {
        super.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeSave(User entity) {
        if (!userRepo.exists(entity.getId())) {
            throw new DataRetrievalFailureException("User with id '" + entity.getId() + "' does not exist");
        }
    }

    @Override
    protected void onAfterSave(User entity) {
        super.onAfterSave(entity);
    }

    @Override
    protected void onBeforeLinkSave(User parent,
                                    Object linked) {
        super.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(User parent,
                                   Object linked) {
        super.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(User parent,
                                      Object linked) {
        super.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onAfterLinkDelete(User parent,
                                     Object linked) {
        super.onAfterLinkDelete(parent, linked);
    }

    @Override
    protected void onBeforeDelete(User entity) {
        if (!userRepo.exists(entity.getId())) {
            throw new DataRetrievalFailureException("User with id '" + entity.getId() + "' does not exist");
        }
    }

    @Override
    protected void onAfterDelete(User entity) {
        super.onAfterDelete(entity);
    }
}
