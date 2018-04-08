package com.ade.open.api.server.context;

import java.util.Map;

/**
 * Created by liyang on 18-2-5.
 */
public interface Context {

    String getId();

    Map<String, Object> getMap();

    <T> T getParameter(String key, Class<T> clazz);

    String getRequestBody();

    String getResponseBody(long timeout);

    void setResponseBody(String body);

    ReturnCode check();

}
