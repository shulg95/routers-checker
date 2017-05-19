package com.routers.engine.script;

//Interface for script executors for any language.

public interface ScriptExecutor {

    /**
     * Method to execute default script method without arguments.
     * Could be used for test purposes.
     * Default method defined in ScriptConstants class.
     *
     * @param scriptFileName name of script file.
     */
    void executeScript(String scriptFileName);

    /**
     * @param scriptFileName name of script file.
     * @param methodName method to be executed
     * @param args method args
     */
    void executeScript(String scriptFileName, String methodName, Object... args);
}