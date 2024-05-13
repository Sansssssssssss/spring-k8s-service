package com.losaxa.framework.auth.mongo.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document("oauth_refresh_token")
public class MongoOauthRefreshToken {

    @Id
    private String tokenId;
    private byte[] token;
    private byte[] authentication;

}
