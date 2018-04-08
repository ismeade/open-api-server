package com.ade.open.api.server.service;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by liyang on 18-2-5.
 */
@Component
public class ObjectContextFactory {

    private static ServerRuntime runtime;

    @Value("${driverClassName}")
    private String driverClassName;
    @Value("${url}")
    private String url;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${minIdle}")
    private int minIdle;
    @Value("${maxActive}")
    private int maxActive;
    @Value("${maxWait}")
    private int maxWait;

    public ObjectContext newContext() {
        if (Objects.isNull(runtime)) {
            synchronized (this) {
                buildServerRuntime();
            }
        }
        return newContextForRuntime();
    }

    private void buildServerRuntime() {
        if (Objects.isNull(runtime)) {
            runtime = ServerRuntime
                    .builder()
                    .addConfig("cayenne-project.xml")
                    .jdbcDriver(driverClassName)
                    .url(url)
                    .user(username)
                    .password(password)
                    .minConnections(minIdle)
                    .maxConnections(maxActive)
                    .maxQueueWaitTime(maxWait)
                    .build();
        }
    }

    private ObjectContext newContextForRuntime() {
        return runtime.newContext();
    }

}
