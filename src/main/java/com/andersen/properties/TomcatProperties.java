package com.andersen.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TomcatProperties {
    private final int port;
    private final String contextPath;

    public TomcatProperties(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));

        port = Integer.parseInt(properties.getProperty("port"));
        contextPath = properties.getProperty("contextPath");
    }

    public int getPort() {
        return port;
    }

    public String getContextPath() {
        return contextPath;
    }
}
