package com.ade.open.api.server.context.verifier;

import com.ade.open.api.server.context.Context;
import com.ade.open.api.server.context.JsonContext;
import com.ade.open.api.server.context.ReturnCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by liyang on 18-2-8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceAuthorizeVerifierTest {

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
    private Context dataJson2 = JsonContext.create("{\n" +
            "    \"service\": \"order.extract\",\n" +
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
    public void check() throws Exception {
        assertEquals(ReturnCode.MESSAGE_INCOMPLETE, new ServiceAuthorizeVerifier(json).check());
        assertEquals(ReturnCode.ILLEGAL_MESSAGE, new ServiceAuthorizeVerifier(noJson).check());
        assertEquals(ReturnCode.ILLEGAL_MESSAGE, new ServiceAuthorizeVerifier(nullStr).check());
        assertEquals(ReturnCode.SUCCESS, new ServiceAuthorizeVerifier(dataJson).check());
        assertEquals(ReturnCode.CUSTOMER_NO_AUTHORITY, new ServiceAuthorizeVerifier(dataJson2).check());
    }

}