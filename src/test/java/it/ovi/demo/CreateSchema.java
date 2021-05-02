package it.ovi.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * This is not a real test, but it automatically creates the "init.sql" file inside
 * "config/postgres/scripts".
 */
@Disabled
@SpringBootTest
@ActiveProfiles({"nocache", "create-schema"})
public class CreateSchema {

    @Test
    void createSchema() {

    }
}
