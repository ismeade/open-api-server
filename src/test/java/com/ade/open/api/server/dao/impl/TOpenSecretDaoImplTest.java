package com.ade.open.api.server.dao.impl;

import com.ade.open.api.server.dao.TOpenSecretDao;
import com.ade.open.api.server.persistent.TOpenSecret;
import com.ade.open.api.server.persistent.TOpenSecretService;
import com.ade.open.api.server.persistent.TOpenService;
import com.ade.open.api.server.service.SpringBeanFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by liyang on 18-2-6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TOpenSecretDaoImplTest {

    @Test
    public void queryOpenSecretByPublicId() throws Exception {
        String publicId = "C3275820075738275325";
        TOpenSecretDao tOpenSecretDao = SpringBeanFactory.getTOpenSecretDao();
        TOpenSecret tOpenSecret = tOpenSecretDao.queryOpenSecretByPublicId(publicId);
        assertNotNull(tOpenSecret);
        System.out.println(tOpenSecret);
        tOpenSecret.getSecretService().stream().map(TOpenSecretService::getService).map(TOpenService::getServiceName).forEach(System.out::println);
    }

}