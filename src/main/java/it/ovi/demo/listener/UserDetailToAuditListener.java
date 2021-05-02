package it.ovi.demo.listener;

import it.ovi.demo.events.UserDetailChangedEvent;
import it.ovi.demo.services.UserDetailAuditService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listens to {@link UserDetailChangedEvent}s and audit user-details changes by using {@link UserDetailAuditService}.
 */
@Component
public class UserDetailToAuditListener {

    private final UserDetailAuditService service;

    public UserDetailToAuditListener(UserDetailAuditService service) {
        this.service = service;
    }

    @EventListener
    @Async
    public void handleEvent(UserDetailChangedEvent event) {
        service.audit(event.getUserDetail());
    }
}
