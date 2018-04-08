package com.ade.open.api.server.service;

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
public class SpringBeanFactoryTest {

    @Test
    public void getApplicationContext() throws Exception {
        assertNotNull(SpringBeanFactory.getApplicationContext());
    }

    @Test
    public void getTOpenSecretDao() throws Exception {
        assertNotNull(SpringBeanFactory.getTOpenSecretDao());
    }

}