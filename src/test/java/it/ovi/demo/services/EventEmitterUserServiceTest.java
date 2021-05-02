package it.ovi.demo.services;

import it.ovi.demo.events.UserDetailChangedEvent;
import it.ovi.demo.models.UserDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ActiveProfiles({"nocache", "embedded-db"})
class EventEmitterUserServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public EventListener eventListener() {
            return new EventListener();
        }
    }

    @Autowired
    private EventListener eventListener;

    @Autowired
    private EventEmitterUserService userService;

    @Test
    void verifyEvents() {
        UserDetail user = new UserDetail("name", "email@fake.com");

        UserDetail savedUser = userService.create(user);

        // verify we received events
        List<UserDetail> eventUsers = eventListener.userDetails;
        assertThat(eventUsers, hasSize(1));
        UserDetail eventUser = eventUsers.get(0);
        assertThat(eventUser.getId(), is(savedUser.getId()));
    }

    /**
     * Stores events in-memory.
     */
    private static class EventListener implements ApplicationListener<UserDetailChangedEvent> {

        private final List<UserDetail> userDetails;

        private EventListener() {
            userDetails = new CopyOnWriteArrayList<>();
        }

        @Override
        public void onApplicationEvent(UserDetailChangedEvent event) {
            userDetails.add(event.getUserDetail());
        }

        public List<UserDetail> getUserDetails() {
            return userDetails;
        }
    }

}