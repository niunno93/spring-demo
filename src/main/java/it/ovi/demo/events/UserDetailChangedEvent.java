package it.ovi.demo.events;

import it.ovi.demo.models.UserDetail;
import org.springframework.context.ApplicationEvent;

/**
 * Emitted when a {@link UserDetail} is created or updated.
 */
public class UserDetailChangedEvent extends ApplicationEvent {

    private final UserDetail userDetail;

    public UserDetailChangedEvent(Object source, UserDetail userDetail) {
        super(source);
        this.userDetail = userDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }
}
