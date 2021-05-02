package it.ovi.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.ovi.demo.controllers.v1.dto.UserDetailDto;
import it.ovi.demo.documents.UserDetailAuditDocument;
import it.ovi.demo.repositories.UserDetailAuditRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = {DemoApplicationTest.Initializer.class})
class DemoApplicationTest {

    private static final String ELASTICSEARCH_PASSWORD = "sa";

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:6-alpine"))
            .withExposedPorts(6379);
    @Container
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:11-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");
    @Container
    public static ElasticsearchContainer elasticsearch = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.12.0")
            .withPassword(ELASTICSEARCH_PASSWORD);

    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserDetailAuditRepository auditRepository;

    private List<UserDetailDto> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        // delete users
        for (UserDetailDto user : users) {
            Long id = user.getId();
            assert id != null;
            restTemplate.delete("/api/v1/user-details/" + id);
        }
        auditRepository.deleteAll();
    }

    @Test
    void testCreateUpdateAndGet() throws JsonProcessingException {
        UserDetailDto unsavedUser = new UserDetailDto("name", "fake@email.com");

        // create
        HttpEntity<String> request = createJsonEntity(unsavedUser);
        ResponseEntity<UserDetailDto> response = restTemplate.postForEntity("/api/v1/user-details", request, UserDetailDto.class);
        assertThat(response.getStatusCode().is2xxSuccessful(), is(true));
        UserDetailDto savedUser = response.getBody();
        assertThat(savedUser.getId(), notNullValue());
        assertThat(savedUser.getName(), is("name"));
        assertThat(savedUser.getEmail(), is("fake@email.com"));
        long id = savedUser.getId();
        users.add(savedUser);

        // update
        savedUser.setName("name2");
        request = createJsonEntity(savedUser);
        restTemplate.put("/api/v1/user-details/" + id, request, UserDetailDto.class);

        // get
        response = restTemplate.getForEntity("/api/v1/user-details/" + id, UserDetailDto.class);
        assertThat(response.getStatusCode().is2xxSuccessful(), is(true));
        UserDetailDto updatedUser = response.getBody();
        assertThat(updatedUser.getId(), is(id));
        assertThat(updatedUser.getName(), is("name2"));
        assertThat(updatedUser.getEmail(), is("fake@email.com"));
    }

    @Test
    void testAudit() throws JsonProcessingException, InterruptedException {
        List<UserDetailDto> unsavedUsers = Arrays.asList(
                new UserDetailDto("name1", "email1@fake.com"),
                new UserDetailDto("name2", "email2@fake.com")
        );

        // create two users
        for (UserDetailDto user : unsavedUsers) {
            users.add(post(user));
        }

        // update one user
        UserDetailDto user1 = users.get(0);
        user1.setName("name1_1");
        put(user1);

        // TODO a retry with delay si better
        Thread.sleep(2000);

        List<UserDetailAuditDocument> audits =
                StreamSupport.stream(auditRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertThat(audits, hasSize(3));
    }

    private HttpEntity<String> createJsonEntity(Object obj) throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(obj);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(content, headers);
    }

    private UserDetailDto post(UserDetailDto user) throws JsonProcessingException {
        HttpEntity<String> request = createJsonEntity(user);
        ResponseEntity<UserDetailDto> response = restTemplate.postForEntity("/api/v1/user-details", request, UserDetailDto.class);
        assertThat(response.getStatusCode().is2xxSuccessful(), is(true));
        return response.getBody();
    }

    private void put(UserDetailDto user) throws JsonProcessingException {
        HttpEntity<String> request = createJsonEntity(user);
        Long id = user.getId();
        restTemplate.put("/api/v1/user-details/" + id, request, UserDetailDto.class);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.redis.port=" + redis.getFirstMappedPort(),

                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "spring.jpa.generate-ddl=true",

                    "spring.elasticsearch.rest.uris=http://localhost:" + elasticsearch.getFirstMappedPort(),
                    "spring.elasticsearch.rest.username=elastic",
                    "spring.elasticsearch.rest.password=" + ELASTICSEARCH_PASSWORD
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}