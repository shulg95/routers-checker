package com.routers.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;


public enum TFTPServerConfig {

    INSTANCE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, String> config;

    public Map<String, String> config() {

        if (config == null) {
            readConfig();
        }

        return config;
    }

    private void readConfig() {
        try {
            URL tftpServerConfigFileUrl = getClass()
                    .getClassLoader()
                    .getResource(ApplicationConfig.INSTANCE.getProperty(AppConfigKeys.TFTP_SERVER_CONFIG_FILE));
            if (tftpServerConfigFileUrl == null) throw new IOException("TFTP server config file is absent");
            config = objectMapper.readValue(
                    new File(tftpServerConfigFileUrl.getFile()),
                    new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read TFTP server config file", e);
        }
    }
}