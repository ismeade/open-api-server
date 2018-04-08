package com.ade.open.api.server.context;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by liyang on 18-2-5.
 */
public class ReturnCodeTest {

    @Test
    public void getReturnCode() throws Exception {
        assertEquals("SUCCESS", ReturnCode.SUCCESS.getReturnCode());
    }

    @Test
    public void getReturnDesc() throws Exception {
        assertEquals("成功", ReturnCode.SUCCESS.getReturnDesc());
    }

    @Test
    public void toJson() throws Exception {
        assertEquals("{\"return_code\":\"SUCCESS\",\"return_desc\":\"成功\" }", ReturnCode.SUCCESS.toJson());
    }

    @Test
    public void toMap() throws Exception {
        Map<String, Object> map = ReturnCode.SUCCESS.toMap();
        assertNotNull(map);
        assertEquals(2, map.entrySet().size());
        assertEquals("SUCCESS", map.get("returnCode"));
        assertEquals("成功", map.get("returnDesc"));
    }

    @Test
    public void success() throws Exception {
        assertTrue(ReturnCode.success(ReturnCode.SUCCESS));
    }

}