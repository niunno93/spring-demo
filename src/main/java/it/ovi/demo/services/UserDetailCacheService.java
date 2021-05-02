package it.ovi.demo.services;

import it.ovi.demo.models.UserDetail;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Delegates {@link UserDetail}s creation and updates to delegate; caches {@link UserDetailCacheService#findById(Long)}.
 */
public class UserDetailCacheService implements UserService {

    private static final String CACHE_NAME = "UserDetailCachedService";

    private final UserService delegate;

    public UserDetailCacheService(UserService delegate) {
        this.delegate = delegate;
    }

    @CachePut(value = CACHE_NAME, key = "#result.id")
    @Override
    public UserDetail create(UserDetail userDetail) {
        return delegate.create(userDetail);
    }

    @CachePut(value = CACHE_NAME, key = "#userDetail.id")
    @Override
    public UserDetail update(UserDetail userDetail) {
        return delegate.update(userDetail);
    }

    @Cacheable(value = CACHE_NAME)
    @Override
    public Optional<UserDetail> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public Page<UserDetail> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

    @CacheEvict(value = CACHE_NAME)
    @Override
    public void deleteById(Long id) {
        delegate.deleteById(id);
    }
}
