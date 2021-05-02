package it.ovi.demo.services;

import it.ovi.demo.models.UserDetail;
import it.ovi.demo.repositories.UserDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class PersistenceUserServiceTest {

    @Autowired
    private UserDetailRepository userDetailRepository;

    private PersistenceUserService persistenceUserService;

    @BeforeEach
    void setUp() {
        persistenceUserService = new PersistenceUserService(userDetailRepository);
    }

    @Test
    void creationAndModificationDate() {
        UserDetail userDetail = new UserDetail("name", "email@fake.com");

        // create
        UserDetail savedUser = persistenceUserService.create(userDetail);
        assertThat(savedUser.getCreationDate(), is(notNullValue()));
        assertThat(savedUser.getModificationDate(), is(notNullValue()));
        assertThat(savedUser.getModificationDate(), is(savedUser.getCreationDate()));

        // update
        savedUser.setName("name2");
        UserDetail updatedUser = persistenceUserService.update(savedUser);
        assertThat(updatedUser.getCreationDate(), is(savedUser.getCreationDate()));
        assertThat(updatedUser.getModificationDate(), is(greaterThan(savedUser.getModificationDate())));
    }
}