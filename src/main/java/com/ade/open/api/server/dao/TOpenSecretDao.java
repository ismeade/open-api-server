package com.ade.open.api.server.dao;

import com.ade.open.api.server.persistent.TOpenSecret;

/**
 * Created by liyang on 18-2-5.
 */
public interface TOpenSecretDao {

    String CACHE_GROUP = "open-secret";

    TOpenSecret queryOpenSecretByPublicId(String publicId);

}
