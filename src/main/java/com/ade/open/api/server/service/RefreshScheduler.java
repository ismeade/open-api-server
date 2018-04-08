package com.ade.open.api.server.service;

import com.ade.open.api.server.dao.TOpenSecretDao;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.RefreshQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by liyang on 18-2-5.
 */
@Service
@EnableScheduling
public class RefreshScheduler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectContextFactory factory;

    @Scheduled(fixedRate = 300000) // 每5分钟执行
    public void refreshCacheGroup() {
        LOG.debug("refresh start.");
        try {
            ObjectContext context = factory.newContext();
            RefreshQuery refresh = new RefreshQuery(TOpenSecretDao.CACHE_GROUP);
            context.performGenericQuery(refresh);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        } finally {
            LOG.debug("refresh end.");
        }
    }

}
