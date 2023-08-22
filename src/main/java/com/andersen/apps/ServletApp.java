package com.andersen.apps;

import com.andersen.controllers.BookController;
import com.andersen.controllers.OrderController;
import com.andersen.controllers.RequestController;
import com.andersen.controllers.servlet.BookControllerServlet;
import com.andersen.controllers.servlet.OrderControllerServlet;
import com.andersen.controllers.servlet.RequestControllerServlet;
import com.andersen.properties.TomcatProperties;
import com.andersen.router.RouterServlet;
import com.andersen.services.BookService;
import com.andersen.services.OrderService;
import com.andersen.services.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class ServletApp implements App {
    private final RouterServlet servlet;
    private final static String TOMCAT_PROPERTIES_PATH = "src/main/resources/tomcat.properties";

    public ServletApp(BookService bookService, OrderService orderService, RequestService requestService) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        BookController bookController = new BookControllerServlet(bookService, objectMapper);
        OrderController orderController = new OrderControllerServlet(bookService, orderService, objectMapper);
        RequestController requestController = new RequestControllerServlet(requestService);

        servlet = new RouterServlet(bookController, orderController, requestController, objectMapper);
    }

    @Override
    public void start() {
        try {
            TomcatProperties tomcatProperties = new TomcatProperties(TOMCAT_PROPERTIES_PATH);

            Tomcat tomcat = new Tomcat();
            tomcat.setBaseDir("temp");

            Connector httpConnector = new Connector();
            httpConnector.setPort(tomcatProperties.getPort());
            tomcat.getService().addConnector(httpConnector);

            String docBase = new File(".").getAbsolutePath();

            Context servletContext = tomcat.addContext(tomcatProperties.getContextPath(), docBase);

            tomcat.addServlet(tomcatProperties.getContextPath(), "RouterServlet", servlet);
            servletContext.addServletMappingDecoded("/*", "RouterServlet");

            tomcat.start();
            tomcat.getServer().await();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
