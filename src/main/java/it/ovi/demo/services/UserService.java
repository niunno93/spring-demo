package it.ovi.demo.services;

import it.ovi.demo.models.UserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Manages {@link UserDetail}s.
 */
public interface UserService {
    UserDetail create(UserDetail userDetail);
    UserDetail update(UserDetail userDetail);
    Optional<UserDetail> findById(Long id);
    Page<UserDetail> findAll(Pageable pageable);
    void deleteById(Long id);
}
