package com.andersen.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LocalStorageProperties {
    private static final String SAVE_PATH_PROPERTY = "savePath";
    private final String savePath;

    public LocalStorageProperties(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));

        savePath = properties.getProperty(SAVE_PATH_PROPERTY);
    }

    public String getSavePath() {
        return savePath;
    }
}
