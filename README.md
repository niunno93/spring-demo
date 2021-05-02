# Spring demo application
This is a demo web application that shows some Spring
features in action.
The application exposes the *user-details* REST API where you can
create, get, list, update and delete *user-detail* 
resources.
*User-detail* is a simple object that contains:

* id
* username 
* email
* creation date
* modification date

The application stores data in PostgreSQL, uses 
Redis as distributed cache and sends audit events
to Elasticsearch.

**NOTE**: this application just wants to show some features of the Spring framework;
it does not try to solve any real business problem.

## Build and Run
To build and run the application, you need the following:

* OpenJDK 11;
* Docker;
* Docker Compose.

The application has been tested with Docker 20.10.6
and Docker Compose 1.29.1 on a Linux machine.

In addition, to run Elasticsearch, you need to 
[set vm.max_map_count to at least 262144](https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html#docker-prod-prerequisites).

### Build
On Linux and macOS:

`./mvnw spring-boot:build-image`

### Run
`docker-compose up`

The command starts PostgreSQL, Redis, Elasticsearch, Kibana and two demo applications.
You can access Swagger at [http://127.0.0.1:8081/swagger-ui/](http://127.0.0.1:8081/swagger-ui/)
and [http://127.0.0.1:8082/swagger-ui/](http://127.0.0.1:8082/swagger-ui/).
The implemented controller is **user-details-controller**.
With swagger, you can create, list and delete *user-detail* objects.

The two demo applications use Redis as distributed cache when retrieving 
*user-detail* elements. Additionally, every time a *user-detail* is created or updated,
an audit event is generated and sent to Elasticsearch asynchronously. 
You can check *user_detail_audit* index in [Kibana](http://127.0.0.1:5601).

## Structure
### Main flow
Http requests are handled by 
[UserDetailsController](src/main/java/it/ovi/demo/controllers/v1/UserDetailsController.java).
The controller has its own [DTOs](src/main/java/it/ovi/demo/controllers/v1/dto),
which show validation constrains such as *@NotNull*, *@NotBlank* and *@Email*;
Spring uses [Jackson](https://github.com/FasterXML/jackson) to deserialize
the HTTP body and hand the object over to 
[Hibernate Validator](https://hibernate.org/validator/). 
If the object is invalid, a Bad-request response is returned.

The requests are forwarded to [UserService](src/main/java/it/ovi/demo/services/UserService.java),
which has 3 implementations composed in a [Decorator](https://en.wikipedia.org/wiki/Decorator_pattern)
fashion. [PersistenceUserService](src/main/java/it/ovi/demo/services/PersistenceUserService.java)
uses [UserDetailRepository](src/main/java/it/ovi/demo/repositories/UserDetailRepository.java)
to store and retrieve data into and from PostgreSQL, whereas 
[EventEmitterUserService](src/main/java/it/ovi/demo/services/EventEmitterUserService.java) 
emits an event every time a *user-detail* is created or updated; the last service,
[UserDetailCacheService](src/main/java/it/ovi/demo/services/UserDetailCacheService.java),
uses Redis cache for fast retrieval.

Events emitted by [EventEmitterUserService](src/main/java/it/ovi/demo/services/EventEmitterUserService.java)
are asynchronously read by 
[UserDetailToAuditListener](src/main/java/it/ovi/demo/listener/UserDetailToAuditListener.java),
which leverages [UserDetailAuditService](src/main/java/it/ovi/demo/services/UserDetailAuditService.java) 
and [UserDetailAuditRepository](src/main/java/it/ovi/demo/repositories/UserDetailAuditRepository.java)
to send those events to Elasticsearch.

### Other features
[Test folder](src/test/java/it/ovi/demo) contains several tests that touch
different layers of the application.

For example:

* [UserDetailRepositoryTest](src/test/java/it/ovi/demo/repositories/UserDetailRepositoryTest.java)
  uses *@DataJpaTest* to test the persistence layer;
* [UserDetailAuditRepositoryTest](src/test/java/it/ovi/demo/repositories/UserDetailAuditRepositoryTest.java)
  starts an Elasticsearch instance to verify that 
  [UserDetailAuditRepository](src/main/java/it/ovi/demo/repositories/UserDetailAuditRepository.java)
  can interact with the search engine;
* [UserDetailsControllerTest](src/test/java/it/ovi/demo/controllers/v1/UserDetailsControllerTest.java)
  starts an embedded PostgresSQL, and a mocked MVC environment to verify
  that *user-detail* resources can be stored and retrieved;
* [DemoApplicationTest](src/test/java/it/ovi/demo/DemoApplicationTest.java)
  verifies the integration between the application and its dependencies.
  
Errors are handled by 
[UserDetailsControllerAdvice](src/main/java/it/ovi/demo/controllers/v1/UserDetailsControllerAdvice.java),
which builds the right response depending on the error type.

[UserDetailEntity](src/main/java/it/ovi/demo/entities/UserDetailEntity.java) 
automatically sets *creationDate* and *modificationDate* by defining *@PrePersist* 
and *@PreUpdate* methods.




