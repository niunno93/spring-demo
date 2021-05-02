package it.ovi.demo.services;

import it.ovi.demo.documents.UserDetailAuditDocument;
import it.ovi.demo.models.UserDetail;
import it.ovi.demo.repositories.UserDetailAuditRepository;
import org.springframework.stereotype.Service;

/**
 * Audits {@link UserDetail}s changes.
 */
@Service
public class UserDetailAuditService {

    private final UserDetailAuditRepository repository;

    public UserDetailAuditService(UserDetailAuditRepository repository) {
        this.repository = repository;
    }

    public void audit(UserDetail userDetail) {
        repository.save(map(userDetail));
    }

    private static UserDetailAuditDocument map(UserDetail user) {
        return new UserDetailAuditDocument(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getModificationDate()
        );
    }
}
