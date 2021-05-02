package it.ovi.demo.services;

import it.ovi.demo.entities.UserDetailEntity;
import it.ovi.demo.models.UserDetail;
import it.ovi.demo.repositories.UserDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Manages {@link UserDetail}s as persistence layer.
 */
@Transactional(readOnly = true)
public class PersistenceUserService implements UserService {

    private final UserDetailRepository repository;

    public PersistenceUserService(UserDetailRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public UserDetail create(UserDetail userDetail) {
        if (userDetail.getId() != null) {
            throw new IllegalArgumentException("User ID must be null.");
        }

        UserDetailEntity entity = new UserDetailEntity(userDetail.getName(), userDetail.getEmail());
        entity = repository.save(entity);

        return map(entity);
    }

    @Transactional
    @Override
    public UserDetail update(UserDetail userDetail) {
        Long id = userDetail.getId();
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        UserDetailEntity entity = repository.findById(id).orElseThrow();
        entity.setName(userDetail.getName());
        entity.setEmail(userDetail.getEmail());

        // force updating modificationDate
        repository.flush();

        return map(entity);
    }

    @Override
    public Optional<UserDetail> findById(Long id) {
        return repository.findById(id).map(PersistenceUserService::map);
    }

    @Override
    public Page<UserDetail> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(PersistenceUserService::map);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException(String.format("User with id %s does not exist", id));
        }
        repository.deleteById(id);
    }

    private static UserDetail map(UserDetailEntity entity) {
        return new UserDetail(entity.getId(), entity.getName(), entity.getEmail(), entity.getCreationDate(),
                entity.getModificationDate());
    }
}
