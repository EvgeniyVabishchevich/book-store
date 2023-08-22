package com.andersen;

import com.andersen.apps.App;
import com.andersen.apps.ServletApp;
import com.andersen.initializers.RepositoryInitializer;
import com.andersen.initializers.ServiceInitializer;
import com.andersen.properties.AppProperties;

public class EntryPoint {
    private static final String PROPERTIES_PATH = "src/main/resources/app.properties";

    public static void main(String[] args) throws Exception {
        AppProperties appProperties = new AppProperties(PROPERTIES_PATH);

        RepositoryInitializer repositoryInitializer = new RepositoryInitializer(appProperties.getRepositoryType());

        ServiceInitializer serviceInitializer = new ServiceInitializer(repositoryInitializer.getBookRepository(),
                repositoryInitializer.getOrderRepository(), repositoryInitializer.getRequestRepository());

        App app = null;

        switch (appProperties.getControllerType()) {
            case SERVLET -> {
                app = new ServletApp(serviceInitializer.getBookService(), serviceInitializer.getOrderService(),
                        serviceInitializer.getRequestService());
            }
            default -> throw new IllegalArgumentException("Unsupported controller type");
        }

        app.start();
    }
}