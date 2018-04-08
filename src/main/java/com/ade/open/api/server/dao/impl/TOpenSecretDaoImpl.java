package com.ade.open.api.server.dao.impl;

import com.ade.open.api.server.dao.TOpenSecretDao;
import com.ade.open.api.server.persistent.TOpenSecret;
import com.ade.open.api.server.persistent.TOpenSecretService;
import com.ade.open.api.server.service.ObjectContextFactory;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by liyang on 18-2-5.
 */
@Service
public class TOpenSecretDaoImpl implements TOpenSecretDao {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectContextFactory objectContextFactory;

    @Override
    public TOpenSecret queryOpenSecretByPublicId(String publicId) {
        LOG.info("publicId {}", publicId);
        try {
            ObjectContext context = objectContextFactory.newContext();
            Assert.notNull(context, "context should not be null.");
            Assert.hasText(publicId, "publicId should has text.");
            return ObjectSelect
                    .query(TOpenSecret.class)
                    .where(TOpenSecret.PUBLIC_ID.eq(publicId))
                    .prefetch(TOpenSecret.SECRET_SERVICE.joint())
                    .prefetch(TOpenSecret.SECRET_SERVICE.dot(TOpenSecretService.SERVICE).joint())
                    .sharedCache(CACHE_GROUP)
                    .selectOne(context);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

}
