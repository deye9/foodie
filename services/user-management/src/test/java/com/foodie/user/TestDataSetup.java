package com.foodie.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestDataSetup implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
  
    private static boolean started = false;

    // Define a PostgreSQL container with specific database name, username, and password
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("user_management")
        .withUsername("user")
        .withPassword("password");

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (started) {
            return;
        }

        // Fire up the postgres container
        postgresContainer.start();

        // Set system properties for the Spring datasource to use the PostgreSQL container
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());

        // Store this instance in the global context store
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("TestDataSetup", this);
    }

    @Override
    public void close() throws Throwable {
        // Stop the PostgreSQL container after all tests
        postgresContainer.stop();
    }

    @Test
    public void whenSelectQueryExecuted_thenResultsReturned() throws Exception {
        if (!started) {
            postgresContainer.start();
        }

        // Perform a SQL query and check the result
        ResultSet resultSet = performQuery(postgresContainer, "SELECT 1");
        resultSet.next();
        int result = resultSet.getInt(1);
        assertEquals(1, result);
    }

    private ResultSet performQuery(PostgreSQLContainer postgres, String query) throws SQLException {
        // Connect to the PostgreSQL container and execute the query
        String jdbcUrl = postgres.getJdbcUrl();
        String username = postgres.getUsername();
        String password = postgres.getPassword();
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        return conn.createStatement()
            .executeQuery(query);
    }
}
