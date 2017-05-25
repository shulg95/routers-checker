package com.routers;

import com.routers.config.*;
import com.routers.connections.TFTPServer;
import com.routers.engine.script.GroovyScriptExecutor;
import com.routers.engine.script.ScriptExecutor;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SingleCheckPerformer implements Runnable {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SingleCheckPerformer.class);

    private ScriptExecutor scriptExecutor;
    private List<Map<String, String>> networkElements;
    private Map<String, String> tftpServerConfig;
    private String scriptStartMethod;

    public SingleCheckPerformer() {
        scriptExecutor = new GroovyScriptExecutor();
        networkElements = ElementConfig.INSTANCE.config();
        tftpServerConfig = TFTPServerConfig.INSTANCE.config();
        scriptStartMethod = ApplicationConfig.INSTANCE.getProperty(AppConfigKeys.SCRIPT_START_METHOD);
    }

    @Override
    public void run() {
        log.debug("Running new check.");
        //Starting TFTP server
        String downloadDirName = tftpServerConfig.get(ConfigKeys.STANDARD_PATH);
        File downloadDir = new File(downloadDirName);
        Integer port = new Integer(tftpServerConfig.get(ConfigKeys.TFTP_SERVER_PORT));
        TFTPServer.ServerMode mode = TFTPServer.ServerMode.PUT_ONLY;

        TFTPServer tftpServer = null;
        try {
            tftpServer = new TFTPServer(downloadDir, downloadDir, port, mode, null, null);
        } catch (IOException e) {
            log.error("Can't start TFTP server.\n {}", e);
            return;
        }

        for (Map<String, String> networkElement : networkElements) {
            scriptExecutor.executeScript(
                    networkElement.get(ConfigKeys.SCRIPT_FILE_PATH),
                    scriptStartMethod,
                    networkElement,
                    tftpServerConfig);
        }

        //Stopping TFTP server
        tftpServer.shutdown();
        log.debug("Check has been finished.");
    }
}