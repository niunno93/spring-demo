package it.ovi.demo.repositories;

import it.ovi.demo.documents.UserDetailAuditDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {UserDetailAuditRepositoryTest.Initializer.class})
class UserDetailAuditRepositoryTest {

    private static final String ELASTICSEARCH_PASSWORD = "sa";

    @Container
    public static ElasticsearchContainer elasticsearch = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.12.0")
            .withPassword(ELASTICSEARCH_PASSWORD);

    @Autowired
    private UserDetailAuditRepository userDetailAuditRepository;

    @Test
    void createAndGet() {
        final UserDetailAuditDocument audit1 = new UserDetailAuditDocument(1L, "name1", "email@fake.com", OffsetDateTime.now());
        final UserDetailAuditDocument audit2 = new UserDetailAuditDocument(2L, "name2", "email2@fake.com", OffsetDateTime.now());
        final UserDetailAuditDocument audit3 = new UserDetailAuditDocument(1L, "name1_2", "email@fake.com", OffsetDateTime.now());

        // save
        userDetailAuditRepository.save(audit1);
        userDetailAuditRepository.save(audit2);
        userDetailAuditRepository.save(audit3);

        // get
        List<UserDetailAuditDocument> retrievedAudits = StreamSupport.stream(userDetailAuditRepository.findAll()
                .spliterator(), false).collect(Collectors.toList());
        assertThat(retrievedAudits, hasSize(3));
        assertThat(retrievedAudits, hasItems(
                allOf(
                        hasProperty("userDetailId", is(audit1.getUserDetailId())),
                        hasProperty("name", is(audit1.getName())),
                        hasProperty("email", is(audit1.getEmail())),
                        hasProperty("modificationDate",
                                is(audit1.getModificationDate().truncatedTo(ChronoUnit.MILLIS)))
                ),
                allOf(
                        hasProperty("userDetailId", is(audit2.getUserDetailId())),
                        hasProperty("name", is(audit2.getName())),
                        hasProperty("email", is(audit2.getEmail())),
                        hasProperty("modificationDate",
                                is(audit2.getModificationDate().truncatedTo(ChronoUnit.MILLIS)))
                ),
                allOf(
                        hasProperty("userDetailId", is(audit3.getUserDetailId())),
                        hasProperty("name", is(audit3.getName())),
                        hasProperty("email", is(audit3.getEmail())),
                        hasProperty("modificationDate",
                                is(audit3.getModificationDate().truncatedTo(ChronoUnit.MILLIS)))
                )
        ));
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.elasticsearch.rest.uris=http://localhost:" + elasticsearch.getFirstMappedPort(),
                    "spring.elasticsearch.rest.username=elastic",
                    "spring.elasticsearch.rest.password=" + ELASTICSEARCH_PASSWORD
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}