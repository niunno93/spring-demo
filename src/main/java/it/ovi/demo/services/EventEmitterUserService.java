package it.ovi.demo.services;

import it.ovi.demo.events.UserDetailChangedEvent;
import it.ovi.demo.models.UserDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Emits a {@link UserDetailChangedEvent} every time a {@link UserDetail} is created or updated.
 */
public class EventEmitterUserService implements UserService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserService delegate;

    public EventEmitterUserService(
            ApplicationEventPublisher applicationEventPublisher,
            @Qualifier("persistenceUserService")
            UserService delegate) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.delegate = delegate;
    }

    @Override
    public UserDetail create(UserDetail userDetail) {
        userDetail = delegate.create(userDetail);
        applicationEventPublisher.publishEvent(new UserDetailChangedEvent(this, userDetail));
        return userDetail;
    }

    @Override
    public UserDetail update(UserDetail userDetail) {
        userDetail = delegate.update(userDetail);
        applicationEventPublisher.publishEvent(new UserDetailChangedEvent(this, userDetail));
        return userDetail;
    }

    @Override
    public Optional<UserDetail> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public Page<UserDetail> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        delegate.deleteById(id);
    }
}
