package com.routers.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public enum  ElementConfig {

    INSTANCE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, String>> config;

    public List<Map<String, String>> config() {

        if (config == null) {
            readConfig();
        }

        return config;
    }

    private void readConfig() {
        try {
            URL neConfigFileUrl = getClass()
                    .getClassLoader()
                    .getResource(ApplicationConfig.INSTANCE.getProperty(AppConfigKeys.NE_CONFIG_FILE));
            if (neConfigFileUrl == null) throw new IOException("NE config file is absent");
            config = objectMapper.readValue(
                    new File(neConfigFileUrl.getFile()),
                    new TypeReference<List<Map<String, String>>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read network element config file", e);
        }
    }
}