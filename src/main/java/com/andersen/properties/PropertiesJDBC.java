package com.andersen.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesJDBC {
    private final String url;
    private final String username;
    private final String password;

    public PropertiesJDBC(String path) throws IOException {
        Properties propertiesJDBC = new Properties();
        propertiesJDBC.load(new FileInputStream(path));

        url = propertiesJDBC.getProperty("url");
        username = propertiesJDBC.getProperty("username");
        password = propertiesJDBC.getProperty("password");
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
