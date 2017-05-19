package com.routers.engine.script;

import com.google.common.base.Preconditions;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class which performs Groovy script execution.
 */
public class GroovyScriptExecutor implements ScriptExecutor {

    private static final Logger log = LoggerFactory.getLogger(GroovyScriptExecutor.class);
    private final GroovyShell groovyShell = new GroovyShell(new Binding());

    @Override
    public void executeScript(String scriptFileName) {
        executeScript(scriptFileName, ScriptConstants.DEFAULT_SCRIPT_METHOD, null);
    }

    @Override
    public void executeScript(String scriptFileName, String methodName, Object... args) {
        Preconditions.checkNotNull(scriptFileName, "Script file name is null");
        try {
            File scriptFile = new File(scriptFileName);
            Script script = groovyShell.parse(scriptFile);
            script.invokeMethod(methodName, args);
        } catch (FileNotFoundException e) {
            log.error("File {} not exists.", scriptFileName);
            throw new IllegalArgumentException("File not exists", e);
        } catch (IOException e) {
            log.error("IO error while reading file {}.", scriptFileName);
            throw new IllegalStateException("IO error", e);
        }
    }

}