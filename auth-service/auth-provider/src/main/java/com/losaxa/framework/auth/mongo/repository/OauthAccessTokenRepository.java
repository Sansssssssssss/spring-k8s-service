package com.losaxa.framework.auth.mongo.repository;

import com.losaxa.core.mongo.BaseMongoRepository;
import com.losaxa.framework.auth.mongo.domain.MongoOauthAccessToken;

public interface OauthAccessTokenRepository extends BaseMongoRepository<MongoOauthAccessToken, String> {

}
