package com.andersen;

import com.andersen.config.model.JdbcConfigModel;
import com.andersen.controllers.BookController;
import com.andersen.controllers.OrderController;
import com.andersen.controllers.RequestController;
import com.andersen.controllers.servlet.BookControllerServlet;
import com.andersen.controllers.servlet.OrderControllerServlet;
import com.andersen.controllers.servlet.RequestControllerServlet;
import com.andersen.models.Book;
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
import com.andersen.router.RouterServlet;
import com.andersen.services.BookService;
import com.andersen.services.OrderService;
import com.andersen.services.RequestService;
import com.andersen.services.impl.BookServiceImpl;
import com.andersen.services.impl.OrderServiceImpl;
import com.andersen.services.impl.RequestServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.imageio.spi.ServiceRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class EntryPoint {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        /*JdbcConfigModel config = readConfig(objectMapper);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.jdbcUrl());
        hikariConfig.setUsername(config.jdbcUsername());
        hikariConfig.setPassword(config.jdbcPassword());

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Database database = new Database(hikariDataSource);*/

        Properties hibernateProperties = new Properties();
        hibernateProperties.load(new FileInputStream("src/main/resources/hibernate.properties"));

        HibernateDatabase hibernateDatabase = new HibernateDatabase(hibernateProperties);

        BookRepository bookRepository = new JpaBookRepository(hibernateDatabase);
        OrderRepository orderRepository = new JpaOrderRepository(hibernateDatabase);
        RequestRepository requestRepository = new JpaRequestRepository(hibernateDatabase);

        BookService bookService = new BookServiceImpl(bookRepository, orderRepository, requestRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, requestRepository);
        RequestService requestService = new RequestServiceImpl(requestRepository);

        BookController bookController = new BookControllerServlet(bookService, objectMapper);
        OrderController orderController = new OrderControllerServlet(bookService, orderService, objectMapper);
        RequestController requestController = new RequestControllerServlet(requestService);


        HttpServlet servlet = new RouterServlet(bookController, orderController, requestController, objectMapper);

        start(servlet);
    }

    private static JdbcConfigModel readConfig(ObjectMapper objectMapper) {
        try {
            String rawConfig = IOUtils.toString(EntryPoint.class.getClassLoader().getResourceAsStream("config.json"),
                    StandardCharsets.UTF_8);
            System.out.printf("Config:\n%s", rawConfig);
            return objectMapper.readValue(rawConfig, JdbcConfigModel.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void start(HttpServlet servlet) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/tomcat.properties"));


            Tomcat tomcat = new Tomcat();
            tomcat.setBaseDir("temp");

            Connector httpConnector = new Connector();
            httpConnector.setPort(Integer.parseInt(properties.getProperty("port")));
            tomcat.getService().addConnector(httpConnector);

            String docBase = new File(".").getAbsolutePath();

            Context servletContext = tomcat.addContext(properties.getProperty("contextPath"), docBase);

            tomcat.addServlet(properties.getProperty("contextPath"), "RouterServlet", servlet);
            servletContext.addServletMappingDecoded("/*", "RouterServlet");

            tomcat.start();
            tomcat.getServer().await();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}