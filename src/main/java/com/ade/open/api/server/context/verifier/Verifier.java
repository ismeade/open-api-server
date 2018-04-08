package com.ade.open.api.server.context.verifier;

import com.ade.open.api.server.service.SpringBeanFactory;
import com.ade.open.api.server.context.Context;
import com.ade.open.api.server.context.ReturnCode;
import com.ade.open.api.server.dao.TOpenSecretDao;
import com.ade.open.api.server.persistent.TOpenSecret;

import java.util.Map;

/**
 * Created by liyang on 18-2-7.
 */
public abstract class Verifier implements Context {

    private Context context;

    protected Verifier(Context context) {
        this.context = context;
    }

    @Override
    public String getId() {
        return context.getId();
    }

    @Override
    public Map<String, Object> getMap() {
        return context.getMap();
    }

    @Override
    public <T> T getParameter(String key, Class<T> clazz) {
        return context.getParameter(key, clazz);
    }

    @Override
    public String getRequestBody() {
        return context.getRequestBody();
    }

    @Override
    public String getResponseBody(long timeout) {
        return context.getResponseBody(timeout);
    }

    @Override
    public void setResponseBody(String body) {
        context.setResponseBody(body);
    }

    @Override
    public ReturnCode check() {
        return context.check();
    }

    protected TOpenSecret getOpenSecret() {
        String publicId = getParameter("customer_id", String.class);
        TOpenSecretDao tOpenSecretDao = SpringBeanFactory.getTOpenSecretDao();
        return tOpenSecretDao.queryOpenSecretByPublicId(publicId);
    }

}
