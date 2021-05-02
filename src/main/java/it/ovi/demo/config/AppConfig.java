package it.ovi.demo.config;

import it.ovi.demo.repositories.UserDetailRepository;
import it.ovi.demo.services.EventEmitterUserService;
import it.ovi.demo.services.PersistenceUserService;
import it.ovi.demo.services.UserDetailCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableCaching
@EnableAsync
public class AppConfig {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Bean
    public PersistenceUserService persistenceUserService() {
        return new PersistenceUserService(userDetailRepository);
    }

    @Bean
    public EventEmitterUserService eventEmitterUserService() {
        return new EventEmitterUserService(applicationEventPublisher, persistenceUserService());
    }

    @Primary
    @Bean
    public UserDetailCacheService userDetailCachedService() {
        return new UserDetailCacheService(eventEmitterUserService());
    }
}
