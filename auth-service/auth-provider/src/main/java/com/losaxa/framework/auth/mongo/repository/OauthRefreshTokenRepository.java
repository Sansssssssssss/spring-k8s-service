package com.losaxa.framework.auth.mongo.repository;

import com.losaxa.core.mongo.BaseMongoRepository;
import com.losaxa.framework.auth.mongo.domain.MongoOauthRefreshToken;

public interface OauthRefreshTokenRepository extends BaseMongoRepository<MongoOauthRefreshToken, String> {
}
