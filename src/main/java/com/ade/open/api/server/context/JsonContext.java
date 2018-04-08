package com.ade.open.api.server.context;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by liyang on 18-2-5.
 */
public class JsonContext implements Context {

    private final static Logger LOG = LoggerFactory.getLogger(JsonContext.class);

    private static JacksonJsonParser jsonParser = new JacksonJsonParser();

    private String id;
    private Map<String, Object> map;
    private String requestBody;
    private String responseBody;

    private JsonContext(String body) {
        this.requestBody = body;
    }

    private void init() {
        initMap();
        initId();
    }

    private void initMap() {
        this.map = jsonParser.parseMap(requestBody);
    }

    private void initId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public <T> T getParameter(String key, Class<T> clazz) {
        if (Objects.isNull(map)
                || StringUtils.isEmpty(key)
                || Objects.isNull(clazz))
            return null;
        Object obj = map.get(key);
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        return null;
    }

    @Override
    public String getRequestBody() {
        return requestBody;
    }

    @Override
    public ReturnCode check() {
        if (Objects.isNull(map))
            return ReturnCode.ILLEGAL_MESSAGE;
        if (StringUtils.isEmpty(getParameter("customer_id", String.class))
                || StringUtils.isEmpty(getParameter("service", String.class))
                || StringUtils.isEmpty(getParameter("version", String.class))
                || StringUtils.isEmpty(getParameter("sign", String.class))) {
            return ReturnCode.MESSAGE_INCOMPLETE;
        } else {
            return ReturnCode.SUCCESS;
        }
    }

    @Override
    public final String getResponseBody(long timeout) {
        try {
            synchronized (this) {
                System.out.println(this);
                this.wait(timeout);
            }
        } catch (InterruptedException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return ReturnCode.SYSTEM_ERROR.toJson();
        }
        return Objects.isNull(responseBody) ? ReturnCode.SYSTEM_TIMEOUT.toJson() : responseBody;
    }

    @Override
    public final void setResponseBody(String responseBody) {
        synchronized (this) {
            this.responseBody = responseBody;
            notify();
        }
    }

    public static Context create(String body) {
        JsonContext context = new JsonContext(body);
        try {
            context.init();
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
        }
        return context;
    }

}
