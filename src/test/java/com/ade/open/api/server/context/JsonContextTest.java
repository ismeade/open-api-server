package com.ade.open.api.server.context;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by liyang on 18-2-8.
 */
public class JsonContextTest {


    private Context json = JsonContext.create("{}");
    private Context noJson = JsonContext.create("aaa");
    private Context nullStr = JsonContext.create(null);
    private Context dataJson = JsonContext.create("{\n" +
            "    \"service\": \"order.create\",\n" +
            "    \"version\": \"v1\",\n" +
            "    \"customer_id\": \"C3275820075738275325\",\n" +
            "    \"vm_id\":\"02000016\",\n" +
            "    \"skus\": [\n" +
            "        {\n" +
            "            \"sku_id\": \"3418\",\n" +
            "            \"sku_count\": \"1\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"order_type\": \"immediate\",\n" +
            "    \"start_day\": \"2018-01-29\",\n" +
            "    \"end_day\": \"2018-01-29\",\n" +
            "    \"sign\": \"FD0B32021795FF241E60CE91B349A86A\"\n" +
            "}");


    @Test
    public void getId() throws Exception {
        assertNotNull(json.getId());
        assertNull(noJson.getId());
        assertNull(nullStr.getId());
        assertNotNull(dataJson.getId());
    }

    @Test
    public void getMap() throws Exception {
        assertNotNull(json.getMap());
        assertNull(noJson.getMap());
        assertNull(nullStr.getMap());
        assertNotNull(dataJson.getMap());
    }

    @Test
    public void getParameter() throws Exception {
        assertNull(json.getParameter("a", String.class));
        assertNull(noJson.getParameter("a", String.class));
        assertNull(nullStr.getParameter("a", String.class));
        assertEquals("v1", dataJson.getParameter("version", String.class));
        assertEquals(null, dataJson.getParameter("version", Integer.class));
        assertEquals(null, dataJson.getParameter("", Integer.class));
    }

    @Test
    public void getRequestBody() throws Exception {
        assertNotNull(json.getRequestBody());
        assertNotNull(noJson.getRequestBody());
        assertNull(nullStr.getRequestBody());
        assertNotNull(dataJson.getRequestBody());
    }

    @Test
    public void getResponseBody() throws Exception {
        new Thread(() -> {
            assertEquals("abc", dataJson.getResponseBody(5000));
            assertEquals(null, json.getResponseBody(2999));
        }).start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataJson.setResponseBody("abc");
        json.setResponseBody("abc");
    }

    @Test
    public void check() throws Exception {
        assertEquals(ReturnCode.MESSAGE_INCOMPLETE, json.check());
        assertEquals(ReturnCode.ILLEGAL_MESSAGE, noJson.check());
        assertEquals(ReturnCode.ILLEGAL_MESSAGE, nullStr.check());
        assertEquals(ReturnCode.SUCCESS, dataJson.check());
    }

    @Test
    public void setResponseBody() throws Exception {
        // getResponseBody()
    }

}