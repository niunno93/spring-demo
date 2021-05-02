package it.ovi.demo.repositories;

import it.ovi.demo.entities.UserDetailEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserDetailRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Test
    void validUser() {
        UserDetailEntity user = new UserDetailEntity("name", "fake@fake.com");
        user = userDetailRepository.save(user);
        assertThat(user.getId(), is(notNullValue()));
    }

    @Test
    void emailAlreadyExist() {
        UserDetailEntity user1 = new UserDetailEntity("name1", "name@fake.com");
        UserDetailEntity user2 = new UserDetailEntity("name2", "name@fake.com");

        assertThrows(PersistenceException.class, () -> {
            userDetailRepository.save(user1);
            userDetailRepository.save(user2);
            entityManager.flush();
        });
    }

    @Test
    void findByEmail() {
        UserDetailEntity user1 = new UserDetailEntity("name1", "email1@fake.com");
        UserDetailEntity user2 = new UserDetailEntity("name2", "email2@fake.com");
        userDetailRepository.save(user1);
        userDetailRepository.save(user2);

        UserDetailEntity retrievedUser = userDetailRepository.findByEmail("email2@fake.com").orElseThrow();
        assertThat(retrievedUser.getName(), is("name2"));

        assertThat(userDetailRepository.findByEmail("invalid@fake.com").isEmpty(), is(true));
    }

    @Test
    void creationAndModificationDate() {
        UserDetailEntity userDetail = new UserDetailEntity("name", "email@fake.com");
        userDetail = userDetailRepository.save(userDetail);
        assertThat(userDetail.getCreationDate(), is(notNullValue()));
        assertThat(userDetail.getModificationDate(), is(notNullValue()));
        assertThat(userDetail.getCreationDate(), is(userDetail.getModificationDate()));
        entityManager.flush();

        // update entity
        userDetail.setName("name2");
        entityManager.flush();
        assertThat(userDetail.getModificationDate(), is(greaterThan(userDetail.getCreationDate())));
    }
}