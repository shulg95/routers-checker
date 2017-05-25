package com.routers.config;

import com.routers.utils.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public enum ApplicationConfig {

    INSTANCE;

    private final Properties properties = new Properties();
    private boolean isLoaded;

    public String getProperty(String name) {
        if (!isLoaded) {
            try (FileInputStream inputStream = new FileInputStream(
                    getClass().getClassLoader().getResource(Constants.CONFIG_FILE).getFile())) {
                properties.loadFromXML(inputStream);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read config file", e);
            }
        }

        return properties.getProperty(name);
    }

    public void setProperty(String name, String value) {

        properties.setProperty(name, value);
        try (FileOutputStream outputStream = new FileOutputStream(
                getClass().getClassLoader().getResource(Constants.CONFIG_FILE).getFile())) {
            properties.storeToXML(outputStream, null);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write to config file", e);
        }
    }
}