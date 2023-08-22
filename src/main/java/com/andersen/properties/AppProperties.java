package com.andersen.properties;

import com.andersen.apps.ControllerType;
import com.andersen.initializers.RepositoryType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {
    private static final String REPOSITORY_PROPERTY = "repositoryType";
    private static final String CONTROLLER_PROPERTY = "controllerType";
    private final RepositoryType repositoryType;
    private final ControllerType controllerType;

    public AppProperties(String path) throws IOException {
        Properties appProperties = new Properties();
        appProperties.load(new FileInputStream(path));

        repositoryType = RepositoryType.valueOf(appProperties.getProperty(REPOSITORY_PROPERTY).toUpperCase());
        controllerType = ControllerType.valueOf(appProperties.getProperty(CONTROLLER_PROPERTY).toUpperCase());
    }

    public RepositoryType getRepositoryType() {
        return repositoryType;
    }

    public ControllerType getControllerType() {
        return controllerType;
    }
}
