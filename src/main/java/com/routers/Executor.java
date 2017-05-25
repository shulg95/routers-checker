package com.routers;

import com.routers.config.AppConfigKeys;
import com.routers.config.ApplicationConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Executor {

    public static void main(String[] args) throws Exception {

        String shedule =  ApplicationConfig.INSTANCE.getProperty(AppConfigKeys.SHEDULE);

        SingleCheckPerformer singleCheckPerformer = new SingleCheckPerformer();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(singleCheckPerformer, 0L, Long.parseLong(shedule), TimeUnit.SECONDS);
    }
}