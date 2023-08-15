package com.andersen.config;

import com.andersen.EntryPoint;
import com.andersen.config.model.ConfigModel;
import com.andersen.controllers.BookController;
import com.andersen.controllers.OrderController;
import com.andersen.controllers.RequestController;
import com.andersen.controllers.servlet.BookControllerServlet;
import com.andersen.controllers.servlet.OrderControllerServlet;
import com.andersen.controllers.servlet.RequestControllerServlet;
import com.andersen.repositories.BookRepository;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.RequestRepository;
import com.andersen.repositories.local.LocalBookRepository;
import com.andersen.repositories.local.LocalOrderRepository;
import com.andersen.repositories.local.LocalRequestRepository;
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
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServlet;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DependencyModule extends AbstractModule {

    public DependencyModule() {
    }

    @Override
    protected void configure() {
        super.configure();

        bind(HttpServlet.class).to(RouterServlet.class);

        bind(BookRepository.class).to(LocalBookRepository.class);
        bind(OrderRepository.class).to(LocalOrderRepository.class);
        bind(RequestRepository.class).to(LocalRequestRepository.class);

        bind(BookService.class).to(BookServiceImpl.class);
        bind(OrderService.class).to(OrderServiceImpl.class);
        bind(RequestService.class).to(RequestServiceImpl.class);

        bind(BookController.class).to(BookControllerServlet.class);
        bind(OrderController.class).to(OrderControllerServlet.class);
        bind(RequestController.class).to(RequestControllerServlet.class);
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    @Provides
    @Singleton
    public ConfigModel configModel() {
        return readConfig(objectMapper());
    }

    private ConfigModel readConfig(ObjectMapper objectMapper) {
        try {
            String rawConfig = IOUtils.toString(EntryPoint.class.getClassLoader().getResourceAsStream("config.json"),
                    StandardCharsets.UTF_8);
            System.out.printf("Config:\n%s", rawConfig);
            return objectMapper.readValue(rawConfig, ConfigModel.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
