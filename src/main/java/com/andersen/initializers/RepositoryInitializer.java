package com.andersen.initializers;

import com.andersen.properties.LocalStorageProperties;
import com.andersen.properties.PropertiesJDBC;
import com.andersen.repositories.BookRepository;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.RequestRepository;
import com.andersen.repositories.jdbc.Database;
import com.andersen.repositories.jdbc.JdbcBookRepository;
import com.andersen.repositories.jdbc.JdbcOrderRepository;
import com.andersen.repositories.jdbc.JdbcRequestRepository;
import com.andersen.repositories.jpa.HibernateDatabase;
import com.andersen.repositories.jpa.JpaBookRepository;
import com.andersen.repositories.jpa.JpaOrderRepository;
import com.andersen.repositories.jpa.JpaRequestRepository;
import com.andersen.repositories.local.LocalBookRepository;
import com.andersen.repositories.local.LocalOrderRepository;
import com.andersen.repositories.local.LocalRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RepositoryInitializer {
    private static final String JDBC_PROPERTIES_PATH = "src/main/resources/jdbc.properties";
    private static final String JPA_PROPERTIES_PATH = "src/main/resources/hibernate.properties";
    private static final String LOCAL_PROPERTIES_PATH = "src/main/resources/localStorage.properties";

    private BookRepository bookRepository;
    private OrderRepository orderRepository;
    private RequestRepository requestRepository;

    public RepositoryInitializer(RepositoryType repositoryType) throws IOException {
        switch (repositoryType) {
            case JPA -> initializeJPA();
            case JDBC -> initializeJDBC();
            case LOCAL -> initializeLocal();
            default -> throw new IllegalArgumentException("Not supported repository type");
        }
    }

    private void initializeJDBC() throws IOException {
        PropertiesJDBC propertiesJDBC = new PropertiesJDBC(JDBC_PROPERTIES_PATH);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(propertiesJDBC.getUrl());
        hikariConfig.setUsername(propertiesJDBC.getUsername());
        hikariConfig.setPassword(propertiesJDBC.getPassword());

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Database database = new Database(hikariDataSource);

        bookRepository = new JdbcBookRepository(database);
        orderRepository = new JdbcOrderRepository(database);
        requestRepository = new JdbcRequestRepository(database);
    }

    private void initializeJPA() throws IOException {
        Properties hibernateProperties = new Properties();
        hibernateProperties.load(new FileInputStream(JPA_PROPERTIES_PATH));

        HibernateDatabase hibernateDatabase = new HibernateDatabase(hibernateProperties);

        bookRepository = new JpaBookRepository(hibernateDatabase);
        orderRepository = new JpaOrderRepository(hibernateDatabase);
        requestRepository = new JpaRequestRepository(hibernateDatabase);
    }

    private void initializeLocal() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LocalStorageProperties properties = new LocalStorageProperties(LOCAL_PROPERTIES_PATH);

        bookRepository = new LocalBookRepository(objectMapper, properties.getSavePath());
        orderRepository = new LocalOrderRepository(objectMapper, properties.getSavePath());
        requestRepository = new LocalRequestRepository(objectMapper, properties.getSavePath());
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public RequestRepository getRequestRepository() {
        return requestRepository;
    }
}
